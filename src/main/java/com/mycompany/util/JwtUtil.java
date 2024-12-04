package com.mycompany.util;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import java.nio.charset.StandardCharsets;
import java.util.Date;

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

    public static byte[] getSecretKey() {
        return SECRET_KEY.getBytes(StandardCharsets.UTF_8);
    }

    public static String getSecretKeyAsString() {
        return SECRET_KEY;
    }

    public static String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS256, getSecretKey())
                .compact();
    }

    private static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static Claims validateToken(String token) {
        try {
            return parseToken(token);
        } catch (SignatureException e) {
            throw new JwtValidationException("Invalid JWT token signature", e);
        } catch (Exception e) {
            throw new JwtValidationException("Error validating JWT token", e);
        }
    }

    static String stripBearerPrefix(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }

    public static String extractEmailFromToken(String token) {
        Claims claims = validateToken(stripBearerPrefix(token));
        return claims.getSubject();
    }

    public static String extractRoleFromToken(String token) {
        Claims claims = validateToken(stripBearerPrefix(token));
        Object role = claims.get("role");
        return role != null ? role.toString() : null;
    }
}
