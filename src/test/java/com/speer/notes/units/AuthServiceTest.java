package com.speer.notes.units;

import com.speer.notes.config.security.JwtUtils;
import com.speer.notes.dto.request.LoginRequest;
import com.speer.notes.dto.request.RegisterRequest;
import com.speer.notes.dto.response.LoginResponse;
import com.speer.notes.dto.response.MessageResponse;
import com.speer.notes.entity.User;
import com.speer.notes.repository.UserRepository;
import com.speer.notes.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginResponse loginResponse;
    private LoginRequest loginRequest;
    private User user;
    private UserDetails userPrincipal;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("test123");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("test123");

        loginResponse = new LoginResponse();
        loginResponse.setEmail("test@example.com");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("test123");

        userPrincipal = org.springframework.security.core.userdetails.User
                .withUsername("testuser")
                .password("test123")
                .authorities(Collections.emptyList())
                .build(); ;
    }

    @Test
    @DisplayName("Should register new user successfully")
    void register_ShouldRegisterNewUser() {
        // Arrange
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        MessageResponse result = authService.registerUser(registerRequest);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when username already exists")
    void register_ShouldThrowExceptionWhenUsernameExists() {
        // Arrange
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // Act & Assert

        assertThrows(IllegalArgumentException.class, () -> authService.registerUser(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void register_ShouldThrowExceptionWhenEmailExists() {
        // Arrange
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authService.registerUser(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should authenticate user successfully")
    void authenticate_ShouldAuthenticateUser() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("token");

        // Act
        LoginResponse result = authService.authenticateUser(loginRequest);

        // Assert
        assertNotNull(result);
        assertEquals("token", result.getToken());
        assertEquals("refreshToken", result.getRefreshToken());
        assertEquals("username", result.getUsername());
        assertEquals("email", result.getEmail());
        assertEquals("tokenType", result.getType());
        assertEquals("roles", result.getRoles());
        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils, times(1)).generateJwtToken(any(Authentication.class));
    }
}
