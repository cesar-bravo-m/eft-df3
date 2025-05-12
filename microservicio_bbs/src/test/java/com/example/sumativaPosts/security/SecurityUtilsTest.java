package com.example.sumativaPosts.security;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

class SecurityUtilsTest {

    private SecurityUtils securityUtils;
    private static final String ROLE_MODERATOR = "ROLE_MODERATOR";
    private static final String ROLE_NORMAL_USER = "ROLE_NORMAL_USER";
    private static final Integer USER_ID = 1;

    @BeforeEach
    void setUp() {
        securityUtils = new SecurityUtils();
        SecurityContextHolder.clearContext();
    }

    @Test
    void isAuthenticated_WhenNotAuthenticated_ShouldReturnFalse() {
        assertFalse(securityUtils.isAuthenticated());
    }

    @Test
    void isAuthenticated_WhenAnonymousUser_ShouldReturnFalse() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn("anonymousUser");
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertFalse(securityUtils.isAuthenticated());
    }

    @Test
    void isAuthenticated_WhenAuthenticated_ShouldReturnTrue() {
        JwtUserDetails userDetails = new JwtUserDetails("testuser", USER_ID, 
            Collections.singletonList(new SimpleGrantedAuthority(ROLE_NORMAL_USER)));
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertTrue(securityUtils.isAuthenticated());
    }

    @Test
    void isModerator_WhenNotAuthenticated_ShouldReturnFalse() {
        assertFalse(securityUtils.isModerator());
    }

    @Test
    void isModerator_WhenNormalUser_ShouldReturnFalse() {
        JwtUserDetails userDetails = new JwtUserDetails("testuser", USER_ID, 
            Collections.singletonList(new SimpleGrantedAuthority(ROLE_NORMAL_USER)));
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertFalse(securityUtils.isModerator());
    }

    @Test
    void isModerator_WhenModerator_ShouldReturnTrue() {
        JwtUserDetails userDetails = new JwtUserDetails("moderator", USER_ID, 
            Collections.singletonList(new SimpleGrantedAuthority(ROLE_MODERATOR)));
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertTrue(securityUtils.isModerator());
    }

    @Test
    void isCurrentUserIdOrModerator_WhenNotAuthenticated_ShouldReturnFalse() {
        assertFalse(securityUtils.isCurrentUserIdOrModerator(USER_ID));
    }

    @Test
    void isCurrentUserIdOrModerator_WhenModerator_ShouldReturnTrue() {
        JwtUserDetails userDetails = new JwtUserDetails("moderator", USER_ID, 
            Collections.singletonList(new SimpleGrantedAuthority(ROLE_MODERATOR)));
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertTrue(securityUtils.isCurrentUserIdOrModerator(2)); // Different user ID
    }

    @Test
    void isCurrentUserIdOrModerator_WhenSameUserId_ShouldReturnTrue() {
        JwtUserDetails userDetails = new JwtUserDetails("testuser", USER_ID, 
            Collections.singletonList(new SimpleGrantedAuthority(ROLE_NORMAL_USER)));
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertTrue(securityUtils.isCurrentUserIdOrModerator(USER_ID));
    }

    @Test
    void isCurrentUserIdOrModerator_WhenDifferentUserId_ShouldReturnFalse() {
        JwtUserDetails userDetails = new JwtUserDetails("testuser", USER_ID, 
            Collections.singletonList(new SimpleGrantedAuthority(ROLE_NORMAL_USER)));
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertFalse(securityUtils.isCurrentUserIdOrModerator(2));
    }

    @Test
    void getCurrentUserId_WhenNotAuthenticated_ShouldReturnNull() {
        assertNull(securityUtils.getCurrentUserId());
    }

    @Test
    void getCurrentUserId_WhenAuthenticated_ShouldReturnUserId() {
        JwtUserDetails userDetails = new JwtUserDetails("testuser", USER_ID, 
            Collections.singletonList(new SimpleGrantedAuthority(ROLE_NORMAL_USER)));
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertEquals(USER_ID, securityUtils.getCurrentUserId());
    }
} 