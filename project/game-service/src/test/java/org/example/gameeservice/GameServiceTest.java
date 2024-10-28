package org.example.gameeservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.example.gameeservice.DTOs.GameResponse;
import org.example.gameeservice.DTOs.HintDTO;
import org.example.gameeservice.DTOs.RiddleDTO;
import org.example.gameeservice.Enums.GameStatus;
import org.example.gameeservice.Enums.GameTopic;
import org.example.gameeservice.Exceptions.GameEndedException;
import org.example.gameeservice.Exceptions.InvalidTopicException;
import org.example.gameeservice.Models.Game;
import org.example.gameeservice.Models.GameSession;
import org.example.gameeservice.Models.Hint;
import org.example.gameeservice.Repositories.GameRepository;
import org.example.gameeservice.Services.GameMessageService;
import org.example.gameeservice.Services.GameService;
import org.example.gameeservice.Services.RiddleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;


public class GameServiceTest {
    @InjectMocks
    private GameService gameService;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private RiddleService riddleService;
    @Mock
    private GameMessageService gameMessageService;


    private static final int EXCEED_MAX_GUESS_COUNT = 11;
    private static final int UNDER_MAX_GUESS_COUNT = 9;
    private static final int HINT_REQUIRED = 9;
    private static final int HINT_NOT_REQUIRED = 8;
    private static final String ANSWER = "TEST123";
    private static final String CORRECT_GUESS = "TEST123";
    private static final String INCORRECT_GUESS = "TEST1234";
    private static final String RIDDLE_TEXT = "RIDDLE TEXT";
    private static final String HINT_TEXT = "HINT TEXT";
    private static final String INVALID_TOPIC = "TOPIC";
    private static final String GAME_NOT_FOUND_EXCEPTION = "Game not found";
    private static final String VALID_TOPIC_ANIMALS = "animals";
    private static final String INVALID_TOPIC_EXCEPTION_MESSAGE = "Invalid topic. Choose among: animals, food and cars";
    private static final String GAME_ENDED_EXCEPTION_MESSAGE = "Game ended with following status: ";
    private static final GameStatus ONGOING_GAME_STATUS = GameStatus.IN_PROGRESS;
    private static final GameStatus WON_GAME_STATUS = GameStatus.WON;
    private static final GameStatus LOST_GAME_STATUS = GameStatus.LOST;
    private static final GameTopic ANIMALS_GAME_TOPIC = GameTopic.ANIMALS;
    private static final String USER_ID = "1";
    private static final Long LONG_USER_ID = 1L;
    private static final Long GAME_ID = 1L;
    private static final String CORRECT_GUESS_MESSAGE = "You won, congratulations!";
    private static final String INCORRECT_GUESS_MESSAGE = "Incorrect guess, try again";
    private static final String LOST_GUESS_MESSAGE = "You lost!";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testIsMaxGuessExceededShouldReturnTrue(){
        Boolean isExceeded = gameService.isMaxGuessExceeded(UNDER_MAX_GUESS_COUNT);

        assertEquals(isExceeded, false);
    }

    @Test
    public void testIsMaxGuessExceededShouldReturnFalse(){
        Boolean isExceeded = gameService.isMaxGuessExceeded(EXCEED_MAX_GUESS_COUNT);

        assertEquals(isExceeded, true);
    }

    @Test
    public void testIsHintRequiredShouldReturnTrue(){
        Boolean isRequired = gameService.isHintRequired(HINT_REQUIRED);

        assertEquals(isRequired, true);
    }

    @Test
    public void testIsHintRequiredShouldReturnFalse(){
        Boolean isRequired = gameService.isHintRequired(HINT_NOT_REQUIRED);

        assertEquals(isRequired, false);
    }

    @Test
    public void isGuessCorrectShouldReturnTrue(){
        Boolean isCorrectGuess = gameService.isGuessCorrect(ANSWER, CORRECT_GUESS);

        assertEquals(isCorrectGuess, true);
    }

    @Test
    public void isGuessCorrectShouldReturnFalse(){
        Boolean isCorrectGuess = gameService.isGuessCorrect(ANSWER, INCORRECT_GUESS);

        assertEquals(isCorrectGuess, false);
    }

    @Test
    public void isGameOngoingShouldReturnTrue(){
        Boolean isGameOngoing = gameService.isGameOngoing(ONGOING_GAME_STATUS);

        assertEquals(isGameOngoing, true);
    }

    @Test
    public void isGameOngoingShouldReturnFalse(){
        Boolean isGameOngoing = gameService.isGameOngoing(WON_GAME_STATUS);

        assertEquals(isGameOngoing, false);
    }

    @Test
    public void handleHintShouldReturnStringHint() {
        Game game = mock(Game.class);

        when(game.getGuessesCount()).thenReturn(HINT_REQUIRED);
        when(game.getRiddle()).thenReturn(RIDDLE_TEXT);
        when(game.getHints()).thenReturn(new ArrayList<>());

        HintDTO hint = new HintDTO(HINT_TEXT);
        when(riddleService.getHint(RIDDLE_TEXT, game.getHints())).thenReturn(hint);

        String obtainedHint = gameService.handleHint(game);

        assertNotNull(obtainedHint);
        assertEquals(obtainedHint, HINT_TEXT);
        verify(game).addHint(any(Hint.class));
        verify(gameRepository).save(game);
    }

    @Test
    public void handleHintShouldReturnNullStringHint() {
        Game game = mock(Game.class);

        when(game.getGuessesCount()).thenReturn(HINT_NOT_REQUIRED);
        when(game.getRiddle()).thenReturn(RIDDLE_TEXT);
        when(game.getHints()).thenReturn(new ArrayList<>());

        HintDTO hint = new HintDTO(HINT_TEXT);
        when(riddleService.getHint(RIDDLE_TEXT, game.getHints())).thenReturn(hint);

        String obtainedHint = gameService.handleHint(game);

        assertNull(obtainedHint);
        verifyNoInteractions(gameRepository, riddleService);
    }

    @Test
    public void startGameShouldReturnNotValidTopicException() {
        InvalidTopicException ex = assertThrows(InvalidTopicException.class, () ->
                gameService.startNewGame(USER_ID, INVALID_TOPIC));

        assertEquals(INVALID_TOPIC_EXCEPTION_MESSAGE, ex.getMessage());

        verifyNoInteractions(riddleService, gameRepository);
    }
    @Test
    public void startGameShouldReturnNewRiddle() {
        RiddleDTO riddleDTO = new RiddleDTO(RIDDLE_TEXT, ANSWER);

        when(riddleService.getRiddle(ANIMALS_GAME_TOPIC)).thenReturn(riddleDTO);

        String obtainedRiddle = gameService.startNewGame(USER_ID, VALID_TOPIC_ANIMALS);

        assertEquals(obtainedRiddle, RIDDLE_TEXT);

        verify(riddleService).getRiddle(any(GameTopic.class));
        verify(gameRepository).save(any(Game.class));
    }
    @Test
    public void guessShouldReturnGameEndedException(){
        GameSession gameSession = new GameSession(LONG_USER_ID);
        Game game = new Game(RIDDLE_TEXT, gameSession, ANSWER, ANIMALS_GAME_TOPIC);
        game.setGameStatus(GameStatus.WON);

        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.of(game));

        GameEndedException ex = assertThrows(GameEndedException.class, () ->
                gameService.guess(GAME_ID, CORRECT_GUESS));

        assertNotNull(ex.getMessage());
        assertEquals(GAME_ENDED_EXCEPTION_MESSAGE + game.getGameStatus(), ex.getMessage());
    }

    @Test
    public void guessShouldReturnGameNotFound(){
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                gameService.guess(GAME_ID, CORRECT_GUESS));


        assertNotNull(ex.getMessage());
        assertEquals(GAME_NOT_FOUND_EXCEPTION, ex.getMessage());
    }
    @Test
    public void guessShouldReturnGameResponseCorrectGuess(){
        GameSession gameSession = new GameSession(LONG_USER_ID);
        Game game = new Game(RIDDLE_TEXT, gameSession, ANSWER, ANIMALS_GAME_TOPIC);

        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.of(game));

        GameResponse gameResponse = gameService.guess(GAME_ID, CORRECT_GUESS);

        assertEquals(CORRECT_GUESS_MESSAGE, gameResponse.message());
        assertEquals(WON_GAME_STATUS, game.getGameStatus());
    }
    @Test
    public void guessShouldReturnGameResponseIncorrectGuess(){
        GameSession gameSession = new GameSession(LONG_USER_ID);
        Game game = new Game(RIDDLE_TEXT, gameSession, ANSWER, ANIMALS_GAME_TOPIC);

        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.of(game));

        GameResponse gameResponse = gameService.guess(GAME_ID, INCORRECT_GUESS);

        assertEquals(INCORRECT_GUESS_MESSAGE, gameResponse.message());
        assertEquals(ONGOING_GAME_STATUS, game.getGameStatus());
    }
    @Test
    public void guessShouldReturnGameResponseLost(){
        GameSession gameSession = new GameSession(LONG_USER_ID);
        Game game = new Game(RIDDLE_TEXT, gameSession, ANSWER, ANIMALS_GAME_TOPIC);
        game.setGuessesCount(10);
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.of(game));

        GameResponse gameResponse = gameService.guess(GAME_ID, INCORRECT_GUESS);

        assertEquals(LOST_GUESS_MESSAGE, gameResponse.message());
        assertEquals(LOST_GAME_STATUS, game.getGameStatus());
    }

}
