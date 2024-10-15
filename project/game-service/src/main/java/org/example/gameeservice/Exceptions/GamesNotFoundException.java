package org.example.gameeservice.Exceptions;

public class GamesNotFoundException extends RuntimeException {
    public GamesNotFoundException() {
        super("Games not found");
    }
}
