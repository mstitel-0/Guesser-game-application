package org.example.gameeservice;

import org.example.gameeservice.DTOs.GameDTO;
import org.example.gameeservice.Enums.GameStatus;
import org.example.gameeservice.Enums.GameTopic;
import org.example.gameeservice.Models.Game;
import org.example.gameeservice.Models.GameSession;
import org.example.gameeservice.Services.GameMapperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

public class GameMapperServiceTest {
    @InjectMocks
    private GameMapperService gameMapperService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testMapGameToGameDtoSuccessfully() {
        GameSession gameSession = new GameSession(1L);
        Game game = new Game("riddle", gameSession, "ANSWER", GameTopic.ANIMALS);
        game.setGameStatus(GameStatus.WON);

        GameDTO gameDTO = gameMapperService.map(game);

        assertNotNull(gameDTO);
        assertNotNull(gameDTO.hints());
        assertEquals(gameDTO.riddle(), game.getRiddle());
        assertEquals(gameDTO.answer(), game.getAnswer());
        assertEquals(gameDTO.gameTopic(), game.getGameTopic());
        assertEquals(gameDTO.guessCount(), game.getGuessesCount());
        assertEquals(gameDTO.gameStatus(), game.getGameStatus() );
    }
}
