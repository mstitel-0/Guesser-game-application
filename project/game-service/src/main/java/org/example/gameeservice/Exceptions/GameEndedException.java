package org.example.gameeservice.Exceptions;

import org.example.gameeservice.Models.GameStatus;

public class GameEndedException extends RuntimeException {
    public GameEndedException(GameStatus gameStatus) {
        super("Game ended with following status: " + gameStatus.toString());
    }
}
