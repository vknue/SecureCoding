package hr.algebra.glowlog.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import hr.algebra.glowlog.dto.Dto;
import hr.algebra.glowlog.entity.User;
import hr.algebra.glowlog.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import hr.algebra.glowlog.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private UserDetailsService userDetailsService;

    @InjectMocks private AuthService authService;

    @Test
    void testLoginWithValidCredentials() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(userDetailsService.loadUserByUsername("testuser"))
                .thenReturn(new org.springframework.security.core.userdetails.User("testuser", "encodedPassword", java.util.Collections.emptyList()));
        when(jwtService.generateAccessToken(any())).thenReturn("fake-jwt-token");

        Dto.TokenResponse response = authService.login(new Dto.LoginRequest("testuser", "password123"), jwtService, userDetailsService);

        assertNotNull(response);
        assertEquals("fake-jwt-token", response.accessToken());
    }

    @Test
    void testLoginWithInvalidPassword() {
        User user = new User();
        user.setPassword("encodedPassword");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encodedPassword")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () ->
                authService.login(new Dto.LoginRequest("testuser", "wrong"), jwtService, userDetailsService));
    }
}