package com.example.sumativaPosts.controller;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.sumativaPosts.dto.ThreadDto;
import com.example.sumativaPosts.dto.ThreadSummaryDto;
import com.example.sumativaPosts.exception.UnauthorizedAccessException;
import com.example.sumativaPosts.security.SecurityUtils;
import com.example.sumativaPosts.service.PostService;
import com.example.sumativaPosts.service.ThreadService;

@ExtendWith(MockitoExtension.class)
class ThreadControllerTest {

    @Mock
    private ThreadService threadService;

    @Mock
    private PostService postService;

    @Mock
    private SecurityUtils securityUtils;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ThreadController threadController;

    private ThreadDto threadDto;
    private ThreadSummaryDto threadSummaryDto;

    @BeforeEach
    void setUp() {
        threadDto = new ThreadDto();
        threadDto.setId(1L);
        threadDto.setTitle("Test Thread");
        threadDto.setUserId(1);
        threadDto.setCategoryId(1L);

        threadSummaryDto = new ThreadSummaryDto();
        threadSummaryDto.setId(1L);
        threadSummaryDto.setTitle("Test Thread");
        threadSummaryDto.setUserId(1);
        threadSummaryDto.setCategoryId(1L);
    }

    @Test
    void getAllThreads_ShouldReturnListOfThreads() {
        List<ThreadSummaryDto> expectedThreads = Arrays.asList(threadSummaryDto);
        when(threadService.getAllThreads()).thenReturn(expectedThreads);

        ResponseEntity<List<ThreadSummaryDto>> response = threadController.getAllThreads();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedThreads, response.getBody());
        verify(threadService).getAllThreads();
    }

    @Test
    void getThreadsByCategoryId_ShouldReturnListOfThreads() {
        List<ThreadSummaryDto> expectedThreads = Arrays.asList(threadSummaryDto);
        when(threadService.getThreadsByCategoryId(anyLong())).thenReturn(expectedThreads);

        ResponseEntity<List<ThreadSummaryDto>> response = threadController.getThreadsByCategoryId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedThreads, response.getBody());
        verify(threadService).getThreadsByCategoryId(1L);
    }

    @Test
    void getThreadById_ShouldReturnThread() {
        when(threadService.getThreadById(anyLong())).thenReturn(threadDto);

        ResponseEntity<ThreadDto> response = threadController.getThreadById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(threadDto, response.getBody());
        verify(threadService).getThreadById(1L);
    }

    @Test
    void createThread_WhenAuthenticated_ShouldCreateThread() {
        when(securityUtils.getCurrentUserId()).thenReturn(1);
        when(threadService.createThread(any(ThreadDto.class))).thenReturn(threadDto);

        ResponseEntity<ThreadDto> response = threadController.createThread(threadDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(threadDto, response.getBody());
        assertEquals(1, threadDto.getUserId());
        verify(threadService).createThread(threadDto);
    }

    @Test
    void createThread_WhenNotAuthenticated_ShouldThrowException() {
        when(securityUtils.getCurrentUserId()).thenReturn(null);

        assertThrows(UnauthorizedAccessException.class, () -> {
            threadController.createThread(threadDto);
        });

        verify(threadService, never()).createThread(any(ThreadDto.class));
    }

    @Test
    void updateThread_WhenAuthorized_ShouldUpdateThread() {
        when(securityUtils.isCurrentUserIdOrModerator(any())).thenReturn(true);
        when(threadService.getThreadById(anyLong())).thenReturn(threadDto);
        when(threadService.updateThread(anyLong(), any(ThreadDto.class))).thenReturn(threadDto);

        ResponseEntity<ThreadDto> response = threadController.updateThread(1L, threadDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(threadDto, response.getBody());
        verify(threadService).updateThread(1L, threadDto);
    }

    @Test
    void updateThread_WhenNotAuthorized_ShouldThrowException() {
        when(securityUtils.isCurrentUserIdOrModerator(any())).thenReturn(false);
        when(threadService.getThreadById(anyLong())).thenReturn(threadDto);

        assertThrows(UnauthorizedAccessException.class, () -> {
            threadController.updateThread(1L, threadDto);
        });

        verify(threadService, never()).updateThread(anyLong(), any(ThreadDto.class));
    }

    @Test
    void deleteThread_WhenAuthorized_ShouldDeleteThread() {
        when(securityUtils.isCurrentUserIdOrModerator(any())).thenReturn(true);
        when(threadService.getThreadById(anyLong())).thenReturn(threadDto);

        ResponseEntity<Void> response = threadController.deleteThread(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(threadService).deleteThread(1L);
    }

    @Test
    void deleteThread_WhenNotAuthorized_ShouldThrowException() {
        when(securityUtils.isCurrentUserIdOrModerator(any())).thenReturn(false);
        when(threadService.getThreadById(anyLong())).thenReturn(threadDto);

        assertThrows(UnauthorizedAccessException.class, () -> {
            threadController.deleteThread(1L);
        });

        verify(threadService, never()).deleteThread(anyLong());
    }

    @Test
    void searchThreads_ShouldReturnMatchingThreads() {
        List<ThreadSummaryDto> expectedThreads = Arrays.asList(threadSummaryDto);
        when(threadService.searchThreads(anyString())).thenReturn(expectedThreads);

        ResponseEntity<List<ThreadSummaryDto>> response = threadController.searchThreads("test");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedThreads, response.getBody());
        verify(threadService).searchThreads("test");
    }
} 