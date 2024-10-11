package org.example.gameeservice.DTOs;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ChoiceDTO(
        @NotBlank
        int index,
        @NotBlank
        List<MessageDTO> messages
) {
}
