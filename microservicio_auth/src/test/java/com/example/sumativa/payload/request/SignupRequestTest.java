package com.example.sumativa.payload.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.example.sumativa.model.ERole;

class SignupRequestTest {

    @Test
    void testConstructorAndGetters() {
        // given
        String username = "testuser";
        String email = "test@example.com";
        String password = "password123";
        ERole role = ERole.MODERATOR;

        // when
        SignupRequest request = new SignupRequest();
        request.setUsername(username);
        request.setEmail(email);
        request.setPassword(password);
        request.setRole(role);

        // then
        assertEquals(username, request.getUsername());
        assertEquals(email, request.getEmail());
        assertEquals(password, request.getPassword());
        assertEquals(role, request.getRole());
    }

    @Test
    void testSetters() {
        // given
        SignupRequest request = new SignupRequest();
        String initialUsername = "initialuser";
        String initialEmail = "initial@example.com";
        String initialPassword = "initial123";
        ERole initialRole = ERole.NORMAL_POSTER;
        request.setUsername(initialUsername);
        request.setEmail(initialEmail);
        request.setPassword(initialPassword);
        request.setRole(initialRole);

        // when
        String newUsername = "newuser";
        String newEmail = "new@example.com";
        String newPassword = "new123";
        ERole newRole = ERole.MODERATOR;
        request.setUsername(newUsername);
        request.setEmail(newEmail);
        request.setPassword(newPassword);
        request.setRole(newRole);

        // then
        assertEquals(newUsername, request.getUsername());
        assertEquals(newEmail, request.getEmail());
        assertEquals(newPassword, request.getPassword());
        assertEquals(newRole, request.getRole());
    }

    @Test
    void testEmptyValues() {
        // given
        SignupRequest request = new SignupRequest();

        // when
        request.setUsername("");
        request.setEmail("");
        request.setPassword("");

        // then
        assertEquals("", request.getUsername());
        assertEquals("", request.getEmail());
        assertEquals("", request.getPassword());
    }

    @Test
    void testNullRole() {
        // given
        SignupRequest request = new SignupRequest();

        // when
        request.setRole(null);

        // then
        assertEquals(ERole.NORMAL_POSTER, request.getRole());
    }

    @Test
    void testIsModerator() {
        // given
        SignupRequest request = new SignupRequest();

        // when
        request.setRole(ERole.MODERATOR);

        // then
        assertTrue(request.isModerator());

        // when
        request.setRole(ERole.NORMAL_POSTER);

        // then
        assertFalse(request.isModerator());
    }
} 