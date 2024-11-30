package com.mycompany.util;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Utility class for working with JSON Web Tokens (JWT).
 * Provides methods for generating, validating, and parsing JWTs.
 */
public class JwtUtil {

    private static final String SECRET_KEY;

    static {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        String envKey = dotenv.get("JWT_SECRET_KEY");
        String systemKey = System.getenv("JWT_SECRET_KEY");

        SECRET_KEY = envKey != null ? envKey : (systemKey != null ? systemKey : "defaultFallbackSecret");
    }

    /**
     * Returns the secret key as a byte array.
     *
     * @return the secret key in UTF-8 encoding.
     */
    public static byte[] getSecretKey() {
        return SECRET_KEY.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Returns the secret key as a string.
     *
     * @return the secret key as a plain string.
     */
    public static String getSecretKeyAsString() {
        return SECRET_KEY;
    }

    /**
     * Generates a JWT token with the specified username and role.
     *
     * @param username the username to include in the token.
     * @param role     the role to include in the token.
     * @return a signed JWT token.
     */
    public static String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS256, getSecretKey())
                .compact();
    }

    /**
     * Parses a JWT token and retrieves its claims.
     *
     * @param token the JWT token to parse.
     * @return the claims contained in the token.
     */
    private static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Validates a JWT token and returns its claims.
     *
     * @param token the JWT token to validate.
     * @return the claims contained in the token.
     * @throws JwtValidationException if the token is invalid or cannot be validated.
     */
    public static Claims validateToken(String token) {
        try {
            return parseToken(token);
        } catch (SignatureException e) {
            throw new JwtValidationException("Invalid JWT token signature", e);
        } catch (Exception e) {
            throw new JwtValidationException("Error validating JWT token", e);
        }
    }

    /**
     * Removes the "Bearer " prefix from a token if present.
     *
     * @param token the token string to process.
     * @return the token without the "Bearer " prefix, or the original token if the prefix is absent.
     */
    private static String stripBearerPrefix(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }

    /**
     * Extracts the username (email) from a JWT token.
     *
     * @param token the JWT token.
     * @return the username (email) contained in the token.
     * @throws JwtValidationException if the token is invalid.
     */
    public static String extractEmailFromToken(String token) {
        Claims claims = validateToken(stripBearerPrefix(token));
        return claims.getSubject();
    }

    /**
     * Extracts the role from a JWT token.
     *
     * @param token the JWT token.
     * @return the role contained in the token, or {@code null} if not present.
     * @throws JwtValidationException if the token is invalid.
     */
    public static String extractRoleFromToken(String token) {
        Claims claims = validateToken(stripBearerPrefix(token));
        Object role = claims.get("role");
        return role != null ? role.toString() : null;
    }
}
