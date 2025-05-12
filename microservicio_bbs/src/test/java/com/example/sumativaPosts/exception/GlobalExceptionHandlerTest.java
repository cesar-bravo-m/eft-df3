package com.example.sumativaPosts.exception;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import jakarta.persistence.EntityNotFoundException;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleEntityNotFoundException_ShouldReturnNotFoundResponse() {
        String errorMessage = "Entity not found";
        EntityNotFoundException exception = new EntityNotFoundException(errorMessage);

        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = 
            exceptionHandler.handleEntityNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertEquals(errorMessage, response.getBody().getMessage());
    }

    @Test
    void handleGlobalException_ShouldReturnInternalServerErrorResponse() {
        Exception exception = new Exception("Unexpected error");

        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = 
            exceptionHandler.handleGlobalException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
        assertEquals("An unexpected error occurred", response.getBody().getMessage());
    }

    @Test
    void handleUnauthorizedAccessException_ShouldReturnForbiddenResponse() {
        String errorMessage = "Unauthorized access";
        UnauthorizedAccessException exception = new UnauthorizedAccessException(errorMessage);

        ResponseEntity<Map<String, String>> response = 
            exceptionHandler.handleUnauthorizedAccessException(exception);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Unauthorized", response.getBody().get("error"));
        assertEquals(errorMessage, response.getBody().get("message"));
    }

    @Test
    void handleAccessDeniedException_ShouldReturnForbiddenResponse() {
        AccessDeniedException exception = new AccessDeniedException("Access denied");

        ResponseEntity<Map<String, String>> response = 
            exceptionHandler.handleAccessDeniedException(exception);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Access Denied", response.getBody().get("error"));
        assertEquals("You do not have permission to perform this action", response.getBody().get("message"));
    }

    @Test
    void errorResponse_ShouldHaveCorrectGettersAndSetters() {
        GlobalExceptionHandler.ErrorResponse errorResponse = 
            new GlobalExceptionHandler.ErrorResponse(404, "Not Found");

        assertEquals(404, errorResponse.getStatus());
        assertEquals("Not Found", errorResponse.getMessage());

        errorResponse.setStatus(500);
        errorResponse.setMessage("Server Error");

        assertEquals(500, errorResponse.getStatus());
        assertEquals("Server Error", errorResponse.getMessage());
    }
} 