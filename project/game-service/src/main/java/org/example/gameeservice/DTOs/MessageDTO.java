package org.example.gameeservice.DTOs;


import jakarta.validation.constraints.NotBlank;

public record MessageDTO(
        @NotBlank
        String role,
        @NotBlank
        String content
) {
}
