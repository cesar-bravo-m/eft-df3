package com.example.sumativaPosts.dto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class ThreadSummaryDtoTest {

    @Test
    void testNoArgsConstructor() {
        ThreadSummaryDto summary = new ThreadSummaryDto();
        assertNotNull(summary);
        assertNull(summary.getId());
        assertNull(summary.getTitle());
        assertNull(summary.getCreatedAt());
        assertNull(summary.getLastUpdatedAt());
        assertNull(summary.getUserId());
        assertNull(summary.getCategoryId());
        assertEquals(0, summary.getPostCount());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        String title = "Test Thread";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime lastUpdatedAt = LocalDateTime.now();
        Integer userId = 123;
        Long categoryId = 456L;
        int postCount = 5;
        
        ThreadSummaryDto summary = new ThreadSummaryDto(id, title, createdAt, lastUpdatedAt, userId, categoryId, postCount);
        
        assertEquals(id, summary.getId());
        assertEquals(title, summary.getTitle());
        assertEquals(createdAt, summary.getCreatedAt());
        assertEquals(lastUpdatedAt, summary.getLastUpdatedAt());
        assertEquals(userId, summary.getUserId());
        assertEquals(categoryId, summary.getCategoryId());
        assertEquals(postCount, summary.getPostCount());
    }

    @Test
    void testSettersAndGetters() {
        ThreadSummaryDto summary = new ThreadSummaryDto();
        
        Long id = 1L;
        String title = "Test Thread";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime lastUpdatedAt = LocalDateTime.now();
        Integer userId = 123;
        Long categoryId = 456L;
        int postCount = 5;
        
        summary.setId(id);
        summary.setTitle(title);
        summary.setCreatedAt(createdAt);
        summary.setLastUpdatedAt(lastUpdatedAt);
        summary.setUserId(userId);
        summary.setCategoryId(categoryId);
        summary.setPostCount(postCount);
        
        assertEquals(id, summary.getId());
        assertEquals(title, summary.getTitle());
        assertEquals(createdAt, summary.getCreatedAt());
        assertEquals(lastUpdatedAt, summary.getLastUpdatedAt());
        assertEquals(userId, summary.getUserId());
        assertEquals(categoryId, summary.getCategoryId());
        assertEquals(postCount, summary.getPostCount());
    }
} 