package org.example.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Message(@JsonProperty(value = "from") User user, Chat chat, String text) {
}
