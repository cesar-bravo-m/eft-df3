package com.example.sumativa.payload.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class MessageResponseTest {

    @Test
    void testConstructorAndGetter() {
        // given
        String message = "Test message";

        // when
        MessageResponse response = new MessageResponse(message);

        // then
        assertEquals(message, response.getMessage());
    }

    @Test
    void testSetter() {
        // given
        MessageResponse response = new MessageResponse("Initial message");

        // when
        String newMessage = "Updated message";
        response.setMessage(newMessage);

        // then
        assertEquals(newMessage, response.getMessage());
    }

    @Test
    void testEmptyMessage() {
        // given
        MessageResponse response = new MessageResponse("");

        // when
        response.setMessage("");

        // then
        assertEquals("", response.getMessage());
    }

    @Test
    void testNullMessage() {
        // given
        MessageResponse response = new MessageResponse(null);

        // when
        response.setMessage(null);

        // then
        assertNull(response.getMessage());
    }
} 