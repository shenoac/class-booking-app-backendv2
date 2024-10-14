package com.mycompany.user.service;

import com.mycompany.user.util.JwtUtil;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import com.mycompany.user.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import io.jsonwebtoken.Claims;
import io.quarkus.mailer.Mailer;
import io.quarkus.mailer.Mail;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class UserService {

    @Inject
    Mailer mailer;

    @Inject
    EntityManager entityManager;

    @ConfigProperty(name = "app.frontend.url")
    String frontendUrl;

    @ConfigProperty(name = "app.server.url")
    String serverUrl;

    public User findUserByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        return query.getResultStream().findFirst().orElse(null);  // Return user if found, else null
    }

    public boolean resetPassword(String token, String newPassword) {
        try {
            // Parse and validate the JWT token
            Claims claims = Jwts.parser()
                    .setSigningKey(JwtUtil.getSecretKey())  // Validate using the same secret key
                    .parseClaimsJws(token)
                    .getBody();

            // Extract user ID from the token
            Long userId = Long.parseLong(claims.getSubject());

            // Find the user by ID using Panache
            User user = User.findById(userId);
            if (user == null) {
                return false; // User not found
            }

            // Hash the new password
            String hashedNewPassword = BcryptUtil.bcryptHash(newPassword);

            // Update the user's password
            user.password = hashedNewPassword;
            user.persist();  // Persist the changes

            return true;  // Password successfully changed

        } catch (Exception e) {
            // Handle token parsing errors or invalid tokens
            return false;
        }
    }


    public String getUserRole(String username) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", username);
        User user = query.getSingleResult();
        return user != null ? user.getRole() : null;
    }

    // This method handles password changes when the user knows the current password
    public boolean changePassword(String email, String currentPassword, String newPassword) {
        User user = User.find("email", email).firstResult();

        if (user != null && BcryptUtil.matches(currentPassword, user.password)) {
            user.password = BcryptUtil.bcryptHash(newPassword);
            user.persist();
            return true;
        }
        return false;
    }

    // This method handles account deletion
    public boolean deleteAccount(String email) {
        User user = User.find("email", email).firstResult();
        if (user != null) {
            User.delete("email", email);
            return true;
        }
        return false;
    }

    public boolean sendPasswordResetToken(String email) {
        User user = User.find("email", email).firstResult();

        if (user == null) {
            return false; // Email not found
        }

        // Generate a password reset token (JWT)
        String token = Jwts.builder()
                .setSubject(user.id.toString())
                .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000)) // 15-minute expiration
                .signWith(SignatureAlgorithm.HS256, JwtUtil.getSecretKey())
                .compact();

        // Build the reset link using the frontend URL
        String resetLink = frontendUrl.replaceAll("/$", "") + "/password-reset?token=" + token;

        // Send the email
        mailer.send(Mail.withText(
                email,  // Recipient's email
                "Password Reset Request",  // Subject
                "Hello, " + user.name + ",\n\n"
                        + "You have requested to reset your password. Click the link below to reset it:\n"
                        + resetLink + "\n\n"
                        + "If you didn't request this, please ignore this email."
        ));

        return true;
    }

    public boolean sendEmailVerificationToken(String email) {
        User user = User.find("email", email).firstResult();

        if (user == null) {
            return false;
        }

        // Generate an email verification token (JWT)
        String token = Jwts.builder()
                .setSubject(user.id.toString())
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 24-hour expiration
                .signWith(SignatureAlgorithm.HS256, JwtUtil.getSecretKey())
                .compact();

        // Build the verification link using the server URL
        String verificationLink = frontendUrl.replaceAll("/$", "") + "/verify-email?token=" + token;

        // Send the email
        mailer.send(Mail.withText(
                email,  // Recipient's email
                "Email Verification",  // Subject
                "Hello, " + user.name + ",\n\n"
                        + "Please verify your email by clicking the link below:\n"
                        + verificationLink + "\n\n"
                        + "If you didn't request this, please ignore this email."
        ));

        return true;
    }

        @Transactional
        public boolean verifyEmail(String token) {
        try {
            // Parse and validate the JWT token
            Claims claims = Jwts.parser()
                    .setSigningKey(JwtUtil.getSecretKey())  // Validate using the same secret key
                    .parseClaimsJws(token)
                    .getBody();

            // Log the claims for debugging
            System.out.println("Claims extracted from token: " + claims);

            // Extract user ID from the token
            Long userId = Long.parseLong(claims.getSubject());

            // Log the user ID for debugging
            System.out.println("Extracted user ID: " + userId);

            // Find the user by ID using Panache
            User user = User.findById(userId);
            if (user == null) {
                System.out.println("User not found for ID: " + userId);  // Log if the user isn't found
                return false; // User not found
            }

            // Log before activation
            System.out.println("User found: " + user.email + ", activating...");

            // Activate the user
            user.activated = true;
            user.persist();
            System.out.println("User activated and changes persisted: " + user.activated);

            return true;  // Email successfully verified

        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return false;
        }
    }

}