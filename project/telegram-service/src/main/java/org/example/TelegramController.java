package org.example;

import org.example.DTOs.TelegramMessage;
import org.example.services.TelegramService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/telegram/webhook")
public class TelegramController {

    private final RestClient restClient;
    private final TelegramService telegramService;

    public TelegramController(RestClient restClient, TelegramService telegramService) {
        this.restClient = restClient;
        this.telegramService = telegramService;
    }

    @PostMapping("/process")
    public ResponseEntity<String> telegramStart(@RequestBody TelegramMessage message) {
        System.out.println(message);
        Map<String, Object> payload = new HashMap<>();
        String response = telegramService.processRegistration(message);
        payload.put("chat_id", message.message().chat().id());
        payload.put("text", response);
        restClient.post()
                .uri("/sendMessage")
                .body(payload)
                .retrieve()
                .toBodilessEntity();

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
