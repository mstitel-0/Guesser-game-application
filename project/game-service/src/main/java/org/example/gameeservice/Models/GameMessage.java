package org.example.gameeservice.Models;

import jakarta.persistence.*;

@Entity
public class GameMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;

    private String message;

    @ManyToOne(cascade = CascadeType.ALL)
    private GameSession gameSession;

    protected GameMessage() {
    }

    public GameMessage(String role, String message, GameSession gameSession) {
        this.role = role;
        this.message = message;
        this.gameSession = gameSession;
    }
}
