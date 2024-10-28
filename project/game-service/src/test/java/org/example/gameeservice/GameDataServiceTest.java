package org.example.gameeservice;

import org.example.gameeservice.DTOs.GameDTO;
import org.example.gameeservice.DTOs.HintDTO;
import org.example.gameeservice.Enums.GameTopic;
import org.example.gameeservice.Exceptions.GamesNotFoundException;
import org.example.gameeservice.Models.Game;
import org.example.gameeservice.Models.GameSession;
import org.example.gameeservice.Repositories.GameRepository;
import org.example.gameeservice.Services.GameDataService;
import org.example.gameeservice.Services.GameMapperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class GameDataServiceTest {
    @InjectMocks
    private GameDataService gameDataService;

    @Mock
    private GameMapperService gameMapperService;

    @Mock
    private GameRepository gameRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void GetUserGameNotFound(){
        Long gameId = 1L;
        Long userId = 1L;
        when(gameRepository.findByGameSessionUserIdAndId(userId, gameId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(GamesNotFoundException.class, () -> gameDataService.getUserGame("1", "1"));

        assertEquals(exception.getMessage(), "Games not found");
    }

    @Test
    public void GetUserGameSuccessfully(){
        Long gameId = 1L;
        Long userId = 1L;
        GameSession gameSession = new GameSession(userId);
        Game expectedGame = new Game("riddle", gameSession, "answer", GameTopic.ANIMALS);
        GameDTO expectedGameDto = new GameDTO(gameId, "riddle", new ArrayList<HintDTO>(), GameTopic.ANIMALS, "answer", 0);

        when(gameRepository.findByGameSessionUserIdAndId(userId, gameId)).thenReturn(Optional.of(expectedGame));
        when(gameMapperService.map(expectedGame)).thenReturn(expectedGameDto);

        GameDTO game  =  gameDataService.getUserGame("1", "1");

        assertNotNull(game);
        assertNotNull(game.hints());
        assertEquals(game.riddle(), expectedGameDto.riddle());
        assertEquals(game.answer(), expectedGameDto.answer());
        assertEquals(game.gameTopic(), expectedGameDto.gameTopic());
        assertEquals(game.guessCount(), expectedGameDto.guessCount());
    }
}
