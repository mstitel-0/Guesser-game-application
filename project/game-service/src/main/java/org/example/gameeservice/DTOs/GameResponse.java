package org.example.gameeservice.DTOs;

import org.example.gameeservice.Models.GameStatus;

import java.util.Optional;

public record GameResponse(
        String message,
        Optional<String> hint,
        GameStatus  gameStatus
) {
}
