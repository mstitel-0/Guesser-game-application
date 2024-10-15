package org.example.gameeservice.Models;

import jakarta.persistence.*;

@Entity
public class Hint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    private String hint;

    protected Hint() {
    }

    public Hint(Game game, String hint) {
        this.game = game;
        this.hint = hint;
    }

    public Long getId() {
        return id;
    }

    public Game getGame() {
        return game;
    }

    public String getHint() {
        return hint;
    }
}
