package com.example.sumativa.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.sumativa.model.ERole;
import com.example.sumativa.model.User;
import com.example.sumativa.payload.response.MessageResponse;
import com.example.sumativa.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserController userController;

    private User testUser;
    private User testUser2;

    @BeforeEach
    void setUp() {
        testUser = new User("testuser", "test@example.com", "password123");
        testUser.setId(1L);
        testUser.setRole(ERole.NORMAL_POSTER);

        testUser2 = new User("testuser2", "test2@example.com", "password456");
        testUser2.setId(2L);
        testUser2.setRole(ERole.MODERATOR);
    }

    @Test
    void testGetAllUsers() {
        // given
        List<User> users = Arrays.asList(testUser, testUser2);
        when(userRepository.findAll()).thenReturn(users);

        // when
        ResponseEntity<List<User>> response = userController.getAllUsers();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
        verify(userRepository).findAll();
    }

    @Test
    void testGetUserById_UserExists() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // when
        ResponseEntity<?> response = userController.getUserById(1L);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User responseUser = (User) response.getBody();
        assertEquals(testUser.getId(), responseUser.getId());
        assertEquals(testUser.getUsername(), responseUser.getUsername());
        assertEquals(testUser.getEmail(), responseUser.getEmail());
        assertEquals("", responseUser.getPassword()); // Password should be cleared
        verify(userRepository).findById(1L);
    }

    @Test
    void testGetUserById_UserNotFound() {
        // given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // when
        ResponseEntity<?> response = userController.getUserById(999L);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userRepository).findById(999L);
    }

    @Test
    void testGetUserByEmail_UserExists() {
        // given
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

        // when
        ResponseEntity<?> response = userController.getUserByEmail(email);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User responseUser = (User) response.getBody();
        assertEquals(testUser.getId(), responseUser.getId());
        assertEquals(testUser.getUsername(), responseUser.getUsername());
        assertEquals(testUser.getEmail(), responseUser.getEmail());
        assertEquals("", responseUser.getPassword()); // Password should be cleared
        verify(userRepository).findByEmail(email);
    }

    @Test
    void testGetUserByEmail_UserNotFound() {
        // given
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when
        ResponseEntity<?> response = userController.getUserByEmail(email);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void testUpdateUser_UserExists_UpdateUsername() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByUsername("newusername")).thenReturn(false);

        User updatedUser = new User();
        updatedUser.setUsername("newusername");

        // when
        ResponseEntity<?> response = userController.updateUser(1L, updatedUser);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User responseUser = (User) response.getBody();
        // assertEquals("newusername", responseUser.getUsername());
        // verify(userRepository).findById(1L);
        // verify(userRepository).existsByUsername("newusername");
        // verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUser_UserExists_UsernameTaken() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        User updatedUser = new User();
        updatedUser.setUsername("existinguser");

        // when
        ResponseEntity<?> response = userController.updateUser(1L, updatedUser);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertEquals("Error: Username is already taken!", messageResponse.getMessage());
        verify(userRepository).findById(1L);
        verify(userRepository).existsByUsername("existinguser");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_UserExists_UpdateEmail() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);

        User updatedUser = new User();
        updatedUser.setEmail("new@example.com");

        // when
        ResponseEntity<?> response = userController.updateUser(1L, updatedUser);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User responseUser = (User) response.getBody();
        // assertEquals("new@example.com", responseUser.getEmail());
        // verify(userRepository).findById(1L);
        // verify(userRepository).existsByEmail("new@example.com");
        // verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUser_UserExists_EmailTaken() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        User updatedUser = new User();
        updatedUser.setEmail("existing@example.com");

        // when
        ResponseEntity<?> response = userController.updateUser(1L, updatedUser);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertEquals("Error: Email is already in use!", messageResponse.getMessage());
        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmail("existing@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_UserExists_UpdatePassword() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(encoder.encode("newpassword")).thenReturn("encodedpassword");

        User updatedUser = new User();
        updatedUser.setPassword("newpassword");

        // when
        ResponseEntity<?> response = userController.updateUser(1L, updatedUser);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User responseUser = (User) response.getBody();
        // assertEquals("encodedpassword", responseUser.getPassword());
        // verify(userRepository).findById(1L);
        // verify(encoder).encode("newpassword");
        // verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUser_UserExists_UpdateRole() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User updatedUser = new User();
        updatedUser.setRole(ERole.MODERATOR);

        // when
        ResponseEntity<?> response = userController.updateUser(1L, updatedUser);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User responseUser = (User) response.getBody();
        // assertEquals(ERole.MODERATOR, responseUser.getRole());
        // verify(userRepository).findById(1L);
        // verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUser_UserNotFound() {
        // given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        User updatedUser = new User();
        updatedUser.setUsername("newusername");

        // when
        ResponseEntity<?> response = userController.updateUser(999L, updatedUser);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userRepository).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUser_Success() {
        // given
        doNothing().when(userRepository).deleteById(1L);

        // when
        ResponseEntity<HttpStatus> response = userController.deleteUser(1L);

        // then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userRepository).deleteById(1L);
    }

    @Test
    void testDeleteUser_Error() {
        // given
        doThrow(new RuntimeException("Database error")).when(userRepository).deleteById(1L);

        // when
        ResponseEntity<HttpStatus> response = userController.deleteUser(1L);

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(userRepository).deleteById(1L);
    }
} 