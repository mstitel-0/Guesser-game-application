package org.example.gameeservice.Models;

import jakarta.persistence.*;
import org.example.gameeservice.Enums.GameStatus;
import org.example.gameeservice.Enums.GameTopic;

import java.util.ArrayList;
import java.util.List;


@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_session_id")
    private GameSession gameSession;
    private String riddle;
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Hint> hints;
    private String answer;
    private int guessesCount;
    private GameStatus gameStatus;
    private GameTopic gameTopic;

    protected Game() {
    }

    public Game(String riddle, GameSession gameSession, String answer, GameTopic gameTopic) {
        this.gameSession = gameSession;
        this.riddle = riddle;
        this.hints = new ArrayList<>();
        this.answer = answer;
        this.guessesCount = 0;
        this.gameStatus = GameStatus.IN_PROGRESS;
        this.gameTopic = gameTopic;
    }

    public Long getId() {
        return id;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public String getRiddle() {
        return riddle;
    }

    public List<Hint> getHints() {
        return hints;
    }

    public String getAnswer() {
        return answer;
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

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public void setGuessesCount(int guessesCount) {
        this.guessesCount = guessesCount;
    }
    public void addHint(Hint hint) {
        this.hints.add(hint);
    }
}
