package com.example.sumativa.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class ERoleTest {

    @Test
    void testEnumValues() {
        // Test that all enum values exist
        ERole[] roles = ERole.values();
        assertEquals(2, roles.length);
        
        // Test specific enum values
        assertEquals(ERole.NORMAL_POSTER, ERole.valueOf("NORMAL_POSTER"));
        assertEquals(ERole.MODERATOR, ERole.valueOf("MODERATOR"));
        
        // Test ordinal values
        assertEquals(0, ERole.NORMAL_POSTER.ordinal());
        assertEquals(1, ERole.MODERATOR.ordinal());
    }
} 