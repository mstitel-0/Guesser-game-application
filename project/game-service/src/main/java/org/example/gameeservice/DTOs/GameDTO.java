package org.example.gameeservice.DTOs;

import jakarta.validation.constraints.NotBlank;

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
        String answer,
        @NotBlank
        int guessCount) implements Serializable {
}
