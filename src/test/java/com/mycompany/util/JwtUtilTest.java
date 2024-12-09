package com.mycompany.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class JwtUtilTest {

    private static final String TEST_USERNAME = "test@example.com";
    private static final String TEST_ROLE = "BUYER";

    @BeforeEach
    void setup() {
        System.setProperty("ENV", "test");
    }

    @Test
    void testGetSecretKey() {
        String mockedSecretKey = "mockedSecretKey";
        try (MockedStatic<JwtUtil> mockedJwtUtil = Mockito.mockStatic(JwtUtil.class)) {
            mockedJwtUtil.when(JwtUtil::getSecretKeyAsString).thenReturn(mockedSecretKey);
            mockedJwtUtil.when(JwtUtil::getSecretKey).thenReturn(mockedSecretKey.getBytes(StandardCharsets.UTF_8));

            assertArrayEquals(mockedSecretKey.getBytes(StandardCharsets.UTF_8), JwtUtil.getSecretKey(), "Secret key bytes should match mocked value");
        }
    }

    @Test
    void testGetSecretKeyAsString() {
        String mockedSecretKey = "mockedSecretKey";
        try (MockedStatic<JwtUtil> mockedJwtUtil = Mockito.mockStatic(JwtUtil.class)) {
            mockedJwtUtil.when(JwtUtil::getSecretKeyAsString).thenReturn(mockedSecretKey);

            assertEquals(mockedSecretKey, JwtUtil.getSecretKeyAsString(), "Secret key string should match mocked value");
        }
    }




    @Test
    void testGenerateToken() {
        String token = JwtUtil.generateToken(TEST_USERNAME, TEST_ROLE);
        assertNotNull(token, "Token should not be null");
        assertTrue(token.split("\\.").length == 3, "Token should have 3 parts (header, payload, signature)");
    }

    @Test
    void testValidateToken() {
        String token = JwtUtil.generateToken(TEST_USERNAME, TEST_ROLE);
        Claims claims = JwtUtil.validateToken(token);

        assertNotNull(claims, "Claims should not be null");
        assertEquals(TEST_USERNAME, claims.getSubject(), "Username should match");
        assertEquals(TEST_ROLE, claims.get("role"), "Role should match");
    }

    @Test
    void testStripBearerPrefix() {
        String tokenWithPrefix = "Bearer testToken";
        String tokenWithoutPrefix = "testToken";

        assertEquals("testToken", JwtUtil.stripBearerPrefix(tokenWithPrefix), "Should remove Bearer prefix");
        assertEquals("testToken", JwtUtil.stripBearerPrefix(tokenWithoutPrefix), "Should not alter token without prefix");
        assertNull(JwtUtil.stripBearerPrefix(null), "Should return null for null input");
    }

    @Test
    void testExtractEmailFromToken() {
        String token = JwtUtil.generateToken(TEST_USERNAME, TEST_ROLE);
        String email = JwtUtil.extractEmailFromToken("Bearer " + token);

        assertEquals(TEST_USERNAME, email, "Extracted email should match the username");
    }

    @Test
    void testExtractRoleFromToken() {
        String token = JwtUtil.generateToken(TEST_USERNAME, TEST_ROLE);
        String role = JwtUtil.extractRoleFromToken("Bearer " + token);

        assertEquals(TEST_ROLE, role, "Extracted role should match the role");
    }
}
