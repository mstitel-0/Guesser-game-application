package org.example.gameeservice.Controllers;

import org.apache.kafka.common.security.auth.AuthenticationContext;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.example.gameeservice.DTOs.GameResponse;
import org.example.gameeservice.DTOs.RiddleDTO;
import org.example.gameeservice.Enums.GameTopic;
import org.example.gameeservice.Services.GameService;
import org.example.gameeservice.Services.RiddleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start")
    public ResponseEntity<String> startGame(@RequestHeader("Game-User-Id")Long userId,
                                               @RequestBody String topic) {
        return new ResponseEntity<>(gameService.startNewGame(userId, topic), HttpStatus.OK);
    }

    @PostMapping("/{gameId}")
    public ResponseEntity<GameResponse> guess(@PathVariable(name = "gameId") Long gameId,
                                              @RequestBody String guess) {
        return new ResponseEntity<>(gameService.guess(gameId, guess), HttpStatus.OK);
    }
}
