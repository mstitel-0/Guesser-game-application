package org.example.gameeservice.Services;

import org.example.gameeservice.DTOs.GameMessageDTO;
import org.example.gameeservice.DTOs.GameResponse;
import org.example.gameeservice.DTOs.HintDTO;
import org.example.gameeservice.DTOs.RiddleDTO;
import org.example.gameeservice.Exceptions.GameEndedException;
import org.example.gameeservice.Models.Game;
import org.example.gameeservice.Enums.GameStatus;
import org.example.gameeservice.Enums.GameTopic;
import org.example.gameeservice.Models.GameSession;
import org.example.gameeservice.Models.Hint;
import org.example.gameeservice.Repositories.GameRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class GameService {

    private static final String SYSTEM_ROLE = "system";

    private static final String USER_ROLE = "user";

    private final GameRepository gameRepository;

    private final RiddleService riddleService;

    private final GameMessageService gameMessageService;

    private static final int MAX_GUESS_COUNT = 10;

    public GameService(GameRepository gameRepository, RiddleService riddleService, GameMessageService gameMessageService) {
        this.gameRepository = gameRepository;
        this.riddleService = riddleService;
        this.gameMessageService = gameMessageService;
    }

    @Transactional
    @CacheEvict(value = "gameCache", key = "#a0")
    public String startNewGame(String userId, String topic) {
        GameTopic actualTopic = GameTopic.topicFromString(topic);

        GameSession gameSession = new GameSession(Long.valueOf(userId));

        RiddleDTO riddleDTO = riddleService.getRiddle(actualTopic);

        Game game = new Game(riddleDTO.riddle(),
                gameSession,
                riddleDTO.answer() ,
                actualTopic
        );

        gameRepository.save(game);

        return game.getRiddle();
    }

    @Transactional
    public GameResponse guess(Long gameId, String guess) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        if (!isGameOngoing(game.getGameStatus())) {
            throw new GameEndedException(game.getGameStatus());
        }

        GameSession gameSession = game.getGameSession();
        List<GameMessageDTO> messages = new ArrayList<>();

        messages.add(new GameMessageDTO(USER_ROLE, guess));

        String responseMessage = "Incorrect guess, try again";

        game.setGuessesCount(game.getGuessesCount() + 1);

        if (isGuessCorrect(game.getAnswer(), guess)) {
            game.setGameStatus(GameStatus.WON);
            responseMessage = "You won, congratulations!";

        }

        if (isMaxGuessExceeded(game.getGuessesCount())) {
            game.setGameStatus(GameStatus.LOST);
            responseMessage = "You lost!";
        }

        messages.add(new GameMessageDTO(SYSTEM_ROLE, responseMessage));

        String hint = handleHint(game);

        if (hint != null) {
            messages.add(new GameMessageDTO(SYSTEM_ROLE, hint));
        }

        gameMessageService.processGameMessages(gameSession, messages );

        return new GameResponse(responseMessage,
                Optional.ofNullable(hint),
                game.getGameStatus());
    }

    public String handleHint(Game game) {
        String hintText = null;

        if (isHintRequired(game.getGuessesCount())) {
            HintDTO hint = riddleService.getHint(game.getRiddle(), game.getHints());
            Hint newHint = new Hint(game, hint.hint());

            game.addHint(newHint);
            gameRepository.save(game);

            hintText = newHint.getHint();
        }
        return hintText;
    }

    public Boolean isGameOngoing(GameStatus gameStatus) {
        return gameStatus == GameStatus.IN_PROGRESS;
    }

    public Boolean isGuessCorrect(String answer, String guess) {
       return answer.toLowerCase().contains(guess.toLowerCase());
    }

    public Boolean isMaxGuessExceeded(int guessCount) {
        return guessCount >= MAX_GUESS_COUNT;
    }

    public Boolean isHintRequired(int guessCount) {
        return guessCount % 3 == 0;
    }

}
