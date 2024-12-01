package org.example.controllers;

import org.example.services.TelegramBot;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequestMapping("/api/telegram/webhook")
public class TelegramController {

    private final TelegramBot telegramBot;

    public TelegramController(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }


    @PostMapping
    public ResponseEntity<BotApiMethod<?>> telegramStart(@RequestBody Update update) {
        System.out.println("Accepted request: " + update.getMessage().getText());
        System.out.println("ChatID: " + update.getMessage().getChatId());
        return new ResponseEntity<>(telegramBot.onWebhookUpdateReceived(update), HttpStatus.OK);
    }

}
