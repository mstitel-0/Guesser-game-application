package org.example.authenticationservice.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "\"user\"")
public final class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    @Column(nullable = true)
    private Long telegramId;

    private boolean isActivated;


    public User() {
    }

    public User(Long id, String email, String password, Long telegramId, boolean isActivated) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.telegramId = telegramId;
        this.isActivated = isActivated;
    }

    public User(String email, String password, Long telegramId, boolean isActivated) {
        this.email = email;
        this.password = password;
        this.telegramId = telegramId;
        this.isActivated = isActivated;
    }



    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Long getTelegramId() {
        return telegramId;
    }

    public boolean isActivated() {
        return isActivated;
    }

}
