package com.mycompany.user.controller;

import com.mycompany.user.dto.*;
import com.mycompany.user.TokenResponse;
import com.mycompany.user.model.User;
import com.mycompany.user.util.JwtUtil;
import com.mycompany.user.service.UserService; // Import UserService
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject; // Import Inject
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.transaction.TransactionManager;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    UserService userService; // Inject UserService

    @Inject
    TransactionManager transactionManager;

    // Login endpoint to authenticate users and generate JWT
    @POST
    @Path("/login")
    @PermitAll
    public Response login(@Valid LoginDTO loginDTO) {
        // Find user by email
        User user = User.find("email", loginDTO.email).firstResult();

        // Check if user exists and has activated their account
        if (user == null || !user.activated || !BcryptUtil.matches(loginDTO.password, user.password)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials or account not activated").build();
        }

        // Generate JWT token with user role using JwtUtil
        String token = JwtUtil.generateToken(user.email);

        return Response.ok(new TokenResponse(token)).build();
    }

    // Register a new buyer
    @POST
    @Path("/register/buyer")
    @PermitAll
    @Transactional
    public Response registerBuyer(@Valid UserDTO userDTO) {
        Response response = registerWithRole(userDTO, "BUYER");

        // If registration is successful, send verification email
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            userService.sendEmailVerificationToken(userDTO.email);  // Send verification email
        }

        return response;
    }


    // Register a new artist
    @POST
    @Path("/register/artist")
    @PermitAll
    @Transactional
    public Response registerArtist(@Valid UserDTO userDTO) {
        return registerWithRole(userDTO, "ARTIST");
    }

    // Reusable method for registration with a specific role
    private Response registerWithRole(UserDTO userDTO, String role) {
        // Check if the user with the same email already exists
        if (User.find("email", userDTO.email).firstResult() != null) {
            return Response.status(Response.Status.CONFLICT).entity("Email already registered").build();
        }

        // Add the user to the database with hashed password and role
        User.add(userDTO.email, userDTO.password, role, userDTO.name);
        return Response.ok("User successfully registered with role: " + role).build();
    }

    @PUT
    @Path("/change-password")
    @Transactional
    public Response changePassword(@HeaderParam("Authorization") String token, @Valid PasswordChangeDTO passwordChangeDTO) {
        // Extract email from JWT token
        String email = JwtUtil.extractEmailFromToken(token);

        // Delegate password change to UserService
        boolean success = userService.changePassword(email, passwordChangeDTO.getCurrentPassword(), passwordChangeDTO.getNewPassword());

        if (success) {
            return Response.ok("Password changed successfully").build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Current password is incorrect").build();
        }
    }

    @PUT
    @Path("/reset-password")
    @Transactional
    public Response resetPassword(@Valid PasswordResetDTO passwordResetDTO) {
        boolean isReset = userService.resetPassword(passwordResetDTO.getToken(), passwordResetDTO.getNewPassword());

        if (isReset) {
            return Response.ok("Password reset successfully.").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid or expired token.").build();
        }
    }

    @DELETE
    @Path("/delete-account")
    @Transactional
    public Response deleteAccount(@HeaderParam("Authorization") String token) {
        // Extract email from JWT token
        String email = JwtUtil.extractEmailFromToken(token);

        // Delegate account deletion to UserService
        boolean success = userService.deleteAccount(email);

        if (success) {
            return Response.ok("Account successfully deleted").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to delete account").build();
        }
    }

    @POST
    @Path("/request-password-reset")
    @PermitAll
    public Response requestPasswordReset(@Valid PasswordResetRequestDTO request) {
        boolean isEmailSent = userService.sendPasswordResetToken(request.getEmail());

        if (isEmailSent) {
            return Response.ok().entity("{\"message\":\"Password reset token generated and logged.\"}").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Email not found.\"}").build();
        }
    }

    @GET
    @Path("/verify-email")
    @Transactional
    public Response verifyEmail(@QueryParam("token") String token) {
        boolean isVerified = userService.verifyEmail(token);

        if (isVerified) {
            return Response.ok("Email successfully verified.").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid or expired token.").build();
        }
    }

}
