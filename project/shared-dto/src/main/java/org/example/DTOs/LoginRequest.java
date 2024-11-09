package org.example.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @NotNull
        @Email
        String email,

        @NotNull
        @Size(min = 5, max = 40)
        String password) {
}
