package com.example.authenticationservice;

import com.example.authenticationservice.DTOs.RegistrationRequest;
import com.example.authenticationservice.Models.User;
import com.example.authenticationservice.Repositories.UserRepository;
import com.example.authenticationservice.Services.AuthenticationService;
import com.example.authenticationservice.Services.EmailService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

public class AuthenticationServiceTest {
    @InjectMocks
    private AuthenticationService authenticationService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private EmailService emailService;

    private final String EMAIL = "test@gmail.com";
    private final String RAW_PASSWORD = "password";
    private final String ENCODED_PASSWORD = "encoded_pass";
    private final String JWT_TOKEN = "token";
    private final boolean NOT_ACTIVATED = false;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUserSuccessfully(){
        RegistrationRequest registrationRequest = new RegistrationRequest(EMAIL, RAW_PASSWORD);
        User user = new User(EMAIL, ENCODED_PASSWORD, NOT_ACTIVATED);

        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any())).thenReturn(user);
        when(jwtUtil.generateEmailConfirmationToken(anyString())).thenReturn(JWT_TOKEN);

        authenticationService.register(registrationRequest);

        verify(userRepository).save(any(User.class));
        verify(jwtUtil).generateEmailConfirmationToken(anyString());
        verify(emailService).sendEmail(eq(EMAIL), eq(JWT_TOKEN));
    }
    @Test
    void activatePassesSuccessfully() {
        User user = new User(EMAIL, ENCODED_PASSWORD, NOT_ACTIVATED);

        when(jwtUtil.verifyEmailConfirmationToken(JWT_TOKEN)).thenReturn(EMAIL);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        authenticationService.activate(JWT_TOKEN);

        verify(jwtUtil).verifyEmailConfirmationToken(anyString());
        verify(userRepository).findByEmail(anyString());
        verify(userRepository).save(any(User.class));
        assertTrue(user.isActivated());
    }

    @Test
    void activateFailsUserNotFound() {
        when(jwtUtil.verifyEmailConfirmationToken(JWT_TOKEN)).thenReturn(EMAIL);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () ->
           authenticationService.activate(JWT_TOKEN)
        );

        assertEquals(EMAIL, exception.getMessage());

        verify(jwtUtil).verifyEmailConfirmationToken(anyString());
        verify(userRepository).findByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }


}
