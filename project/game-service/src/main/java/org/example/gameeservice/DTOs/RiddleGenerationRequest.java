package org.example.gameeservice.DTOs;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record RiddleGenerationRequest(
        @NotBlank
        String model,
        @NotBlank
        List<MessageDTO> messages
) {
}
