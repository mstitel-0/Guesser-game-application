package org.example.gameeservice.Controllers;

import org.example.gameeservice.DTOs.GameResponse;
import org.example.gameeservice.Services.GameService;
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
    public ResponseEntity<String> startGame(@RequestHeader(name = "X-Game-User-Id") String id, @RequestBody String topic) {
        return new ResponseEntity<>(gameService.startNewGame(id, topic), HttpStatus.OK);
    }

    @PostMapping("/{gameId}/guess")
    public ResponseEntity<GameResponse> guess(@PathVariable(name = "gameId") Long gameId,
                                              @RequestBody String guess) {
        return new ResponseEntity<>(gameService.guess(gameId, guess), HttpStatus.OK);
    }
}
