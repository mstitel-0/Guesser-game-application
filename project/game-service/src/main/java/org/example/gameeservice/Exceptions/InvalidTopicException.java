package org.example.gameeservice.Exceptions;

public class InvalidTopicException extends RuntimeException {
    public InvalidTopicException() {
        super("Invalid topic. Choose among: animals, food and cars");
    }
}
