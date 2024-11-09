package org.example.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GameGuessResponseDTO(
        @JsonProperty(value = "message")
        String message,

        @JsonProperty(value = "hint", required = false)
        String hint,

        @JsonProperty(value = "gameStatus")
        String status) {
}
