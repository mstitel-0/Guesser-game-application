package org.example.gameeservice.DTOs.OpenaiAPI;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record OpenaiInputDTO(
        @NotBlank
        List<OpenaiMessageDTO> messages
) {
}
