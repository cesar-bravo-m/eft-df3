package com.example.sumativaPosts.dto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class PostDtoTest {

    @Test
    void testNoArgsConstructor() {
        PostDto post = new PostDto();
        assertNotNull(post);
        assertNull(post.getId());
        assertNull(post.getContent());
        assertNull(post.getCreatedAt());
        assertNull(post.getLastUpdatedAt());
        assertNull(post.getUserId());
        assertNull(post.getThreadId());
        assertNull(post.getThreadTitle());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        String content = "Test Content";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime lastUpdatedAt = LocalDateTime.now();
        Integer userId = 123;
        Long threadId = 456L;
        String threadTitle = "Test Thread";
        
        PostDto post = new PostDto(id, content, createdAt, lastUpdatedAt, userId, threadId, threadTitle);
        
        assertEquals(id, post.getId());
        assertEquals(content, post.getContent());
        assertEquals(createdAt, post.getCreatedAt());
        assertEquals(lastUpdatedAt, post.getLastUpdatedAt());
        assertEquals(userId, post.getUserId());
        assertEquals(threadId, post.getThreadId());
        assertEquals(threadTitle, post.getThreadTitle());
    }

    @Test
    void testSettersAndGetters() {
        PostDto post = new PostDto();
        
        Long id = 1L;
        String content = "Test Content";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime lastUpdatedAt = LocalDateTime.now();
        Integer userId = 123;
        Long threadId = 456L;
        String threadTitle = "Test Thread";
        
        post.setId(id);
        post.setContent(content);
        post.setCreatedAt(createdAt);
        post.setLastUpdatedAt(lastUpdatedAt);
        post.setUserId(userId);
        post.setThreadId(threadId);
        post.setThreadTitle(threadTitle);
        
        assertEquals(id, post.getId());
        assertEquals(content, post.getContent());
        assertEquals(createdAt, post.getCreatedAt());
        assertEquals(lastUpdatedAt, post.getLastUpdatedAt());
        assertEquals(userId, post.getUserId());
        assertEquals(threadId, post.getThreadId());
        assertEquals(threadTitle, post.getThreadTitle());
    }
} 