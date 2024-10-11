package org.example.gameeservice.Models;

import jakarta.persistence.*;

@Entity
public class GameMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;

    private String message;

    @ManyToOne
    private GameSession gameSession;
}
