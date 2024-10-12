package org.example.gameeservice.Models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @OneToOne(mappedBy = "gameSession")
    private Game game;

    @OneToMany(mappedBy = "gameSession")
    private List<GameMessage> gameMessages;

    protected GameSession() {
    }

    public GameSession(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Game getGame() {
        return game;
    }

    public List<GameMessage> getGameMessages() {
        return gameMessages;
    }

}
