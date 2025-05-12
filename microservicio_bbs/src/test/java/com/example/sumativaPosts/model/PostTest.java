package com.example.sumativaPosts.model;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PostTest {

    private Post post;
    private Thread thread;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        post = new Post();
        thread = new Thread();
        now = LocalDateTime.now();
    }

    @Test
    void testNoArgsConstructor() {
        assertNotNull(post);
        assertNull(post.getId());
        assertNull(post.getContent());
        assertNotNull(post.getCreatedAt());
        assertNotNull(post.getLastUpdatedAt());
        assertNull(post.getUserId());
        assertNull(post.getThread());
    }

    @Test
    void testAllArgsConstructor() {
        String content = "Test Content";
        Integer userId = 1;
        
        Post post = new Post(content, userId, thread);
        
        assertNull(post.getId());
        assertEquals(content, post.getContent());
        assertNotNull(post.getCreatedAt());
        assertNotNull(post.getLastUpdatedAt());
        assertEquals(userId, post.getUserId());
        assertEquals(thread, post.getThread());
    }

    @Test
    void testSettersAndGetters() {
        Long id = 1L;
        String content = "Test Content";
        Integer userId = 1;
        
        post.setId(id);
        post.setContent(content);
        post.setCreatedAt(now);
        post.setLastUpdatedAt(now);
        post.setUserId(userId);
        post.setThread(thread);
        
        assertEquals(id, post.getId());
        assertEquals(content, post.getContent());
        assertEquals(now, post.getCreatedAt());
        assertEquals(now, post.getLastUpdatedAt());
        assertEquals(userId, post.getUserId());
        assertEquals(thread, post.getThread());
    }

    @Test
    void testPreUpdate() {
        LocalDateTime initialTime = LocalDateTime.now();
        post.setLastUpdatedAt(initialTime);
        
        // Wait a bit to ensure the timestamps will be different
        try {
            java.lang.Thread.sleep(100);
        } catch (InterruptedException e) {
            java.lang.Thread.currentThread().interrupt();
        }
        
        post.preUpdate();
        
        assertNotNull(post.getLastUpdatedAt());
        assertTrue(post.getLastUpdatedAt().isAfter(initialTime));
    }
} 