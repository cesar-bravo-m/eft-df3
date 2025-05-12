package com.example.sumativa.payload.response;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.example.sumativa.model.ERole;

class JwtResponseTest {

    @Test
    void testConstructorAndGetters() {
        // given
        String token = "test.jwt.token";
        Long id = 1L;
        String username = "testuser";
        String email = "test@example.com";
        ERole role = ERole.MODERATOR;
        List<String> roles = Arrays.asList("ROLE_MODERATOR");

        // when
        JwtResponse response = new JwtResponse(token, id, username, email, role, roles);

        // then
        assertEquals(token, response.getAccessToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(id, response.getId());
        assertEquals(username, response.getUsername());
        assertEquals(email, response.getEmail());
        assertEquals(role, response.getRole());
        assertEquals(roles, response.getRoles());
        assertTrue(response.isModerator());
    }

    @Test
    void testSetters() {
        // given
        JwtResponse response = new JwtResponse("old.token", 1L, "olduser", "old@example.com", ERole.NORMAL_POSTER, Arrays.asList("ROLE_NORMAL_POSTER"));

        // when
        response.setAccessToken("new.token");
        response.setTokenType("Custom");
        response.setId(2L);
        response.setUsername("newuser");
        response.setEmail("new@example.com");
        response.setRole(ERole.MODERATOR);

        // then
        assertEquals("new.token", response.getAccessToken());
        assertEquals("Custom", response.getTokenType());
        assertEquals(2L, response.getId());
        assertEquals("newuser", response.getUsername());
        assertEquals("new@example.com", response.getEmail());
        assertEquals(ERole.MODERATOR, response.getRole());
        assertTrue(response.isModerator());
    }

    @Test
    void testNormalPosterRole() {
        // given
        JwtResponse response = new JwtResponse("token", 1L, "user", "user@example.com", ERole.NORMAL_POSTER, Arrays.asList("ROLE_NORMAL_POSTER"));

        // then
        assertFalse(response.isModerator());
    }

    @Test
    void testEmptyRoles() {
        // given
        JwtResponse response = new JwtResponse("token", 1L, "user", "user@example.com", ERole.NORMAL_POSTER, Arrays.asList());

        // then
        assertFalse(response.isModerator());
        assertTrue(response.getRoles().isEmpty());
    }

    @Test
    void testNullValues() {
        // given
        JwtResponse response = new JwtResponse(null, null, null, null, null, null);

        // then
        assertNull(response.getAccessToken());
        assertEquals("Bearer", response.getTokenType()); // Default value
        assertNull(response.getId());
        assertNull(response.getUsername());
        assertNull(response.getEmail());
        assertNull(response.getRole());
        assertNull(response.getRoles());
        assertFalse(response.isModerator());
    }

    @Test
    void testSetNullValues() {
        // given
        JwtResponse response = new JwtResponse("token", 1L, "user", "user@example.com", ERole.MODERATOR, Arrays.asList("ROLE_MODERATOR"));

        // when
        response.setAccessToken(null);
        response.setTokenType(null);
        response.setId(null);
        response.setUsername(null);
        response.setEmail(null);
        response.setRole(null);

        // then
        assertNull(response.getAccessToken());
        assertNull(response.getTokenType());
        assertNull(response.getId());
        assertNull(response.getUsername());
        assertNull(response.getEmail());
        assertNull(response.getRole());
        assertFalse(response.isModerator());
    }
} 