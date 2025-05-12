package com.example.sumativa.security.jwt;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.example.sumativa.model.ERole;
import com.example.sumativa.model.User;
import com.example.sumativa.security.services.UserDetailsImpl;

class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private static final String TEST_SECRET = "testSecretKeyWhichIsAtLeast32BytesLongForTesting";
    private static final int TEST_EXPIRATION = 3600000; // 1 hour

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        // Set test values using reflection
        try {
            java.lang.reflect.Field secretField = JwtUtils.class.getDeclaredField("jwtSecret");
            secretField.setAccessible(true);
            secretField.set(jwtUtils, TEST_SECRET);

            java.lang.reflect.Field expirationField = JwtUtils.class.getDeclaredField("jwtExpirationMs");
            expirationField.setAccessible(true);
            expirationField.set(jwtUtils, TEST_EXPIRATION);
        } catch (Exception e) {
            fail("Failed to set up test environment", e);
        }
    }

    @Test
    void testGenerateJwtToken() {
        // given
        User user = new User("testuser", "test@example.com", "password123");
        user.setId(1L);
        user.setRole(ERole.MODERATOR);
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());

        // when
        String token = jwtUtils.generateJwtToken(authentication);

        // then
        assertNotNull(token);
        assertTrue(jwtUtils.validateJwtToken(token));
        assertEquals("testuser", jwtUtils.getUserNameFromJwtToken(token));
        assertEquals(1L, jwtUtils.getUserIdFromJwtToken(token));
        List<String> roles = jwtUtils.getRolesFromJwtToken(token);
        assertEquals(1, roles.size());
        assertTrue(roles.contains("ROLE_MODERATOR"));
    }

    @Test
    void testGenerateJwtTokenWithNormalPoster() {
        // given
        User user = new User("normaluser", "normal@example.com", "password123");
        user.setId(2L);
        user.setRole(ERole.NORMAL_POSTER);
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());

        // when
        String token = jwtUtils.generateJwtToken(authentication);

        // then
        assertNotNull(token);
        assertTrue(jwtUtils.validateJwtToken(token));
        assertEquals("normaluser", jwtUtils.getUserNameFromJwtToken(token));
        assertEquals(2L, jwtUtils.getUserIdFromJwtToken(token));
        List<String> roles = jwtUtils.getRolesFromJwtToken(token);
        assertEquals(1, roles.size());
        assertTrue(roles.contains("ROLE_NORMAL_POSTER"));
    }

    @Test
    void testValidateJwtToken_InvalidToken() {
        // given
        String invalidToken = "invalid.token.here";

        // when
        boolean isValid = jwtUtils.validateJwtToken(invalidToken);

        // then
        assertFalse(isValid);
    }

    @Test
    void testValidateJwtToken_ExpiredToken() {
        // given
        User user = new User("testuser", "test@example.com", "password123");
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());

        // Create a JwtUtils instance with very short expiration
        JwtUtils shortExpirationJwtUtils = new JwtUtils();
        try {
            java.lang.reflect.Field secretField = JwtUtils.class.getDeclaredField("jwtSecret");
            secretField.setAccessible(true);
            secretField.set(shortExpirationJwtUtils, TEST_SECRET);

            java.lang.reflect.Field expirationField = JwtUtils.class.getDeclaredField("jwtExpirationMs");
            expirationField.setAccessible(true);
            expirationField.set(shortExpirationJwtUtils, 1); // 1 millisecond
        } catch (Exception e) {
            fail("Failed to set up test environment", e);
        }

        String token = shortExpirationJwtUtils.generateJwtToken(authentication);

        // Wait for token to expire
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // when
        boolean isValid = jwtUtils.validateJwtToken(token);

        // then
        assertFalse(isValid);
    }

    @Test
    void testValidateJwtToken_EmptyToken() {
        // when
        boolean isValid = jwtUtils.validateJwtToken("");

        // then
        assertFalse(isValid);
    }

    @Test
    void testValidateJwtToken_NullToken() {
        // when
        boolean isValid = jwtUtils.validateJwtToken(null);

        // then
        assertFalse(isValid);
    }
} 