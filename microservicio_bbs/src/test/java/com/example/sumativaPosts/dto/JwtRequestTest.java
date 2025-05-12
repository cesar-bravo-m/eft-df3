package com.example.sumativaPosts.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtRequestTest {

    private JwtRequest jwtRequest;
    private static final String TEST_TOKEN = "test.jwt.token";

    @BeforeEach
    void setUp() {
        jwtRequest = new JwtRequest();
    }

    @Test
    void testNoArgsConstructor() {
        assertNull(jwtRequest.getToken());
    }

    @Test
    void testAllArgsConstructor() {
        JwtRequest request = new JwtRequest(TEST_TOKEN);
        assertEquals(TEST_TOKEN, request.getToken());
    }

    @Test
    void testSetAndGetToken() {
        jwtRequest.setToken(TEST_TOKEN);
        assertEquals(TEST_TOKEN, jwtRequest.getToken());
    }
} 