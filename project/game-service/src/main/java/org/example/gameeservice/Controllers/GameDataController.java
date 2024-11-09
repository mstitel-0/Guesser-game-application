package org.example.gameeservice.Controllers;

import org.example.gameeservice.DTOs.GameDTO;
import org.example.gameeservice.Services.GameDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game")
public class GameDataController {
    private final GameDataService gameDataService;

    public GameDataController(GameDataService gameDataService) {
        this.gameDataService = gameDataService;
    }

    @GetMapping("{gameId}")
    public ResponseEntity<GameDTO> getUserGame(@RequestHeader(name = "X-Game-User-Id") String userId,
                                           @PathVariable("gameId") String gameId) {
        return new ResponseEntity<>(gameDataService.getUserGame(userId, gameId), HttpStatus.OK);
    }

    @GetMapping("/games")
    public ResponseEntity<List<GameDTO>> getAllUserGames(@RequestHeader(name = "X-Game-User-Id") String userId,
                                                         @RequestParam(name = "gameStatus",required = false) String gameStatus) {
        return new ResponseEntity<>(gameDataService.getAllUserGames(userId, gameStatus), HttpStatus.OK);
    }

    @GetMapping("/games/recent")
    public ResponseEntity<Long> getUserMostRecentGame(@RequestHeader(name = "X-Game-User-Id") String userId) {
        return new ResponseEntity<>(gameDataService.getUserMostRecentGameAnswer(userId), HttpStatus.OK);
    }
}
