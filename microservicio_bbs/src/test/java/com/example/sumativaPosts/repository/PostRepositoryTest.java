package com.example.sumativaPosts.repository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.example.sumativaPosts.config.TestSecurityConfig;
import com.example.sumativaPosts.model.Category;
import com.example.sumativaPosts.model.Post;
import com.example.sumativaPosts.model.Thread;

@DataJpaTest(
    excludeAutoConfiguration = TestSecurityConfig.class,
    properties = {
        "spring.jpa.hibernate.ddl-auto=none",
        "spring.sql.init.mode=always",
        "spring.jpa.defer-datasource-initialization=true"
    }
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles({"test", "web-test"})
@EntityScan(basePackages = {"com.example.sumativaPosts.model"})
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=none",
    "spring.sql.init.mode=always",
    "spring.jpa.defer-datasource-initialization=true"
})
class PostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PostRepository postRepository;

    private Category category;
    private Thread thread1;
    private Thread thread2;
    private Post post1;
    private Post post2;
    private Post post3;

    @BeforeEach
    void setUp() {
        // Create test category
        category = new Category();
        category.setName("Test Category");
        category.setDescription("Test Description");

        // Save category first
        entityManager.persist(category);
        entityManager.flush();

        // Create test threads
        thread1 = new Thread();
        thread1.setTitle("Test Thread 1");
        thread1.setCreatedAt(LocalDateTime.now());
        thread1.setLastUpdatedAt(LocalDateTime.now());
        thread1.setUserId(1);
        thread1.setCategory(category);

        thread2 = new Thread();
        thread2.setTitle("Test Thread 2");
        thread2.setCreatedAt(LocalDateTime.now());
        thread2.setLastUpdatedAt(LocalDateTime.now());
        thread2.setUserId(2);
        thread2.setCategory(category);

        // Save threads
        entityManager.persist(thread1);
        entityManager.persist(thread2);
        entityManager.flush();

        // Create test posts
        post1 = new Post();
        post1.setContent("Test Post 1");
        post1.setCreatedAt(LocalDateTime.now());
        post1.setUserId(1);
        post1.setThread(thread1);

        post2 = new Post();
        post2.setContent("Test Post 2");
        post2.setCreatedAt(LocalDateTime.now());
        post2.setUserId(1);
        post2.setThread(thread1);

        post3 = new Post();
        post3.setContent("Test Post 3");
        post3.setCreatedAt(LocalDateTime.now());
        post3.setUserId(2);
        post3.setThread(thread2);

        // Save posts
        entityManager.persist(post1);
        entityManager.persist(post2);
        entityManager.persist(post3);
        entityManager.flush();
    }

    @Test
    void findByThreadId_ShouldReturnPostsForThread() {
        List<Post> posts = postRepository.findByThreadId(thread1.getId());

        assertNotNull(posts);
        assertEquals(2, posts.size());
        assertTrue(posts.stream().allMatch(post -> post.getThread().getId().equals(thread1.getId())));
        assertTrue(posts.stream().anyMatch(post -> post.getContent().equals("Test Post 1")));
        assertTrue(posts.stream().anyMatch(post -> post.getContent().equals("Test Post 2")));
    }

    // @Test
    // void findByThreadId_WithNonExistentThread_ShouldReturnEmptyList() {
    //     List<Post> posts = postRepository.findByThreadId(999L);

    //     assertNotNull(posts);
    //     assertTrue(posts.isEmpty());
    // }

    // @Test
    // void findByUserId_ShouldReturnPostsForUser() {
    //     List<Post> posts = postRepository.findByUserId(1);

    //     assertNotNull(posts);
    //     assertEquals(2, posts.size());
    //     assertTrue(posts.stream().allMatch(post -> post.getUserId().equals(1)));
    //     assertTrue(posts.stream().anyMatch(post -> post.getContent().equals("Test Post 1")));
    //     assertTrue(posts.stream().anyMatch(post -> post.getContent().equals("Test Post 2")));
    // }

    // @Test
    // void findByUserId_WithNonExistentUser_ShouldReturnEmptyList() {
    //     List<Post> posts = postRepository.findByUserId(999);

    //     assertNotNull(posts);
    //     // assertTrue(posts.isEmpty());
    // }

    // @Test
    // void save_ShouldCreateNewPost() {
    //     Post newPost = new Post();
    //     newPost.setContent("New Test Post");
    //     newPost.setCreatedAt(LocalDateTime.now());
    //     newPost.setUserId(3);
    //     newPost.setThread(thread1);

    //     Post savedPost = postRepository.save(newPost);

    //     assertNotNull(savedPost);
    //     assertNotNull(savedPost.getId());
    //     assertEquals("New Test Post", savedPost.getContent());
    //     assertEquals(3, savedPost.getUserId());
    //     assertEquals(thread1.getId(), savedPost.getThread().getId());
    // }

    // @Test
    // void findById_ShouldReturnPost() {
    //     Post foundPost = postRepository.findById(post1.getId()).orElse(null);

    //     assertNotNull(foundPost);
    //     assertEquals(post1.getId(), foundPost.getId());
    //     assertEquals(post1.getContent(), foundPost.getContent());
    //     assertEquals(post1.getUserId(), foundPost.getUserId());
    //     assertEquals(post1.getThread().getId(), foundPost.getThread().getId());
    // }

    // @Test
    // void findAll_ShouldReturnAllPosts() {
    //     List<Post> posts = postRepository.findAll();

    //     assertNotNull(posts);
    //     assertEquals(3, posts.size());
    // }

    // @Test
    // void delete_ShouldRemovePost() {
    //     postRepository.delete(post1);
    //     entityManager.flush();

    //     Post deletedPost = postRepository.findById(post1.getId()).orElse(null);
    //     assertNull(deletedPost);
    // }
} 