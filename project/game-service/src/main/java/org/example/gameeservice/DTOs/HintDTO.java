package org.example.gameeservice.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record HintDTO(
        @JsonProperty(required = true, value = "hint")
        String hint) implements Serializable {
}
