package org.example.gameeservice.DTOs.OpenaiAPI;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record OpenaiChoiceDTO(
        @NotBlank
        int index,
        @NotBlank
        List<OpenaiMessageDTO> messages
) {
}
