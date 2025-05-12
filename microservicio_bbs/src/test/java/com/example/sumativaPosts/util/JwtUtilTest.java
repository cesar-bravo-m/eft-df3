package com.example.sumativaPosts.util;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import io.jsonwebtoken.Claims;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private static final String JWT_SECRET = "b9c7f2f2db3b2c8c4b5d0d1c3d5f8e9a6b3c7d8e5f2a9b7c4d1e8f3a6b9c7d8a3f4e5a1b2c3d4e5f6g7h8i9j0k1l2";
    private static final long JWT_EXPIRATION_MS = 86400000; // 24 hours
    private static final String USERNAME = "testuser";
    private static final Integer USER_ID = 1;
    private static final List<String> ROLES = List.of("ROLE_NORMAL_USER");

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", JWT_SECRET);
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", JWT_EXPIRATION_MS);
    }

    @Test
    void generateToken_ShouldCreateValidToken() {
        String token = jwtUtil.generateToken(USERNAME, USER_ID, ROLES);
        
        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
        assertEquals(USERNAME, jwtUtil.extractUsername(token));
    }

    @Test
    void generateSampleToken_ShouldCreateValidToken() {
        String token = jwtUtil.generateSampleToken(USERNAME);
        
        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
        assertEquals(USERNAME, jwtUtil.extractUsername(token));
        
        Map<String, Object> claims = jwtUtil.getClaimsAsMap(token);
        assertEquals(USERNAME, claims.get("username"));
        assertEquals(1, claims.get("userId"));
        assertTrue(((List<?>) claims.get("roles")).contains("ROLE_NORMAL_USER"));
    }

    @Test
    void generateModeratorToken_ShouldCreateValidToken() {
        String token = jwtUtil.generateModeratorToken(USERNAME);
        
        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
        assertEquals(USERNAME, jwtUtil.extractUsername(token));
        
        Map<String, Object> claims = jwtUtil.getClaimsAsMap(token);
        assertEquals(USERNAME, claims.get("username"));
        assertEquals(2, claims.get("userId"));
        assertTrue(((List<?>) claims.get("roles")).contains("ROLE_MODERATOR"));
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername() {
        String token = jwtUtil.generateToken(USERNAME, USER_ID, ROLES);
        assertEquals(USERNAME, jwtUtil.extractUsername(token));
    }

    @Test
    void extractExpiration_ShouldReturnFutureDate() {
        String token = jwtUtil.generateToken(USERNAME, USER_ID, ROLES);
        Date expiration = jwtUtil.extractExpiration(token);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void extractAllClaims_ShouldReturnAllClaims() {
        String token = jwtUtil.generateToken(USERNAME, USER_ID, ROLES);
        Claims claims = jwtUtil.extractAllClaims(token);
        
        assertEquals(USERNAME, claims.getSubject());
        assertEquals(USERNAME, claims.get("username"));
        assertEquals(USER_ID, claims.get("userId"));
        assertTrue(((List<?>) claims.get("roles")).contains("ROLE_NORMAL_USER"));
    }

    @Test
    void extractClaim_ShouldReturnSpecificClaim() {
        String token = jwtUtil.generateToken(USERNAME, USER_ID, ROLES);
        String username = jwtUtil.extractClaim(token, Claims::getSubject);
        assertEquals(USERNAME, username);
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnTrue() {
        String token = jwtUtil.generateToken(USERNAME, USER_ID, ROLES);
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void validateToken_WithInvalidToken_ShouldReturnFalse() {
        assertFalse(jwtUtil.validateToken("invalid.token.here"));
    }

    @Test
    void getClaimsAsMap_ShouldReturnClaimsAsMap() {
        String token = jwtUtil.generateToken(USERNAME, USER_ID, ROLES);
        Map<String, Object> claims = jwtUtil.getClaimsAsMap(token);
        
        assertEquals(USERNAME, claims.get("username"));
        assertEquals(USER_ID, claims.get("userId"));
        assertTrue(((List<?>) claims.get("roles")).contains("ROLE_NORMAL_USER"));
    }

    @Test
    void validateToken_WithExpiredToken_ShouldReturnFalse() {
        // Create a token with a very short expiration
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", 1L);
        String token = jwtUtil.generateToken(USERNAME, USER_ID, ROLES);
        
        // Wait for token to expire
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        assertFalse(jwtUtil.validateToken(token));
    }
} 