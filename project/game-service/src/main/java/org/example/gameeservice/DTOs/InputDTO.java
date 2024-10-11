package org.example.gameeservice.DTOs;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record InputDTO(
        @NotBlank
        List<MessageDTO> messages
) {
}
