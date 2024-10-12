package org.example.gameeservice.Enums;

import org.example.gameeservice.Exceptions.InvalidTopicException;

public enum GameTopic {
    ANIMALS, CARS, FOOD;

    public static GameTopic topicFromString(String topic) {
        return switch (topic.toLowerCase()) {
            case "animal" -> ANIMALS;
            case "food" -> FOOD;
            case "cars" -> CARS;
            default -> throw new InvalidTopicException();
        };
    }
}
