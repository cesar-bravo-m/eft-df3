package com.example.sumativa.security.services;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.sumativa.model.ERole;
import com.example.sumativa.model.User;

class UserDetailsImplTest {

    @Test
    void testConstructorAndGetters() {
        // given
        Long id = 1L;
        String username = "testuser";
        String email = "test@example.com";
        String password = "password123";
        ERole role = ERole.NORMAL_POSTER;
        Collection<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_NORMAL_POSTER")
        );

        // when
        UserDetailsImpl userDetails = new UserDetailsImpl(id, username, email, password, role, authorities);

        // then
        assertEquals(id, userDetails.getId());
        assertEquals(username, userDetails.getUsername());
        assertEquals(email, userDetails.getEmail());
        assertEquals(password, userDetails.getPassword());
        assertEquals(role, userDetails.getRole());
        assertEquals(authorities, userDetails.getAuthorities());
    }

    @Test
    void testBuildFromUser_NormalPoster() {
        // given
        User user = new User("testuser", "test@example.com", "password123");
        user.setId(1L);
        user.setRole(ERole.NORMAL_POSTER);

        // when
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        // then
        assertEquals(user.getId(), userDetails.getId());
        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getEmail(), userDetails.getEmail());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertEquals(user.getRole(), userDetails.getRole());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_NORMAL_POSTER")));
        assertFalse(userDetails.isModerator());
    }

    @Test
    void testBuildFromUser_Moderator() {
        // given
        User user = new User("moderator", "mod@example.com", "password123");
        user.setId(2L);
        user.setRole(ERole.MODERATOR);

        // when
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        // then
        assertEquals(user.getId(), userDetails.getId());
        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getEmail(), userDetails.getEmail());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertEquals(user.getRole(), userDetails.getRole());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MODERATOR")));
        assertTrue(userDetails.isModerator());
    }

    @Test
    void testAccountStatusMethods() {
        // given
        UserDetailsImpl userDetails = UserDetailsImpl.build(
            new User("testuser", "test@example.com", "password123")
        );

        // then
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void testEqualsAndHashCode() {
        // given
        User user1 = new User("user1", "user1@example.com", "password123");
        user1.setId(1L);
        User user2 = new User("user2", "user2@example.com", "password123");
        user2.setId(1L);
        User user3 = new User("user3", "user3@example.com", "password123");
        user3.setId(2L);

        UserDetailsImpl userDetails1 = UserDetailsImpl.build(user1);
        UserDetailsImpl userDetails2 = UserDetailsImpl.build(user2);
        UserDetailsImpl userDetails3 = UserDetailsImpl.build(user3);

        // then
        assertTrue(userDetails1.equals(userDetails1)); // same instance
        assertTrue(userDetails1.equals(userDetails2)); // same id
        assertFalse(userDetails1.equals(userDetails3)); // different id
        assertFalse(userDetails1.equals(null)); // null
        assertFalse(userDetails1.equals(new Object())); // different class
    }
} 