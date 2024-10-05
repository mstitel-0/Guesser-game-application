package org.example.authenticationservice.Controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.authenticationservice.DTOs.JwtResponse;
import org.example.authenticationservice.DTOs.LoginRequest;
import org.example.authenticationservice.DTOs.RegistrationRequest;
import org.example.authenticationservice.Services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

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
        final JwtResponse jwtResponse = authenticationService.login(loginRequest);
        final Cookie refreshCookie =  new Cookie("refreshToken", jwtResponse.refreshToken());

        refreshCookie.setPath("/api/authentication/**");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setMaxAge(12 * 60 * 60);

        httpServletResponse.addCookie(refreshCookie);

        return ResponseEntity.ok(jwtResponse.accessToken());
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(HttpServletRequest httpServletRequest) {
        final String refreshToken = Arrays
                .stream(httpServletRequest.getCookies())
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .findFirst().orElseThrow(() -> new BadCredentialsException("Refresh token is not found."))
                .getValue();

        return ResponseEntity.ok(authenticationService.refresh(refreshToken));
    }

}
