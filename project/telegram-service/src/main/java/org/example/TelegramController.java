package org.example;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/telegram/webhook")
public class TelegramController {

    @GetMapping("/start")
    public ResponseEntity<String> start() {
        return ResponseEntity.ok("Hello World!");
    }
}
