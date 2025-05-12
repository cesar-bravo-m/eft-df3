package com.example.sumativa.security.jwt;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.sumativa.model.ERole;
import com.example.sumativa.model.User;
import com.example.sumativa.security.services.UserDetailsImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

@ExtendWith(MockitoExtension.class)
class AuthTokenFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private User testUser;
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        testUser = new User("testuser", "test@example.com", "password123");
        testUser.setId(1L);
        testUser.setRole(ERole.MODERATOR);
        userDetails = UserDetailsImpl.build(testUser);
        SecurityContextHolder.clearContext();
    }

    // @Test
    // void testDoFilterInternal_ValidToken_UserFound() throws ServletException, IOException {
    //     // given
    //     String token = "valid.jwt.token";
    //     request.addHeader("Authorization", "Bearer " + token);
    //     when(jwtUtils.validateJwtToken(token)).thenReturn(true);
    //     when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("testuser");
    //     when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);

    //     // when
    //     authTokenFilter.doFilterInternal(request, response, filterChain);

    //     // then
    //     verify(filterChain).doFilter(request, response);
    //     UsernamePasswordAuthenticationToken authentication = 
    //         (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    //     // assertEquals(userDetails, authentication.getPrincipal().getUsername());
    //     // assertEquals(userDetails.getAuthorities(), authentication.getAuthorities());
    // }

    // @Test
    // void testDoFilterInternal_ValidToken_UserNotFound() throws ServletException, IOException {
    //     // given
    //     String token = "valid.jwt.token";
    //     request.addHeader("Authorization", "Bearer " + token);
    //     when(jwtUtils.validateJwtToken(token)).thenReturn(true);
    //     when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("testuser");
    //     when(jwtUtils.getRolesFromJwtToken(token)).thenReturn(Arrays.asList("ROLE_MODERATOR"));
    //     when(jwtUtils.getUserIdFromJwtToken(token)).thenReturn(1L);
    //     doThrow(new RuntimeException("User not found")).when(userDetailsService).loadUserByUsername(anyString());

    //     // when
    //     authTokenFilter.doFilterInternal(request, response, filterChain);

    //     // then
    //     verify(filterChain).doFilter(request, response);
    //     UsernamePasswordAuthenticationToken authentication = 
    //         (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    //     assertEquals("testuser", authentication.getPrincipal());
    //     assertEquals(1, authentication.getAuthorities().size());
    //     assertEquals(new SimpleGrantedAuthority("ROLE_MODERATOR"), 
    //         authentication.getAuthorities().iterator().next());
    // }

    @Test
    void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
        // given
        String token = "invalid.jwt.token";
        request.addHeader("Authorization", "Bearer " + token);
        when(jwtUtils.validateJwtToken(token)).thenReturn(false);

        // when
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternal_NoToken() throws ServletException, IOException {
        // given
        // No Authorization header

        // when
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternal_InvalidHeaderFormat() throws ServletException, IOException {
        // given
        request.addHeader("Authorization", "InvalidFormat token");

        // when
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternal_ExceptionHandling() throws ServletException, IOException {
        // given
        String token = "valid.jwt.token";
        request.addHeader("Authorization", "Bearer " + token);
        when(jwtUtils.validateJwtToken(token)).thenThrow(new RuntimeException("Test exception"));

        // when
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
} 