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

    private boolean isActivated;


    public User() {
    }

    public User(Long id, String email, String password, boolean isActivated) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.isActivated = isActivated;
    }

    public User(String email, String password, boolean isActivated) {
        this.email = email;
        this.password = password;
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

    public boolean isActivated() {
        return isActivated;
    }

}
