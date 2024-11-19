package com.mycompany.util;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.logging.Logger;

public class JwtUtil {

    private static final Logger LOGGER = Logger.getLogger(JwtUtil.class.getName());


    private static final String SECRET_KEY;

    static {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing() // Don't fail if .env doesn't exist
                .load();

        // Try to load from .env first
        String envKey = dotenv.get("JWT_SECRET_KEY");
        String systemKey = System.getenv("JWT_SECRET_KEY");

        // Use the first non-null key found
        SECRET_KEY = envKey != null ? envKey : (systemKey != null ? systemKey : "defaultFallbackSecret");

        LOGGER.info("JWT Secret Key initialized: " + SECRET_KEY);
        LOGGER.info("JWT Secret Key length (in bits): " + (SECRET_KEY.getBytes(StandardCharsets.UTF_8).length * 8));
    }


    // Method to return the secret key as a byte array
    public static byte[] getSecretKey() {
        return SECRET_KEY.getBytes(StandardCharsets.UTF_8); // Used by UserService
    }

    // Optional: Method to return the secret key as a String
    public static String getSecretKeyAsString() {
        return SECRET_KEY;
    }

    // Generate a JWT token
    public static String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour expiration
                .signWith(SignatureAlgorithm.HS256, getSecretKey()) // Use byte[] for signing
                .compact();
    }

    // Validate a JWT token
    public static Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSecretKey()) // Use byte[] for validation
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    // Extract email from token
    public static String extractEmailFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Claims claims = validateToken(token);
        return claims.getSubject();
    }

    // Extract role from token
    public static String extractRoleFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Claims claims = validateToken(token);
        return (String) claims.get("role");
    }
}
