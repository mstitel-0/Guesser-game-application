package com.example.authenticationservice.Services;

import com.example.authenticationservice.DTOs.LoginRequest;
import com.example.authenticationservice.DTOs.MailConfirmationRequest;
import com.example.authenticationservice.DTOs.RegistrationRequest;
import com.example.authenticationservice.JwtUtil;
import com.example.authenticationservice.Models.User;
import com.example.authenticationservice.Repositories.UserRepository;
import org.springframework.kafka.core.KafkaTemplate;
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
    private final KafkaTemplate<String, MailConfirmationRequest> kafkaTemplate;

    public AuthenticationService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtUtil jwtUtil, KafkaTemplate<String, MailConfirmationRequest> kafkaTemplate) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtil = jwtUtil;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void register(RegistrationRequest registrationRequest) {
        User user = new User();
        user.setEmail(registrationRequest.email());
        user.setPassword(bCryptPasswordEncoder.encode(registrationRequest.password()));
        user.setActivated(false);
        userRepository.save(user);
        MailConfirmationRequest request = new MailConfirmationRequest(registrationRequest.email(),
                jwtUtil.generateToken(registrationRequest.email()));

        kafkaTemplate.send("mail-confirmation", request);
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
        //todo: user's not activated case handling
        return jwtUtil.generateToken(loginRequest.email());
    }

}
