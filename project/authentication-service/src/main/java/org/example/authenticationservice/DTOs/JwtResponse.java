package org.example.authenticationservice.DTOs;

public record JwtResponse(String accessToken, String refreshToken) {
}
