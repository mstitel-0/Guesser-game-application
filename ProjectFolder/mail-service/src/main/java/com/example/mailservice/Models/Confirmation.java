package com.example.mailservice.Models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Confirmation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private LocalDateTime dateTime;

    public Confirmation() {
    }

    public Confirmation(String email, LocalDateTime dateTime) {
        this.email = email;
        this.dateTime = dateTime;
    }
}
