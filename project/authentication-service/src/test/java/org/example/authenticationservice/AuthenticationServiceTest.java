package org.example.authenticationservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.example.DTOs.MailConfirmationRequest;
import org.example.authenticationservice.DTOs.RegistrationRequest;
import org.example.authenticationservice.Models.User;
import org.example.authenticationservice.Repositories.UserRepository;
import org.example.authenticationservice.Services.AuthenticationService;
import org.example.authenticationservice.Services.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.BadCredentialsException;
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
    private KafkaTemplate<String, MailConfirmationRequest> kafkaTemplate;

    private final String EMAIL = "test@gmail.com";
    private final String RAW_PASSWORD = "password";
    private final String ENCODED_PASSWORD = "encoded_pass";
    private final String JWT_TOKEN = "token";
    private final String MAIL_CONFIRMATION_TOKEN = "mail-token";
    private final String KAFKA_MAIL_CONFIRMATION_TOPIC = "mail-confirmation";
    private final String EMAIL_IS_TAKEN_EXCEPTION_MESSAGE = "This email is already in use";
    private final boolean NOT_ACTIVATED = false;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUserSuccessfully(){
        RegistrationRequest registrationRequest = new RegistrationRequest(EMAIL, RAW_PASSWORD);
        User user = new User(EMAIL, ENCODED_PASSWORD, NOT_ACTIVATED);
        MailConfirmationRequest mailConfirmationRequest = new MailConfirmationRequest(EMAIL, MAIL_CONFIRMATION_TOKEN);

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(RAW_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtUtil.generateEmailConfirmationToken(EMAIL)).thenReturn(MAIL_CONFIRMATION_TOKEN);

        authenticationService.register(registrationRequest);

        verify(userRepository).findByEmail(EMAIL);
        verify(bCryptPasswordEncoder).encode(RAW_PASSWORD);
        verify(userRepository).save(any(User.class));
        verify(jwtUtil).generateEmailConfirmationToken(EMAIL);
        verify(kafkaTemplate).send(KAFKA_MAIL_CONFIRMATION_TOPIC,
                mailConfirmationRequest);

        verifyNoMoreInteractions(userRepository, jwtUtil, kafkaTemplate, bCryptPasswordEncoder);
    }
    @Test
    void registerUserFailsEmailIsAlreadyInUse(){
        RegistrationRequest registrationRequest = new RegistrationRequest(EMAIL, RAW_PASSWORD);
        User user = new User(EMAIL, ENCODED_PASSWORD, NOT_ACTIVATED);

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        Exception exception = assertThrows(BadCredentialsException.class, () ->
                authenticationService.register(registrationRequest));
        assertEquals(exception.getMessage(), EMAIL_IS_TAKEN_EXCEPTION_MESSAGE);

        verify(userRepository).findByEmail(EMAIL);

        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(kafkaTemplate, jwtUtil);
    }
    @Test
    void activatePassesSuccessfully() {
        User user = new User(EMAIL, ENCODED_PASSWORD, NOT_ACTIVATED);

        when(jwtUtil.verifyEmailConfirmationToken(JWT_TOKEN)).thenReturn(EMAIL);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        authenticationService.activate(JWT_TOKEN);

        assertTrue(user.isActivated());

        verify(jwtUtil).verifyEmailConfirmationToken(JWT_TOKEN);
        verify(userRepository).findByEmail(EMAIL);
        verify(userRepository).save(user);

        verifyNoMoreInteractions(userRepository, jwtUtil, kafkaTemplate, bCryptPasswordEncoder);
    }

    @Test
    void activateFailsUserNotFound() {
        when(jwtUtil.verifyEmailConfirmationToken(JWT_TOKEN)).thenReturn(EMAIL);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () ->
           authenticationService.activate(JWT_TOKEN)
        );
        assertEquals(EMAIL, exception.getMessage());

        verify(jwtUtil).verifyEmailConfirmationToken(JWT_TOKEN);
        verify(userRepository).findByEmail(EMAIL);

        verifyNoMoreInteractions(userRepository);
    }


}
