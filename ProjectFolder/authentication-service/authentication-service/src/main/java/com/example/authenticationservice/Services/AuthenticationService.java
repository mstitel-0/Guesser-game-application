package com.example.authenticationservice.Services;

import com.example.authenticationservice.DTOs.LoginRequest;
import com.example.authenticationservice.DTOs.RegistrationRequest;
import com.example.authenticationservice.JwtUtil;
import com.example.authenticationservice.Models.User;
import com.example.authenticationservice.Repositories.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    public AuthenticationService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtUtil jwtUtil, EmailService emailService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    @Transactional
    public void register(RegistrationRequest registrationRequest) {
        User user = new User();
        user.setEmail(registrationRequest.email());
        user.setPassword(bCryptPasswordEncoder.encode(registrationRequest.password()));
        user.setActivated(false);
        userRepository.save(user);
        emailService.sendEmail(registrationRequest.email(),
                jwtUtil.generateEmailConfirmationToken(registrationRequest.email()));
    }

    @Transactional
    public void activate(String token) {
        String email = jwtUtil.verifyEmailConfirmationToken(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        user.setActivated(true);
        userRepository.save(user);
    }

    public String login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new UsernameNotFoundException(loginRequest.email()));
        if (!bCryptPasswordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }
        return jwtUtil.generateToken(loginRequest.email());
    }

}
