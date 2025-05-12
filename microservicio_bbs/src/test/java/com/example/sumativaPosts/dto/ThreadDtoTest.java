package com.example.sumativaPosts.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class ThreadDtoTest {

    @Test
    void testNoArgsConstructor() {
        ThreadDto thread = new ThreadDto();
        assertNotNull(thread);
        assertNull(thread.getId());
        assertNull(thread.getTitle());
        assertNull(thread.getCreatedAt());
        assertNull(thread.getLastUpdatedAt());
        assertNull(thread.getUserId());
        assertNull(thread.getCategoryId());
        assertNull(thread.getCategoryName());
        assertNull(thread.getFirstPostContent());
        assertNull(thread.getPosts());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        String title = "Test Thread";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime lastUpdatedAt = LocalDateTime.now();
        Integer userId = 123;
        Long categoryId = 456L;
        String categoryName = "Test Category";
        String firstPostContent = "First post content";
        
        ThreadDto thread = new ThreadDto(id, title, createdAt, lastUpdatedAt, userId, categoryId, categoryName, firstPostContent);
        
        assertEquals(id, thread.getId());
        assertEquals(title, thread.getTitle());
        assertEquals(createdAt, thread.getCreatedAt());
        assertEquals(lastUpdatedAt, thread.getLastUpdatedAt());
        assertEquals(userId, thread.getUserId());
        assertEquals(categoryId, thread.getCategoryId());
        assertEquals(categoryName, thread.getCategoryName());
        assertEquals(firstPostContent, thread.getFirstPostContent());
    }

    @Test
    void testSettersAndGetters() {
        ThreadDto thread = new ThreadDto();
        
        Long id = 1L;
        String title = "Test Thread";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime lastUpdatedAt = LocalDateTime.now();
        Integer userId = 123;
        Long categoryId = 456L;
        String categoryName = "Test Category";
        String firstPostContent = "First post content";
        List<PostDto> posts = new ArrayList<>();
        
        thread.setId(id);
        thread.setTitle(title);
        thread.setCreatedAt(createdAt);
        thread.setLastUpdatedAt(lastUpdatedAt);
        thread.setUserId(userId);
        thread.setCategoryId(categoryId);
        thread.setCategoryName(categoryName);
        thread.setFirstPostContent(firstPostContent);
        thread.setPosts(posts);
        
        assertEquals(id, thread.getId());
        assertEquals(title, thread.getTitle());
        assertEquals(createdAt, thread.getCreatedAt());
        assertEquals(lastUpdatedAt, thread.getLastUpdatedAt());
        assertEquals(userId, thread.getUserId());
        assertEquals(categoryId, thread.getCategoryId());
        assertEquals(categoryName, thread.getCategoryName());
        assertEquals(firstPostContent, thread.getFirstPostContent());
        assertEquals(posts, thread.getPosts());
    }
} 