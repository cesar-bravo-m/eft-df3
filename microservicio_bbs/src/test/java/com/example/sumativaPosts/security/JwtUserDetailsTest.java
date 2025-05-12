package com.example.sumativaPosts.security;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class JwtUserDetailsTest {

    private JwtUserDetails userDetails;
    private static final String USERNAME = "testuser";
    private static final Integer USER_ID = 1;
    private static final Collection<SimpleGrantedAuthority> AUTHORITIES = 
        Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));

    @BeforeEach
    void setUp() {
        userDetails = new JwtUserDetails(USERNAME, USER_ID, AUTHORITIES);
    }

    @Test
    void constructor_ShouldSetFieldsCorrectly() {
        assertEquals(USERNAME, userDetails.getUsername());
        assertEquals(USER_ID, userDetails.getUserId());
        assertEquals(AUTHORITIES, userDetails.getAuthorities());
    }

    @Test
    void getAuthorities_ShouldReturnAuthorities() {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertEquals(AUTHORITIES, authorities);
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void getPassword_ShouldReturnNull() {
        assertNull(userDetails.getPassword());
    }

    @Test
    void getUsername_ShouldReturnUsername() {
        assertEquals(USERNAME, userDetails.getUsername());
    }

    @Test
    void getUserId_ShouldReturnUserId() {
        assertEquals(USER_ID, userDetails.getUserId());
    }

    @Test
    void isAccountNonExpired_ShouldReturnTrue() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void isAccountNonLocked_ShouldReturnTrue() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpired_ShouldReturnTrue() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void isEnabled_ShouldReturnTrue() {
        assertTrue(userDetails.isEnabled());
    }
} 