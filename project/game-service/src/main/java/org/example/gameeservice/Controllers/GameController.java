package org.example.gameeservice.Controllers;

import org.example.gameeservice.DTOs.RiddleDTO;
import org.example.gameeservice.Enums.GameTopic;
import org.example.gameeservice.Services.RiddleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final RiddleService riddleService;

    public GameController(RiddleService riddleService) {
        this.riddleService = riddleService;
    }

    @GetMapping("/start")
    public ResponseEntity<RiddleDTO> startGame() {
        return ResponseEntity.ok(riddleService.getRiddle(GameTopic.ANIMALS));
    }
}
