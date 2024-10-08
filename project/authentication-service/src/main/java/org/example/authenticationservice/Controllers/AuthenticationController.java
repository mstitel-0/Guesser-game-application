package org.example.authenticationservice.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.authenticationservice.DTOs.LoginRequest;
import org.example.authenticationservice.DTOs.RegistrationRequest;
import org.example.authenticationservice.Services.AuthenticationService;
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
        return ResponseEntity.ok("Confirm your email.");
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam String token) {
        authenticationService.activate(token);
        return ResponseEntity.ok("Email has been successfully confirmed.");
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
        return ResponseEntity.ok(authenticationService.login(loginRequest, httpServletResponse));
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(authenticationService.refresh(httpServletRequest));
    }

}
