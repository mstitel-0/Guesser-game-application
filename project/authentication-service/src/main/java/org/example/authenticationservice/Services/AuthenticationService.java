package org.example.authenticationservice.Services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.DTOs.MailConfirmationRequest;
import org.example.authenticationservice.DTOs.LoginRequest;
import org.example.DTOs.RegistrationRequest;
import org.example.authenticationservice.Exceptions.UserNotActivatedException;
import org.example.authenticationservice.Models.User;
import org.example.authenticationservice.Repositories.UserRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;
    private final KafkaTemplate<String, MailConfirmationRequest> kafkaTemplate;

    private static final String REFRESH_TOKEN_COOKIE_HEADER = "refreshToken";
    private static final String MAIL_SERVICE_KAFKA_TOPIC = "mail-confirmation";
    private static final int COOKIE_AGE = 43200;

    public AuthenticationService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtUtil jwtUtil, KafkaTemplate<String, MailConfirmationRequest> kafkaTemplate) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtil = jwtUtil;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void register(RegistrationRequest registrationRequest) {
        userRepository.findByEmail(registrationRequest.email())
                .ifPresentOrElse(
                        user -> handleRegisteringExistingUser(user),
                        () -> handleRegisteringNewUser(registrationRequest)
                );
    }
    public void handleRegisteringExistingUser(User user) {
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User already exists. Confirm your email");
        }
        throw new BadCredentialsException("User already exists. Log in");
    }

    public void handleRegisteringNewUser(RegistrationRequest registrationRequest) {
        User user = new User(
                registrationRequest.email(),
                bCryptPasswordEncoder.encode(registrationRequest.password()),
                registrationRequest.telegramId(),
                false
        );
        userRepository.save(user);

        sendConfirmationEmail(registrationRequest.email());
    }
    public void sendConfirmationEmail(String email){
        MailConfirmationRequest request = new MailConfirmationRequest(email,
                jwtUtil.generateEmailConfirmationToken(email));

        kafkaTemplate.send(MAIL_SERVICE_KAFKA_TOPIC, request);
    }

    @Transactional
    public void activate(String token) {
        String email = jwtUtil.verifyEmailConfirmationToken(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        User activatedUser = new User(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getTelegramId(),
                true
        );

        userRepository.save(activatedUser);
    }

    public String login(LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with given email: " + loginRequest.email()));
        if (!user.isActivated()) {
            throw new UserNotActivatedException("Confirm your email first!");
        }
        if (!bCryptPasswordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }
        String accessToken = jwtUtil.generateAccessToken(loginRequest.email(), user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(loginRequest.email(), user.getId());


        Cookie refreshCookie = new Cookie(REFRESH_TOKEN_COOKIE_HEADER, refreshToken);
        refreshCookie.setPath("/api/authentication");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setMaxAge(COOKIE_AGE);

        httpServletResponse.addCookie(refreshCookie);

        return accessToken;
    }

    public String refresh(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null) {
            throw new BadCredentialsException("Invalid cookies");
        }
        String refreshToken = Arrays
                .stream(cookies)
                .filter(cookie -> cookie.getName().equals(REFRESH_TOKEN_COOKIE_HEADER))
                .findFirst().orElseThrow(() -> new BadCredentialsException("Refresh token is missing."))
                .getValue();

        return jwtUtil.refreshAccessToken(refreshToken);
    }
}
