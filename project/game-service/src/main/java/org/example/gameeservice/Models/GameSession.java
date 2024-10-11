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
}
