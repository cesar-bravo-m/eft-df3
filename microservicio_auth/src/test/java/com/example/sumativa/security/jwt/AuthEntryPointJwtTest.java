package com.example.sumativa.security.jwt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

class AuthEntryPointJwtTest {

    private AuthEntryPointJwt authEntryPointJwt;

    @Mock
    private AuthenticationException authException;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        authEntryPointJwt = new AuthEntryPointJwt();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        outputStream = new ByteArrayOutputStream();
        response.getWriter().write(""); // Initialize the writer
    }

    @Test
    void testCommence() throws IOException, ServletException {
        // given
        String errorMessage = "Test error message";
        String servletPath = "/api/test";
        request.setServletPath(servletPath);
        when(authException.getMessage()).thenReturn(errorMessage);

        // when
        authEntryPointJwt.commence(request, response, authException);

        // then
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        // Verify response body
        String responseBody = response.getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseMap = mapper.readValue(responseBody, Map.class);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, responseMap.get("status"));
        assertEquals("Unauthorized", responseMap.get("error"));
        assertEquals(errorMessage, responseMap.get("message"));
        assertEquals(servletPath, responseMap.get("path"));
    }

    @Test
    void testCommenceWithNullMessage() throws IOException, ServletException {
        // given
        String servletPath = "/api/test";
        request.setServletPath(servletPath);
        when(authException.getMessage()).thenReturn(null);

        // when
        authEntryPointJwt.commence(request, response, authException);

        // then
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        // Verify response body
        String responseBody = response.getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseMap = mapper.readValue(responseBody, Map.class);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, responseMap.get("status"));
        assertEquals("Unauthorized", responseMap.get("error"));
        assertNull(responseMap.get("message"));
        assertEquals(servletPath, responseMap.get("path"));
    }

    @Test
    void testCommenceWithEmptyServletPath() throws IOException, ServletException {
        // given
        String errorMessage = "Test error message";
        request.setServletPath("");
        when(authException.getMessage()).thenReturn(errorMessage);

        // when
        authEntryPointJwt.commence(request, response, authException);

        // then
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        // Verify response body
        String responseBody = response.getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseMap = mapper.readValue(responseBody, Map.class);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, responseMap.get("status"));
        assertEquals("Unauthorized", responseMap.get("error"));
        assertEquals(errorMessage, responseMap.get("message"));
        assertEquals("", responseMap.get("path"));
    }
} 