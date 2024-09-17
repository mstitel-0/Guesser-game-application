package com.example.authenticationservice.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
        @Email
        String email,
        @Size(min = 5)
        String password) {
}
