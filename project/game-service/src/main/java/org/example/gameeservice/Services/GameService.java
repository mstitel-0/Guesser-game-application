package org.example.gameeservice.Services;

import org.example.gameeservice.DTOs.GameResponse;
import org.example.gameeservice.Exceptions.GameEndedException;
import org.example.gameeservice.Exceptions.InvalidTopicException;
import org.example.gameeservice.Models.Game;
import org.example.gameeservice.Enums.GameStatus;
import org.example.gameeservice.Enums.GameTopic;
import org.example.gameeservice.Models.GameSession;
import org.example.gameeservice.Models.Hint;
import org.example.gameeservice.Repositories.GameRepository;
import org.example.gameeservice.Repositories.HintRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class GameService {

    private final HintRepository hintRepository;

    private final GameRepository gameRepository;

    private static int MAX_GUESS_COUNT = 9;

    public GameService(HintRepository hintRepository, GameRepository gameRepository) {
        this.hintRepository = hintRepository;
        this.gameRepository = gameRepository;
    }

    public String startNewGame(String topic) {

        GameTopic actualTopic = switch (topic) {
            case "animal" -> GameTopic.ANIMALS;
            case "food" -> GameTopic.FOOD;
            case "cars" -> GameTopic.CARS;
            default -> throw new InvalidTopicException();
        };

        //response from chatgpt
        GameSession gameSession = new GameSession();
        Game game = new Game("chatgtp riddle", gameSession,"chatgpt asnwer" , actualTopic);

        gameRepository.save(game);

        Hint hint = new Hint(game, "chatgpt hint");

        hintRepository.save(hint);

        return hint.getHint();
    }

    @Transactional
    public GameResponse guess(Long gameId, String guess) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        if (!isGameOngoing(game.getGameStatus())) {
            throw new GameEndedException(game.getGameStatus());
        }

        String responseMessage = "Incorrect guess, try again";

        game.setGuessesCount(game.getGuessesCount() + 1);

        if (isGuessCorrect(game.getRiddle(), guess)) {
            game.setGameStatus(GameStatus.WON);
            responseMessage = "You won, congratulations!";

        }

        if (isMaxGuessExceeded(game.getGuessesCount())) {
            game.setGameStatus(GameStatus.LOST);
            responseMessage = "You lost!";
        }

        String hint = handleHint(game);

        return new GameResponse(responseMessage,
                Optional.ofNullable(hint),
                game.getGameStatus());
    }

    public Boolean isGameOngoing(GameStatus gameStatus) {
        return gameStatus == GameStatus.IN_PROGRESS;
    }

    public Boolean isGuessCorrect(String answer, String guess) {
        return true;
    }

    public Boolean isMaxGuessExceeded(int guessCount) {
        return guessCount == MAX_GUESS_COUNT;
    }

    public String handleHint(Game game) {
        String hintText = null;

        if (isHintRequired(game.getGuessesCount())) {
            Hint newHint = getHint(game.getRiddle());

            game.addHint(newHint);
            gameRepository.save(game);
            hintRepository.save(newHint);
            hintText = newHint.getHint();
        }

        return hintText;
    }

    public Boolean isHintRequired(int guessCount) {
        return guessCount % 3 == 0;
    }

    public Hint getHint(String riddle) {
        //send request
        return null;
    }
}
