package com.example.sumativaPosts.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.sumativaPosts.dto.PostDto;
import com.example.sumativaPosts.model.Category;
import com.example.sumativaPosts.model.Post;
import com.example.sumativaPosts.model.Thread;
import com.example.sumativaPosts.repository.PostRepository;
import com.example.sumativaPosts.repository.ThreadRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private ThreadRepository threadRepository;

    @InjectMocks
    private PostServiceImpl postService;

    private Post post;
    private Thread thread;
    private Category category;
    private PostDto postDto;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        
        category = new Category();
        category.setId(1L);
        category.setName("Test Category");
        category.setDescription("Test Description");
        category.setThreads(new HashSet<>());
        
        thread = new Thread();
        thread.setId(1L);
        thread.setTitle("Test Thread");
        thread.setCreatedAt(now);
        thread.setLastUpdatedAt(now);
        thread.setUserId(1);
        thread.setCategory(category);
        thread.setPosts(new HashSet<>());
        
        post = new Post();
        post.setId(1L);
        post.setContent("Test Content");
        post.setCreatedAt(now);
        post.setLastUpdatedAt(now);
        post.setUserId(1);
        post.setThread(thread);
        
        postDto = new PostDto();
        postDto.setId(1L);
        postDto.setContent("Test Content");
        postDto.setCreatedAt(now);
        postDto.setLastUpdatedAt(now);
        postDto.setUserId(1);
        postDto.setThreadId(1L);
        postDto.setThreadTitle("Test Thread");
    }

    @Test
    void getAllPosts_ShouldReturnListOfPosts() {
        when(postRepository.findAll()).thenReturn(Arrays.asList(post));

        List<PostDto> result = postService.getAllPosts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(postDto.getId(), result.get(0).getId());
        assertEquals(postDto.getContent(), result.get(0).getContent());
        verify(postRepository).findAll();
    }

    @Test
    void getPostsByThreadId_ShouldReturnListOfPosts() {
        when(postRepository.findByThreadId(1L)).thenReturn(Arrays.asList(post));

        List<PostDto> result = postService.getPostsByThreadId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(postDto.getId(), result.get(0).getId());
        assertEquals(postDto.getContent(), result.get(0).getContent());
        verify(postRepository).findByThreadId(1L);
    }

    @Test
    void getPostsByUserId_ShouldReturnListOfPosts() {
        when(postRepository.findByUserId(1)).thenReturn(Arrays.asList(post));

        List<PostDto> result = postService.getPostsByUserId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(postDto.getId(), result.get(0).getId());
        assertEquals(postDto.getContent(), result.get(0).getContent());
        verify(postRepository).findByUserId(1);
    }

    @Test
    void getPostById_WhenPostExists_ShouldReturnPost() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        PostDto result = postService.getPostById(1L);

        assertNotNull(result);
        assertEquals(postDto.getId(), result.getId());
        assertEquals(postDto.getContent(), result.getContent());
        verify(postRepository).findById(1L);
    }

    @Test
    void getPostById_WhenPostDoesNotExist_ShouldThrowException() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            postService.getPostById(1L);
        });

        verify(postRepository).findById(1L);
    }

    @Test
    void createPost_WhenThreadExists_ShouldCreatePost() {
        when(threadRepository.findById(1L)).thenReturn(Optional.of(thread));
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(threadRepository.save(any(Thread.class))).thenReturn(thread);

        PostDto result = postService.createPost(postDto);

        assertNotNull(result);
        assertEquals(postDto.getId(), result.getId());
        assertEquals(postDto.getContent(), result.getContent());
        verify(threadRepository).findById(1L);
        verify(postRepository).save(any(Post.class));
        verify(threadRepository).save(any(Thread.class));
    }

    @Test
    void createPost_WhenThreadDoesNotExist_ShouldThrowException() {
        when(threadRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            postService.createPost(postDto);
        });

        verify(threadRepository).findById(1L);
    }

    @Test
    void updatePost_WhenPostExists_ShouldUpdatePost() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(threadRepository.save(any(Thread.class))).thenReturn(thread);

        PostDto result = postService.updatePost(1L, postDto);

        assertNotNull(result);
        assertEquals(postDto.getId(), result.getId());
        assertEquals(postDto.getContent(), result.getContent());
        verify(postRepository).findById(1L);
        verify(postRepository).save(any(Post.class));
        verify(threadRepository).save(any(Thread.class));
    }

    @Test
    void updatePost_WhenPostDoesNotExist_ShouldThrowException() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            postService.updatePost(1L, postDto);
        });

        verify(postRepository).findById(1L);
    }

    @Test
    void updatePost_WhenThreadChanges_ShouldUpdateThread() {
        Thread newThread = new Thread();
        newThread.setId(2L);
        newThread.setTitle("New Thread");
        
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(threadRepository.findById(2L)).thenReturn(Optional.of(newThread));
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(threadRepository.save(any(Thread.class))).thenReturn(newThread);
        
        postDto.setThreadId(2L);
        
        PostDto result = postService.updatePost(1L, postDto);
        
        assertNotNull(result);
        assertEquals(postDto.getId(), result.getId());
        assertEquals(postDto.getContent(), result.getContent());
        verify(postRepository).findById(1L);
        verify(threadRepository).findById(2L);
        verify(postRepository).save(any(Post.class));
        verify(threadRepository).save(any(Thread.class));
    }

    @Test
    void deletePost_WhenPostExists_ShouldDeletePost() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(threadRepository.save(any(Thread.class))).thenReturn(thread);

        postService.deletePost(1L);

        verify(postRepository).findById(1L);
        verify(postRepository).deleteById(1L);
        verify(threadRepository).save(any(Thread.class));
    }

    @Test
    void deletePost_WhenPostDoesNotExist_ShouldThrowException() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            postService.deletePost(1L);
        });

        verify(postRepository).findById(1L);
    }

    @Test
    void searchPosts_ShouldReturnMatchingPosts() {
        when(postRepository.findAll()).thenReturn(Arrays.asList(post));

        List<PostDto> result = postService.searchPosts("Test");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(postDto.getId(), result.get(0).getId());
        assertEquals(postDto.getContent(), result.get(0).getContent());
        verify(postRepository).findAll();
    }

    @Test
    void searchPosts_WhenNoMatches_ShouldReturnEmptyList() {
        when(postRepository.findAll()).thenReturn(Arrays.asList(post));

        List<PostDto> result = postService.searchPosts("Nonexistent");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(postRepository).findAll();
    }
} 