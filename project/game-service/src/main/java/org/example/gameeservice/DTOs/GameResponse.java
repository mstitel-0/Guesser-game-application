package org.example.gameeservice.DTOs;

import jakarta.validation.constraints.NotBlank;
import org.example.gameeservice.Enums.GameStatus;

import java.util.Optional;

public record GameResponse(
        @NotBlank
        String message,
        @NotBlank
        Optional<String> hint,
        @NotBlank
        GameStatus  gameStatus
) {
}
