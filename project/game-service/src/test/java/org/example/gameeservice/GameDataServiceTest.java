package org.example.gameeservice;

import org.example.gameeservice.DTOs.GameDTO;
import org.example.gameeservice.DTOs.HintDTO;
import org.example.gameeservice.Enums.GameStatus;
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

    private static final Long USER_LONG_ID = 1L;
    private static final Long GAME_LONG_ID = 1L;
    private static final String USER_STRING_ID = "1";
    private static final String GAME_STRING_ID = "1";
    private static final String RIDDLE_TEXT = "riddle";
    private static final String ANSWER_TEXT = "answer";
    private static final GameTopic ANIMAL_GAME_TOPIC = GameTopic.ANIMALS;
    private static final GameStatus WON_GAME_STATUS = GameStatus.WON;
    private static final GameStatus LOST_GAME_STATUS = GameStatus.LOST;
    private static final String WON_GAME_STATUS_STRING = "WON";
    private static final String EXCEPTION_GAME_NOT_FOUND_MESSAGE = "Games not found";
    private static final int GUESS_COUNT_ZERO = 0;
    private static final List<HintDTO> EMPTY_HINT_DTO_LIST = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getUserGameNotFound(){
        when(gameRepository.findByGameSessionUserIdAndId(USER_LONG_ID, GAME_LONG_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(GamesNotFoundException.class,
                () -> gameDataService.getUserGame(USER_STRING_ID, GAME_STRING_ID));

        assertEquals(exception.getMessage(), EXCEPTION_GAME_NOT_FOUND_MESSAGE);
    }

    @Test
    public void getUserGameSuccessfully(){
        GameSession gameSession = new GameSession(USER_LONG_ID);
        Game expectedGame = new Game(RIDDLE_TEXT, gameSession, ANSWER_TEXT, ANIMAL_GAME_TOPIC);
        GameDTO expectedGameDto = new GameDTO(GAME_LONG_ID, RIDDLE_TEXT, new ArrayList<>(), GameTopic.ANIMALS, GameStatus.WON, "answer", 0);

        when(gameRepository.findByGameSessionUserIdAndId(USER_LONG_ID, GAME_LONG_ID)).thenReturn(Optional.of(expectedGame));
        when(gameMapperService.map(expectedGame)).thenReturn(expectedGameDto);

        GameDTO game  =  gameDataService.getUserGame(USER_STRING_ID, GAME_STRING_ID);

        assertNotNull(game);
        assertNotNull(game.hints());
        assertEquals(game.riddle(), expectedGameDto.riddle());
        assertEquals(game.answer(), expectedGameDto.answer());
        assertEquals(game.gameTopic(), expectedGameDto.gameTopic());
        assertEquals(game.guessCount(), expectedGameDto.guessCount());
    }

    @Test
    public void getAllUserGamesSuccessfully(){
        GameSession gameSession = new GameSession(USER_LONG_ID);
        Game expectedGame = new Game(RIDDLE_TEXT, gameSession, ANSWER_TEXT, ANIMAL_GAME_TOPIC);
        GameDTO expectedGameDto = new GameDTO(GAME_LONG_ID, RIDDLE_TEXT, EMPTY_HINT_DTO_LIST, ANIMAL_GAME_TOPIC, WON_GAME_STATUS,ANSWER_TEXT, GUESS_COUNT_ZERO);
        List<GameDTO> expectedGames = List.of(expectedGameDto);

        when(gameRepository.findAllByGameSessionUserId(USER_LONG_ID)).thenReturn(Optional.of(List.of(expectedGame)));
        when(gameMapperService.map(expectedGame)).thenReturn(expectedGameDto);

        List<GameDTO> games = gameDataService.getAllUserGames(USER_STRING_ID, null);

        assertNotNull(games);
        assertEquals(games.size(), expectedGames.size());
        assertEquals(games.get(0).riddle(), expectedGames.get(0).riddle());
    }

    @Test
    public void getAllUSerGamesNotFound(){
        when(gameRepository.findAllByGameSessionUserId(USER_LONG_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(GamesNotFoundException.class,
                () -> gameDataService.getUserGame(USER_STRING_ID, GAME_STRING_ID));

        assertEquals(exception.getMessage(), EXCEPTION_GAME_NOT_FOUND_MESSAGE);
    }

    @Test
    public void getAllUserGamesWithStatusSuccessfully(){
        GameSession gameSession = new GameSession(USER_LONG_ID);
        Game expectedGame = new Game(RIDDLE_TEXT, gameSession, ANSWER_TEXT, ANIMAL_GAME_TOPIC);
        GameDTO expectedGameDto = new GameDTO(GAME_LONG_ID, RIDDLE_TEXT, EMPTY_HINT_DTO_LIST, ANIMAL_GAME_TOPIC, WON_GAME_STATUS,ANSWER_TEXT, GUESS_COUNT_ZERO);
        List<GameDTO> expectedGames = List.of(expectedGameDto);

        when(gameRepository.findAllByGameSessionUserIdAndGameStatus(USER_LONG_ID, GameStatus.WON)).thenReturn(Optional.of(List.of(expectedGame)));
        when(gameMapperService.map(expectedGame)).thenReturn(expectedGameDto);

        List<GameDTO> games = gameDataService.getAllUserGames(USER_STRING_ID, WON_GAME_STATUS_STRING);

        assertNotNull(games);
        assertEquals(games.get(0).gameStatus(), expectedGames.get(0).gameStatus());
        assertEquals(games.size(), expectedGames.size());
    }

    @Test
    public void getAllUserGamesWithStatusNotFound(){
        when(gameRepository.findAllByGameSessionUserIdAndGameStatus(USER_LONG_ID, LOST_GAME_STATUS)).thenReturn(Optional.empty());

        Exception exception = assertThrows(GamesNotFoundException.class,
                () -> gameDataService.getUserGame(USER_STRING_ID, GAME_STRING_ID));

        assertEquals(exception.getMessage(), EXCEPTION_GAME_NOT_FOUND_MESSAGE);
    }
}
