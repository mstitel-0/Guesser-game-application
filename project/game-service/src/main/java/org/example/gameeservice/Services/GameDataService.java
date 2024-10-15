package org.example.gameeservice.Services;

import org.example.gameeservice.DTOs.GameDTO;
import org.example.gameeservice.DTOs.HintDTO;
import org.example.gameeservice.Exceptions.GamesNotFoundException;
import org.example.gameeservice.Repositories.GameRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameDataService {
    private final GameRepository gameRepository;

    public GameDataService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Cacheable(value = "gameCache", key = "#a0")
    public List<GameDTO> getAllUserGames(String userId) {
        System.out.println("Method invoked with userId: " + userId);
        return gameRepository.findAllByGameSessionUserId(Long.valueOf(userId))
                .filter(games -> !games.isEmpty())
                .map(games -> games.stream()
                        .map(game -> new GameDTO(game.getId(), game.getRiddle(), game.getHints()
                                .stream()
                                .map(hint -> new HintDTO(hint.getHint()))
                                .toList(),
                                game.getAnswer(), game.getGuessesCount()))
                        .toList())
                .orElseThrow(GamesNotFoundException::new);
    }
}
