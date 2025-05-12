package com.example.sumativaPosts.dto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class CategoryDtoTest {

    @Test
    void testNoArgsConstructor() {
        CategoryDto category = new CategoryDto();
        assertNotNull(category);
        assertNull(category.getId());
        assertNull(category.getName());
        assertNull(category.getDescription());
        assertNull(category.getThreads());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        String name = "Test Category";
        String description = "Test Description";
        
        CategoryDto category = new CategoryDto(id, name, description);
        
        assertEquals(id, category.getId());
        assertEquals(name, category.getName());
        assertEquals(description, category.getDescription());
    }

    @Test
    void testSettersAndGetters() {
        CategoryDto category = new CategoryDto();
        
        Long id = 1L;
        String name = "Test Category";
        String description = "Test Description";
        List<ThreadSummaryDto> threads = new ArrayList<>();
        
        category.setId(id);
        category.setName(name);
        category.setDescription(description);
        category.setThreads(threads);
        
        assertEquals(id, category.getId());
        assertEquals(name, category.getName());
        assertEquals(description, category.getDescription());
        assertEquals(threads, category.getThreads());
    }
} 