package org.example.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record MailConfirmationRequest(
        @NotNull
        @Email
        String email,
        @NotNull
        String token) {
}
