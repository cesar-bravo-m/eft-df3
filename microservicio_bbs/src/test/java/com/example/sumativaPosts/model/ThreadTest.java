package com.example.sumativaPosts.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ThreadTest {

    private Thread thread;
    private Category category;
    private Post post;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        thread = new Thread();
        category = new Category();
        post = new Post();
        now = LocalDateTime.now();
    }

    @Test
    void testNoArgsConstructor() {
        assertNotNull(thread);
        assertNull(thread.getId());
        assertNull(thread.getTitle());
        assertNotNull(thread.getCreatedAt());
        assertNotNull(thread.getLastUpdatedAt());
        assertNull(thread.getUserId());
        assertNull(thread.getCategory());
        assertNotNull(thread.getPosts());
        assertTrue(thread.getPosts().isEmpty());
    }

    @Test
    void testAllArgsConstructor() {
        String title = "Test Thread";
        Integer userId = 1;
        
        Thread thread = new Thread(title, userId, category);
        
        assertNull(thread.getId());
        assertEquals(title, thread.getTitle());
        assertNotNull(thread.getCreatedAt());
        assertNotNull(thread.getLastUpdatedAt());
        assertEquals(userId, thread.getUserId());
        assertEquals(category, thread.getCategory());
        assertNotNull(thread.getPosts());
        assertTrue(thread.getPosts().isEmpty());
    }

    @Test
    void testSettersAndGetters() {
        Long id = 1L;
        String title = "Test Thread";
        Integer userId = 1;
        Set<Post> posts = new HashSet<>();
        
        thread.setId(id);
        thread.setTitle(title);
        thread.setCreatedAt(now);
        thread.setLastUpdatedAt(now);
        thread.setUserId(userId);
        thread.setCategory(category);
        thread.setPosts(posts);
        
        assertEquals(id, thread.getId());
        assertEquals(title, thread.getTitle());
        assertEquals(now, thread.getCreatedAt());
        assertEquals(now, thread.getLastUpdatedAt());
        assertEquals(userId, thread.getUserId());
        assertEquals(category, thread.getCategory());
        assertEquals(posts, thread.getPosts());
    }

    @Test
    void testAddPost() {
        thread.addPost(post);
        
        assertTrue(thread.getPosts().contains(post));
        assertEquals(thread, post.getThread());
    }

    @Test
    void testRemovePost() {
        thread.addPost(post);
        thread.removePost(post);
        
        assertTrue(thread.getPosts().isEmpty());
        assertNull(post.getThread());
    }

    @Test
    void testPreUpdate() {
        LocalDateTime initialTime = LocalDateTime.now();
        thread.setLastUpdatedAt(initialTime);
        
        // Wait a bit to ensure the timestamps will be different
        try {
            java.lang.Thread.sleep(100);
        } catch (InterruptedException e) {
            java.lang.Thread.currentThread().interrupt();
        }
        
        thread.preUpdate();
        
        assertNotNull(thread.getLastUpdatedAt());
        assertTrue(thread.getLastUpdatedAt().isAfter(initialTime));
    }
} 