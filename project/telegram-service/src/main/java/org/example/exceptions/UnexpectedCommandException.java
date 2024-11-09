package org.example.exceptions;

public class UnexpectedCommandException extends RuntimeException {
    public UnexpectedCommandException(String message) {
        super(message);
    }
}
