package com.example.authenticationservice.Controllers;

import com.example.authenticationservice.DTOs.RegistrationRequest;
import com.example.authenticationservice.Services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody RegistrationRequest registrationRequest) {
        authenticationService.register(registrationRequest);
        return ResponseEntity.ok("New user has been registered successfully");
    }

}
