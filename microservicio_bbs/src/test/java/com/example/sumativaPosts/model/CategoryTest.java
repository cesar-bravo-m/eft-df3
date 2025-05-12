package com.example.sumativaPosts.model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryTest {

    private Category category;
    private Thread thread1;
    private Thread thread2;

    @BeforeEach
    void setUp() {
        category = new Category();
        thread1 = new Thread();
        thread2 = new Thread();
    }

    @Test
    void testNoArgsConstructor() {
        assertNotNull(category);
        assertNull(category.getId());
        assertNull(category.getName());
        assertNull(category.getDescription());
        assertNotNull(category.getThreads());
        assertTrue(category.getThreads().isEmpty());
    }

    @Test
    void testAllArgsConstructor() {
        String name = "Test Category";
        String description = "Test Description";
        
        Category category = new Category(name, description);
        
        assertNull(category.getId());
        assertEquals(name, category.getName());
        assertEquals(description, category.getDescription());
        assertNotNull(category.getThreads());
        assertTrue(category.getThreads().isEmpty());
    }

    @Test
    void testSettersAndGetters() {
        Long id = 1L;
        String name = "Test Category";
        String description = "Test Description";
        Set<Thread> threads = new HashSet<>();
        
        category.setId(id);
        category.setName(name);
        category.setDescription(description);
        category.setThreads(threads);
        
        assertEquals(id, category.getId());
        assertEquals(name, category.getName());
        assertEquals(description, category.getDescription());
        assertEquals(threads, category.getThreads());
    }

    @Test
    void testAddThread() {
        category.addThread(thread1);
        
        assertTrue(category.getThreads().contains(thread1));
        assertEquals(category, thread1.getCategory());
    }

    @Test
    void testRemoveThread() {
        category.addThread(thread1);
        category.removeThread(thread1);
        
        assertTrue(category.getThreads().isEmpty());
        assertNull(thread1.getCategory());
    }

    @Test
    void testMultipleThreads() {
        category.addThread(thread1);
        category.addThread(thread2);
        
        assertEquals(2, category.getThreads().size());
        assertTrue(category.getThreads().contains(thread1));
        assertTrue(category.getThreads().contains(thread2));
        assertEquals(category, thread1.getCategory());
        assertEquals(category, thread2.getCategory());
    }

    @Test
    void testRemoveNonExistentThread() {
        category.removeThread(thread1);
        
        assertTrue(category.getThreads().isEmpty());
    }
} 