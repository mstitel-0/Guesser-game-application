package org.example.gameeservice.Models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String riddle;
    @OneToMany(mappedBy = "game")
    private List<Hint> hints;
    private int guessesCount;
    private GameStatus gameStatus;
    private GameTopic gameTopic;

    protected Game() {
    }

    public Game(String riddle, GameTopic gameTopic) {
        this.riddle = riddle;
        this.hints = new ArrayList<>();
        this.guessesCount = 0;
        this.gameStatus = GameStatus.IN_PROGRESS;
        this.gameTopic = gameTopic;
    }

    public Long getId() {
        return id;
    }

    public String getRiddle() {
        return riddle;
    }

    public List<Hint> getHints() {
        return hints;
    }

    public int getGuessesCount() {
        return guessesCount;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public GameTopic getGameTopic() {
        return gameTopic;
    }

    public void addHint(Hint hint) {
        this.hints.add(hint);
    }

    public void setGuessesCount(int guessesCount) {
        this.guessesCount = guessesCount;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }
}
