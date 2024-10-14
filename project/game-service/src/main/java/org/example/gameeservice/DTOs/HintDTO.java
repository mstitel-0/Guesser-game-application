package org.example.gameeservice.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HintDTO(
        @JsonProperty(required = true, value = "hint")
        String hint) {
}
