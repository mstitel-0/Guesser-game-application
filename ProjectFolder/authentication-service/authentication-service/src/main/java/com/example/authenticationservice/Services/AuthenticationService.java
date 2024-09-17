package com.example.authenticationservice.Services;

import com.example.authenticationservice.DTOs.RegistrationRequest;
import com.example.authenticationservice.Models.User;
import com.example.authenticationservice.Repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthenticationService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public void register(RegistrationRequest registrationRequest) {
        User user = new User();
        user.setEmail(registrationRequest.email());
        user.setPassword(bCryptPasswordEncoder.encode(registrationRequest.password()));
        userRepository.save(user);
    }

}
