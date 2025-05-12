package com.example.sumativaPosts.repository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.example.sumativaPosts.config.TestSecurityConfig;
import com.example.sumativaPosts.model.Category;
import com.example.sumativaPosts.model.Thread;

@DataJpaTest(
    excludeAutoConfiguration = TestSecurityConfig.class,
    properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.sql.init.mode=always",
    }
)
@ActiveProfiles({"test", "web-test"})
@EntityScan(basePackages = {"com.example.sumativaPosts.model"})
class ThreadRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ThreadRepository threadRepository;

    private Category category1;
    private Category category2;
    private Thread thread1;
    private Thread thread2;
    private Thread thread3;

    @BeforeEach
    void setUp() {
        // Create test categories
        category1 = new Category();
        category1.setName("Test Category 1");
        category1.setDescription("Test Description 1");

        category2 = new Category();
        category2.setName("Test Category 2");
        category2.setDescription("Test Description 2");

        // Save categories first
        entityManager.persist(category1);
        entityManager.persist(category2);
        entityManager.flush();

        // Create test threads
        thread1 = new Thread();
        thread1.setTitle("Test Thread 1");
        thread1.setCreatedAt(LocalDateTime.now());
        thread1.setLastUpdatedAt(LocalDateTime.now());
        thread1.setUserId(1);
        thread1.setCategory(category1);

        thread2 = new Thread();
        thread2.setTitle("Test Thread 2");
        thread2.setCreatedAt(LocalDateTime.now());
        thread2.setLastUpdatedAt(LocalDateTime.now());
        thread2.setUserId(1);
        thread2.setCategory(category1);

        thread3 = new Thread();
        thread3.setTitle("Test Thread 3");
        thread3.setCreatedAt(LocalDateTime.now());
        thread3.setLastUpdatedAt(LocalDateTime.now());
        thread3.setUserId(2);
        thread3.setCategory(category2);

        // Save threads to database
        entityManager.persist(thread1);
        entityManager.persist(thread2);
        entityManager.persist(thread3);
        entityManager.flush();
    }

    @Test
    void findByCategoryId_ShouldReturnThreadsForCategory() {
        List<Thread> threads = threadRepository.findByCategoryId(category1.getId());

        assertNotNull(threads);
        assertEquals(2, threads.size());
        assertTrue(threads.stream().allMatch(thread -> thread.getCategory().getId().equals(category1.getId())));
        assertTrue(threads.stream().anyMatch(thread -> thread.getTitle().equals("Test Thread 1")));
        assertTrue(threads.stream().anyMatch(thread -> thread.getTitle().equals("Test Thread 2")));
    }

    @Test
    void findByCategoryId_WithNonExistentCategory_ShouldReturnEmptyList() {
        List<Thread> threads = threadRepository.findByCategoryId(999L);

        assertNotNull(threads);
        assertTrue(threads.isEmpty());
    }

    @Test
    void save_ShouldCreateNewThread() {
        Thread newThread = new Thread();
        newThread.setTitle("New Test Thread");
        newThread.setCreatedAt(LocalDateTime.now());
        newThread.setLastUpdatedAt(LocalDateTime.now());
        newThread.setUserId(3);
        newThread.setCategory(category1);

        Thread savedThread = threadRepository.save(newThread);

        assertNotNull(savedThread);
        assertNotNull(savedThread.getId());
        assertEquals("New Test Thread", savedThread.getTitle());
        assertEquals(3, savedThread.getUserId());
        assertEquals(category1.getId(), savedThread.getCategory().getId());
    }

    @Test
    void findById_ShouldReturnThread() {
        Thread foundThread = threadRepository.findById(thread1.getId()).orElse(null);

        assertNotNull(foundThread);
        assertEquals(thread1.getId(), foundThread.getId());
        assertEquals(thread1.getTitle(), foundThread.getTitle());
        assertEquals(thread1.getUserId(), foundThread.getUserId());
        assertEquals(thread1.getCategory().getId(), foundThread.getCategory().getId());
    }

    @Test
    void findAll_ShouldReturnAllThreads() {
        List<Thread> threads = threadRepository.findAll();

        assertNotNull(threads);
        assertEquals(3, threads.size());
    }

    @Test
    void delete_ShouldRemoveThread() {
        threadRepository.delete(thread1);
        entityManager.flush();

        Thread deletedThread = threadRepository.findById(thread1.getId()).orElse(null);
        assertNull(deletedThread);
    }

    @Test
    void existsById_ShouldReturnTrueForExistingThread() {
        boolean exists = threadRepository.existsById(thread1.getId());
        assertTrue(exists);
    }

    @Test
    void count_ShouldReturnTotalNumberOfThreads() {
        long count = threadRepository.count();
        assertEquals(3, count);
    }
} 