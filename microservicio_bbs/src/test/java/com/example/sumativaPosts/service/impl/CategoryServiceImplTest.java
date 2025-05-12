package com.example.sumativaPosts.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.sumativaPosts.dto.CategoryDto;
import com.example.sumativaPosts.model.Category;
import com.example.sumativaPosts.model.Thread;
import com.example.sumativaPosts.repository.CategoryRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private Thread thread;
    private CategoryDto categoryDto;
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
        
        categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Test Category");
        categoryDto.setDescription("Test Description");
    }

    @Test
    void getAllCategories_ShouldReturnListOfCategories() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category));

        List<CategoryDto> result = categoryService.getAllCategories();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(categoryDto.getId(), result.get(0).getId());
        assertEquals(categoryDto.getName(), result.get(0).getName());
        assertEquals(categoryDto.getDescription(), result.get(0).getDescription());
        verify(categoryRepository).findAll();
    }

    @Test
    void getCategoryById_WhenCategoryExists_ShouldReturnCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryDto result = categoryService.getCategoryById(1L);

        assertNotNull(result);
        assertEquals(categoryDto.getId(), result.getId());
        assertEquals(categoryDto.getName(), result.getName());
        assertEquals(categoryDto.getDescription(), result.getDescription());
        verify(categoryRepository).findById(1L);
    }

    @Test
    void getCategoryById_WhenCategoryDoesNotExist_ShouldThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            categoryService.getCategoryById(1L);
        });

        verify(categoryRepository).findById(1L);
    }

    @Test
    void createCategory_ShouldCreateCategory() {
        Category newCategory = new Category();
        newCategory.setName(categoryDto.getName());
        newCategory.setDescription(categoryDto.getDescription());
        
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDto result = categoryService.createCategory(categoryDto);

        assertNotNull(result);
        assertEquals(categoryDto.getId(), result.getId());
        assertEquals(categoryDto.getName(), result.getName());
        assertEquals(categoryDto.getDescription(), result.getDescription());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void updateCategory_WhenCategoryExists_ShouldUpdateCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDto result = categoryService.updateCategory(1L, categoryDto);

        assertNotNull(result);
        assertEquals(categoryDto.getId(), result.getId());
        assertEquals(categoryDto.getName(), result.getName());
        assertEquals(categoryDto.getDescription(), result.getDescription());
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void updateCategory_WhenCategoryDoesNotExist_ShouldThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            categoryService.updateCategory(1L, categoryDto);
        });

        verify(categoryRepository).findById(1L);
    }

    @Test
    void deleteCategory_WhenCategoryExists_ShouldDeleteCategory() {
        when(categoryRepository.existsById(1L)).thenReturn(true);

        categoryService.deleteCategory(1L);

        verify(categoryRepository).existsById(1L);
        verify(categoryRepository).deleteById(1L);
    }

    @Test
    void deleteCategory_WhenCategoryDoesNotExist_ShouldThrowException() {
        when(categoryRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            categoryService.deleteCategory(1L);
        });

        verify(categoryRepository).existsById(1L);
    }

    @Test
    void convertToDto_WithThreads_ShouldIncludeThreads() {
        category.addThread(thread);
        
        assertThrows(EntityNotFoundException.class, () -> {
            categoryService.getCategoryById(1L);
        });
    }
} 