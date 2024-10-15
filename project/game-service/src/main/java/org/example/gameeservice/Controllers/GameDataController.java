package org.example.gameeservice.Controllers;

import org.example.gameeservice.DTOs.GameDTO;
import org.example.gameeservice.Services.GameDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/game")
public class GameDataController {
    private final GameDataService gameDataService;

    public GameDataController(GameDataService gameDataService) {
        this.gameDataService = gameDataService;
    }

    @GetMapping("/games")
    public ResponseEntity<List<GameDTO>> getAllUserGames(@RequestHeader(name = "X-Game-User-Id") String userId) {
        return new ResponseEntity<>(gameDataService.getAllUserGames(userId), HttpStatus.OK);
    }
}
