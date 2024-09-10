package com.mycompany.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import java.util.Base64;
import java.util.Date;

public class JwtUtil {
    // Base64-encoded secret key
    private static final String BASE64_ENCODED_SECRET_KEY = "TzcmWWRvYXohdGIkanZJVnNVU3ljN3FPTk02WHJtQUY3azl0YlMmPw==";

    // Decode Base64 key for internal use
    private static final byte[] SECRET_KEY = Base64.getDecoder().decode(BASE64_ENCODED_SECRET_KEY);

    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour expiration
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static byte[] getSecretKey() {
        return SECRET_KEY;
    }

    public static Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    public static String extractEmailFromToken(String token) {
        // Remove "Bearer " prefix if present
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Parse the token to get claims
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Extract the email from claims
        return claims.getSubject();
    }
}
