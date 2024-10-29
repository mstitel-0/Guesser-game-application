package org.example.gameeservice.DTOs;

import jakarta.validation.constraints.NotBlank;
import org.example.gameeservice.Enums.GameStatus;
import org.example.gameeservice.Enums.GameTopic;

import java.io.Serializable;
import java.util.List;

public record GameDTO(
        @NotBlank
        Long id,
        @NotBlank
        String riddle,
        @NotBlank
        List<HintDTO> hints,
        @NotBlank
        GameTopic gameTopic,
        @NotBlank
        GameStatus gameStatus,
        @NotBlank
        String answer,
        @NotBlank
        int guessCount) implements Serializable {
}
