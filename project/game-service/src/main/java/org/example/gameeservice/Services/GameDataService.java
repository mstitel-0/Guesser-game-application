package org.example.gameeservice.Services;

import org.example.gameeservice.DTOs.GameDTO;
import org.example.gameeservice.Enums.GameStatus;
import org.example.gameeservice.Exceptions.GamesNotFoundException;
import org.example.gameeservice.Repositories.GameRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameDataService {
    private final GameRepository gameRepository;

    private final GameMapperService gameMapperService;

    public GameDataService(GameRepository gameRepository, GameMapperService gameMapperService) {
        this.gameRepository = gameRepository;
        this.gameMapperService = gameMapperService;
    }

    @Cacheable(value = "gameCache", condition = "#a1 == null", key = "#a0" )
    public List<GameDTO> getAllUserGames(String userId, String gameStatus) {
        if (gameStatus == null ){
            return gameRepository.findAllByGameSessionUserId(Long.valueOf(userId))
                    .map(games -> games.stream()
                            .map(gameMapperService::map)
                            .toList())
                    .orElseThrow(GamesNotFoundException::new);
        } else {
            return gameRepository.findAllByGameSessionUserIdAndGameStatus(Long.valueOf(userId), GameStatus.valueOf(gameStatus))
                    .map(games -> games.stream()
                            .map(gameMapperService::map)
                            .toList())
                    .orElseThrow(GamesNotFoundException::new);
        }
    }

    public GameDTO getUserGame(String userId, String gameId) {
        return gameRepository.findByGameSessionUserIdAndId(Long.valueOf(userId), Long.valueOf(gameId))
                .map(gameMapperService::map)
                .orElseThrow(GamesNotFoundException::new);
    }

    public Long getUserMostRecentGameAnswer(String userId) {
        return gameRepository.findFirstByGameSessionUserIdOrderByIdDesc(Long.valueOf(userId))
                .map(game -> {
                    GameDTO gameDTO = gameMapperService.map(game);
                    return gameDTO.id();
                })
                .orElseThrow(GamesNotFoundException::new);
    }
}
