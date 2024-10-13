package org.example.gameeservice.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record RiddleDTO(
        @NotBlank
        @JsonProperty(required = true, value = "riddle")
        String riddle,

        @NotBlank
        @JsonProperty(required = true, value = "answer")
        String answer) {
}
