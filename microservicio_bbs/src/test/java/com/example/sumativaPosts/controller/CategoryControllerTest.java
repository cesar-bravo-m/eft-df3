package com.example.sumativaPosts.controller;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.sumativaPosts.dto.CategoryDto;
import com.example.sumativaPosts.exception.UnauthorizedAccessException;
import com.example.sumativaPosts.security.SecurityUtils;
import com.example.sumativaPosts.service.CategoryService;

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private CategoryController categoryController;

    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Test Category");
        categoryDto.setDescription("Test Description");
    }

    @Test
    void getAllCategories_ShouldReturnListOfCategories() {
        List<CategoryDto> categories = Arrays.asList(categoryDto);
        when(categoryService.getAllCategories()).thenReturn(categories);

        ResponseEntity<List<CategoryDto>> response = categoryController.getAllCategories();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categories, response.getBody());
        verify(categoryService).getAllCategories();
    }

    @Test
    void getCategoryById_ShouldReturnCategory() {
        when(categoryService.getCategoryById(1L)).thenReturn(categoryDto);

        ResponseEntity<CategoryDto> response = categoryController.getCategoryById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categoryDto, response.getBody());
        verify(categoryService).getCategoryById(1L);
    }

    @Test
    void createCategory_WhenModerator_ShouldCreateCategory() {
        when(securityUtils.isModerator()).thenReturn(true);
        when(categoryService.createCategory(categoryDto)).thenReturn(categoryDto);

        ResponseEntity<CategoryDto> response = categoryController.createCategory(categoryDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(categoryDto, response.getBody());
        verify(categoryService).createCategory(categoryDto);
    }

    @Test
    void createCategory_WhenNotModerator_ShouldThrowException() {
        when(securityUtils.isModerator()).thenReturn(false);

        assertThrows(UnauthorizedAccessException.class, () -> {
            categoryController.createCategory(categoryDto);
        });

        verify(categoryService, never()).createCategory(any());
    }

    @Test
    void updateCategory_WhenModerator_ShouldUpdateCategory() {
        when(securityUtils.isModerator()).thenReturn(true);
        when(categoryService.updateCategory(1L, categoryDto)).thenReturn(categoryDto);

        ResponseEntity<CategoryDto> response = categoryController.updateCategory(1L, categoryDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categoryDto, response.getBody());
        verify(categoryService).updateCategory(1L, categoryDto);
    }

    @Test
    void updateCategory_WhenNotModerator_ShouldThrowException() {
        when(securityUtils.isModerator()).thenReturn(false);

        assertThrows(UnauthorizedAccessException.class, () -> {
            categoryController.updateCategory(1L, categoryDto);
        });

        verify(categoryService, never()).updateCategory(any(), any());
    }

    @Test
    void deleteCategory_WhenModerator_ShouldDeleteCategory() {
        when(securityUtils.isModerator()).thenReturn(true);
        doNothing().when(categoryService).deleteCategory(1L);

        ResponseEntity<Void> response = categoryController.deleteCategory(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(categoryService).deleteCategory(1L);
    }

    @Test
    void deleteCategory_WhenNotModerator_ShouldThrowException() {
        when(securityUtils.isModerator()).thenReturn(false);

        assertThrows(UnauthorizedAccessException.class, () -> {
            categoryController.deleteCategory(1L);
        });

        verify(categoryService, never()).deleteCategory(any());
    }
} 