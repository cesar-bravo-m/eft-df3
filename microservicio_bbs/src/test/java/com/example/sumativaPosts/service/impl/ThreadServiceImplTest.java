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

import com.example.sumativaPosts.dto.ThreadDto;
import com.example.sumativaPosts.dto.ThreadSummaryDto;
import com.example.sumativaPosts.model.Category;
import com.example.sumativaPosts.model.Post;
import com.example.sumativaPosts.model.Thread;
import com.example.sumativaPosts.repository.CategoryRepository;
import com.example.sumativaPosts.repository.ThreadRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class ThreadServiceImplTest {

    @Mock
    private ThreadRepository threadRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ThreadServiceImpl threadService;

    private Thread thread;
    private Category category;
    private Post post;
    private ThreadDto threadDto;
    private ThreadSummaryDto threadSummaryDto;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        
        category = new Category();
        category.setId(1L);
        category.setName("Test Category");
        
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
        
        threadDto = new ThreadDto();
        threadDto.setId(1L);
        threadDto.setTitle("Test Thread");
        threadDto.setCreatedAt(now);
        threadDto.setLastUpdatedAt(now);
        threadDto.setUserId(1);
        threadDto.setCategoryId(1L);
        threadDto.setCategoryName("Test Category");
        
        threadSummaryDto = new ThreadSummaryDto();
        threadSummaryDto.setId(1L);
        threadSummaryDto.setTitle("Test Thread");
        threadSummaryDto.setCreatedAt(now);
        threadSummaryDto.setLastUpdatedAt(now);
        threadSummaryDto.setUserId(1);
        threadSummaryDto.setCategoryId(1L);
        threadSummaryDto.setPostCount(0);
    }

    @Test
    void getAllThreads_ShouldReturnListOfThreads() {
        when(threadRepository.findAll()).thenReturn(Arrays.asList(thread));

        List<ThreadSummaryDto> result = threadService.getAllThreads();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(threadSummaryDto.getId(), result.get(0).getId());
        assertEquals(threadSummaryDto.getTitle(), result.get(0).getTitle());
        verify(threadRepository).findAll();
    }

    @Test
    void getThreadsByCategoryId_ShouldReturnListOfThreads() {
        when(threadRepository.findByCategoryId(1L)).thenReturn(Arrays.asList(thread));

        List<ThreadSummaryDto> result = threadService.getThreadsByCategoryId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(threadSummaryDto.getId(), result.get(0).getId());
        assertEquals(threadSummaryDto.getTitle(), result.get(0).getTitle());
        verify(threadRepository).findByCategoryId(1L);
    }

    @Test
    void getThreadById_WhenThreadExists_ShouldReturnThread() {
        when(threadRepository.findById(1L)).thenReturn(Optional.of(thread));

        ThreadDto result = threadService.getThreadById(1L);

        assertNotNull(result);
        assertEquals(threadDto.getId(), result.getId());
        assertEquals(threadDto.getTitle(), result.getTitle());
        verify(threadRepository).findById(1L);
    }

    @Test
    void getThreadById_WhenThreadDoesNotExist_ShouldThrowException() {
        when(threadRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            threadService.getThreadById(1L);
        });

        verify(threadRepository).findById(1L);
    }

    @Test
    void createThread_WhenCategoryExists_ShouldCreateThread() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(threadRepository.save(any(Thread.class))).thenReturn(thread);

        ThreadDto result = threadService.createThread(threadDto);

        assertNotNull(result);
        assertEquals(threadDto.getId(), result.getId());
        assertEquals(threadDto.getTitle(), result.getTitle());
        verify(categoryRepository).findById(1L);
        verify(threadRepository).save(any(Thread.class));
    }

    @Test
    void createThread_WhenCategoryDoesNotExist_ShouldThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            threadService.createThread(threadDto);
        });

        verify(categoryRepository).findById(1L);
    }

    @Test
    void updateThread_WhenThreadExists_ShouldUpdateThread() {
        when(threadRepository.findById(1L)).thenReturn(Optional.of(thread));
        when(threadRepository.save(any(Thread.class))).thenReturn(thread);

        ThreadDto result = threadService.updateThread(1L, threadDto);

        assertNotNull(result);
        assertEquals(threadDto.getId(), result.getId());
        assertEquals(threadDto.getTitle(), result.getTitle());
        verify(threadRepository).findById(1L);
        verify(threadRepository).save(any(Thread.class));
    }

    @Test
    void updateThread_WhenThreadDoesNotExist_ShouldThrowException() {
        when(threadRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            threadService.updateThread(1L, threadDto);
        });

        verify(threadRepository).findById(1L);
    }

    @Test
    void updateThread_WhenCategoryChanges_ShouldUpdateCategory() {
        Category newCategory = new Category();
        newCategory.setId(2L);
        newCategory.setName("New Category");
        
        when(threadRepository.findById(1L)).thenReturn(Optional.of(thread));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(newCategory));
        when(threadRepository.save(any(Thread.class))).thenReturn(thread);
        
        threadDto.setCategoryId(2L);
        
        ThreadDto result = threadService.updateThread(1L, threadDto);
        
        assertNotNull(result);
        assertEquals(threadDto.getId(), result.getId());
        assertEquals(threadDto.getTitle(), result.getTitle());
        verify(threadRepository).findById(1L);
        verify(categoryRepository).findById(2L);
        verify(threadRepository).save(any(Thread.class));
    }

    @Test
    void deleteThread_WhenThreadExists_ShouldDeleteThread() {
        when(threadRepository.existsById(1L)).thenReturn(true);

        threadService.deleteThread(1L);

        verify(threadRepository).existsById(1L);
        verify(threadRepository).deleteById(1L);
    }

    @Test
    void deleteThread_WhenThreadDoesNotExist_ShouldThrowException() {
        when(threadRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            threadService.deleteThread(1L);
        });

        verify(threadRepository).existsById(1L);
    }

    @Test
    void searchThreads_ShouldReturnMatchingThreads() {
        when(threadRepository.findAll()).thenReturn(Arrays.asList(thread));

        List<ThreadSummaryDto> result = threadService.searchThreads("Test");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(threadSummaryDto.getId(), result.get(0).getId());
        assertEquals(threadSummaryDto.getTitle(), result.get(0).getTitle());
        verify(threadRepository).findAll();
    }

    @Test
    void searchThreads_WhenNoMatches_ShouldReturnEmptyList() {
        when(threadRepository.findAll()).thenReturn(Arrays.asList(thread));

        List<ThreadSummaryDto> result = threadService.searchThreads("Nonexistent");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(threadRepository).findAll();
    }
} 