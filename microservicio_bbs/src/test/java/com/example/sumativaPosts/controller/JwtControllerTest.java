package com.example.sumativaPosts.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.example.sumativaPosts.dto.JwtRequest;
import com.example.sumativaPosts.util.JwtUtil;

class JwtControllerTest {

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private JwtController jwtController;

    private String validToken;
    private String invalidToken;
    private Map<String, Object> claims;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validToken = "valid.jwt.token";
        invalidToken = "invalid.jwt.token";
        claims = new HashMap<>();
        claims.put("sub", "testuser");
        claims.put("roles", List.of("ROLE_USER"));
    }

    @Test
    void decodeToken_WithValidToken_ShouldReturnClaims() {
        when(jwtUtil.getClaimsAsMap(validToken)).thenReturn(claims);

        ResponseEntity<?> response = jwtController.decodeToken(validToken);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(claims, response.getBody());
        verify(jwtUtil).getClaimsAsMap(validToken);
    }

    @Test
    void decodeToken_WithInvalidToken_ShouldReturnBadRequest() {
        when(jwtUtil.getClaimsAsMap(invalidToken)).thenThrow(new RuntimeException("Invalid token"));

        ResponseEntity<?> response = jwtController.decodeToken(invalidToken);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid token: Invalid token", response.getBody());
        verify(jwtUtil).getClaimsAsMap(invalidToken);
    }

    @Test
    void decodeTokenPost_WithValidToken_ShouldReturnClaims() {
        JwtRequest request = new JwtRequest();
        request.setToken(validToken);
        when(jwtUtil.getClaimsAsMap(validToken)).thenReturn(claims);

        ResponseEntity<?> response = jwtController.decodeTokenPost(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(claims, response.getBody());
        verify(jwtUtil).getClaimsAsMap(validToken);
    }

    @Test
    void decodeTokenPost_WithInvalidToken_ShouldReturnBadRequest() {
        JwtRequest request = new JwtRequest();
        request.setToken(invalidToken);
        when(jwtUtil.getClaimsAsMap(invalidToken)).thenThrow(new RuntimeException("Invalid token"));

        ResponseEntity<?> response = jwtController.decodeTokenPost(request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid token: Invalid token", response.getBody());
        verify(jwtUtil).getClaimsAsMap(invalidToken);
    }

    @Test
    void generateSampleToken_ShouldReturnToken() {
        String username = "testuser";
        String expectedToken = "sample.jwt.token";
        when(jwtUtil.generateSampleToken(username)).thenReturn(expectedToken);

        ResponseEntity<?> response = jwtController.generateSampleToken(username);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedToken, response.getBody());
        verify(jwtUtil).generateSampleToken(username);
    }

    @Test
    void generateSampleToken_WithDefaultUsername_ShouldReturnToken() {
        String expectedToken = "sample.jwt.token";
        when(jwtUtil.generateSampleToken("testuser")).thenReturn(expectedToken);

        ResponseEntity<?> response = jwtController.generateSampleToken(null);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void generateNormalUserToken_ShouldReturnToken() {
        String username = "normaluser";
        Integer userId = 1;
        String expectedToken = "normal.user.jwt.token";
        when(jwtUtil.generateToken(username, userId, List.of("ROLE_NORMAL_USER"))).thenReturn(expectedToken);

        ResponseEntity<?> response = jwtController.generateNormalUserToken(username, userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedToken, response.getBody());
        verify(jwtUtil).generateToken(username, userId, List.of("ROLE_NORMAL_USER"));
    }

    @Test
    void generateNormalUserToken_WithDefaultValues_ShouldReturnToken() {
        String expectedToken = "normal.user.jwt.token";
        when(jwtUtil.generateToken("normaluser", 1, List.of("ROLE_NORMAL_USER"))).thenReturn(expectedToken);

        ResponseEntity<?> response = jwtController.generateNormalUserToken(null, null);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void generateModeratorToken_ShouldReturnToken() {
        String username = "moderator";
        Integer userId = 2;
        String expectedToken = "moderator.jwt.token";
        when(jwtUtil.generateToken(username, userId, List.of("ROLE_MODERATOR"))).thenReturn(expectedToken);

        ResponseEntity<?> response = jwtController.generateModeratorToken(username, userId);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void generateModeratorToken_WithDefaultValues_ShouldReturnToken() {
        String expectedToken = "moderator.jwt.token";
        when(jwtUtil.generateToken("moderator", 2, List.of("ROLE_MODERATOR"))).thenReturn(expectedToken);

        ResponseEntity<?> response = jwtController.generateModeratorToken(null, null);

        assertEquals(200, response.getStatusCodeValue());
    }
} 