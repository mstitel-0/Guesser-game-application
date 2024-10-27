package org.example.gameeservice.Services;

import org.example.gameeservice.DTOs.GameDTO;
import org.example.gameeservice.DTOs.HintDTO;
import org.example.gameeservice.Models.Game;
import org.springframework.stereotype.Service;

@Service
public class GameMapperService {
    public GameDTO map(Game game) {
        return new GameDTO(game.getId(),
                game.getRiddle(),
                game.getHints()
                        .stream()
                        .map(hint -> new HintDTO(hint.getHint()))
                        .toList(),
                game.getAnswer(),
                game.getGuessesCount()
        );
    }
}
