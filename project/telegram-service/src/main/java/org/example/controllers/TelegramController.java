package org.example.controllers;

import org.example.DTOs.TelegramMessage;
import org.example.services.TelegramService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/telegram/webhook")
public class TelegramController {

    private final TelegramService telegramService;

    public TelegramController(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @PostMapping("/process")
    public ResponseEntity<String> telegramStart(@RequestBody TelegramMessage message) {
        System.out.println("Accepted request: " + message);
        telegramService.processRequest(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
