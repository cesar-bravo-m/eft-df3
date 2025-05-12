package com.example.sumativa.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("testuser", "test@example.com", "password123");
    }

    @Test
    void testDefaultConstructor() {
        User emptyUser = new User();
        assertNotNull(emptyUser);
        assertNull(emptyUser.getUsername());
        assertNull(emptyUser.getEmail());
        assertNull(emptyUser.getPassword());
        assertNull(emptyUser.getRole());
        assertFalse(emptyUser.isAdmin());
    }

    @Test
    void testParameterizedConstructor() {
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals(ERole.NORMAL_POSTER, user.getRole());
        assertFalse(user.isAdmin());
    }

    @Test
    void testGettersAndSetters() {
        // Test ID
        user.setId(1L);
        assertEquals(1L, user.getId());

        // Test Username
        user.setUsername("newuser");
        assertEquals("newuser", user.getUsername());

        // Test Email
        user.setEmail("new@example.com");
        assertEquals("new@example.com", user.getEmail());

        // Test Password
        user.setPassword("newpassword");
        assertEquals("newpassword", user.getPassword());

        // Test Role
        user.setRole(ERole.MODERATOR);
        assertEquals(ERole.MODERATOR, user.getRole());
        assertTrue(user.isAdmin());
        assertTrue(user.isModerator());

        // Test setting back to NORMAL_POSTER
        user.setRole(ERole.NORMAL_POSTER);
        assertEquals(ERole.NORMAL_POSTER, user.getRole());
        assertFalse(user.isAdmin());
        assertFalse(user.isModerator());
    }

    @Test
    void testAdminBackwardCompatibility() {
        // Test setting admin through setAdmin
        user.setAdmin(true);
        assertTrue(user.isAdmin());
        assertEquals(ERole.MODERATOR, user.getRole());

        // Test setting admin to false
        user.setAdmin(false);
        assertFalse(user.isAdmin());
        assertEquals(ERole.NORMAL_POSTER, user.getRole());
    }

    @Test
    void testRoleAndAdminSynchronization() {
        // Test that setting role updates isAdmin
        user.setRole(ERole.MODERATOR);
        assertTrue(user.isAdmin());

        user.setRole(ERole.NORMAL_POSTER);
        assertFalse(user.isAdmin());

        // Test that setting isAdmin updates role
        user.setAdmin(true);
        assertEquals(ERole.MODERATOR, user.getRole());

        user.setAdmin(false);
        assertEquals(ERole.NORMAL_POSTER, user.getRole());
    }
} 