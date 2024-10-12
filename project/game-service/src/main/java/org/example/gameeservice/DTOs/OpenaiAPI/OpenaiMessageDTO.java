package org.example.gameeservice.DTOs.OpenaiAPI;


import jakarta.validation.constraints.NotBlank;

public record OpenaiMessageDTO(
        @NotBlank
        String role,
        @NotBlank
        String content
) {
}
