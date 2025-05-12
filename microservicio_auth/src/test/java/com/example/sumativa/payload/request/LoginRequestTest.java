package com.example.sumativa.payload.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class LoginRequestTest {

    @Test
    void testConstructorAndGetters() {
        // given
        String email = "test@example.com";
        String password = "password123";

        // when
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);

        // then
        assertEquals(email, request.getEmail());
        assertEquals(password, request.getPassword());
    }

    @Test
    void testSetters() {
        // given
        LoginRequest request = new LoginRequest();
        String initialEmail = "initial@example.com";
        String initialPassword = "initial123";
        request.setEmail(initialEmail);
        request.setPassword(initialPassword);

        // when
        String newEmail = "new@example.com";
        String newPassword = "new123";
        request.setEmail(newEmail);
        request.setPassword(newPassword);

        // then
        assertEquals(newEmail, request.getEmail());
        assertEquals(newPassword, request.getPassword());
    }

    @Test
    void testEmptyValues() {
        // given
        LoginRequest request = new LoginRequest();

        // when
        request.setEmail("");
        request.setPassword("");

        // then
        assertEquals("", request.getEmail());
        assertEquals("", request.getPassword());
    }
} 