package org.example.authenticationservice.Services;

import org.example.DTOs.MailConfirmationRequest;
import org.example.authenticationservice.DTOs.JwtResponse;
import org.example.authenticationservice.DTOs.LoginRequest;
import org.example.authenticationservice.DTOs.RegistrationRequest;
import org.example.authenticationservice.Exceptions.UserNotActivatedException;
import org.example.authenticationservice.Models.User;
import org.example.authenticationservice.Repositories.UserRepository;
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
        if (userRepository.findByEmail(registrationRequest.email()).isPresent()) {
            throw new BadCredentialsException("This email is already in use");
        }
        User user = new User();
        user.setEmail(registrationRequest.email());
        user.setPassword(bCryptPasswordEncoder.encode(registrationRequest.password()));
        user.setActivated(false);
        userRepository.save(user);
        MailConfirmationRequest request = new MailConfirmationRequest(registrationRequest.email(),
                jwtUtil.generateEmailConfirmationToken(registrationRequest.email()));

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

    public JwtResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new UsernameNotFoundException(loginRequest.email()));
        if (!user.isActivated()) {
            throw new UserNotActivatedException("Confirm your email first!");
        }
        if (!bCryptPasswordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }
        return new JwtResponse(jwtUtil.generateAccessToken(loginRequest.email()),
                jwtUtil.generateRefreshToken(loginRequest.email()));
    }

    public String refresh(String refreshToken) {
        return jwtUtil.refreshAccessToken(refreshToken);
    }

}
