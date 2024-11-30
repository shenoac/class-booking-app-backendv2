package com.mycompany.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private static final String TEST_USERNAME = "test@example.com";
    private static final String TEST_ROLE = "BUYER";
    private static final String EXPECTED_SECRET_KEY = "1c6pWNpwt61DR84xxvCT//QMp2jZ0QkwSFlrXzkvDq4=";


    @Test
    void testGetSecretKey() {
        byte[] secretKeyBytes = JwtUtil.getSecretKey();
        byte[] expectedKeyBytes = EXPECTED_SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        assertArrayEquals(expectedKeyBytes, secretKeyBytes, "The byte array of the secret key should match the expected value");
    }


    @Test
    void testGetSecretKeyAsString() {
        String expectedSecretKey = "1c6pWNpwt61DR84xxvCT//QMp2jZ0QkwSFlrXzkvDq4=";
        String actualSecretKey = JwtUtil.getSecretKeyAsString();
        assertEquals(expectedSecretKey, actualSecretKey, "The secret key as a string should match the expected value");
    }


    @Test
    void generateToken() {
        String token = JwtUtil.generateToken(TEST_USERNAME, TEST_ROLE);
        assertNotNull(token, "Token should not be null");
        assertTrue(token.split("\\.").length == 3, "Token should have 3 parts (header, payload, signature)");
    }

    @Test
    void validateToken() {
        String token = JwtUtil.generateToken(TEST_USERNAME, TEST_ROLE);
        Claims claims = JwtUtil.validateToken(token);

        assertNotNull(claims, "Claims should not be null");
        assertEquals(TEST_USERNAME, claims.getSubject(), "Username should match");
        assertEquals(TEST_ROLE, claims.get("role"), "Role should match");
    }

    @Test
    void extractEmailFromToken() {
        String token = JwtUtil.generateToken(TEST_USERNAME, TEST_ROLE);
        String email = JwtUtil.extractEmailFromToken(token);

        assertEquals(TEST_USERNAME, email, "Extracted email should match the username");
    }

    @Test
    void extractRoleFromToken() {
        String token = JwtUtil.generateToken(TEST_USERNAME, TEST_ROLE);
        String role = JwtUtil.extractRoleFromToken(token);

        assertEquals(TEST_ROLE, role,"Extracted role should match the expected role");
    }
}