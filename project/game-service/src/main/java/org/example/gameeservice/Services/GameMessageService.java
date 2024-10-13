package org.example.gameeservice.Services;

import org.example.gameeservice.DTOs.GameMessageDTO;
import org.example.gameeservice.Models.GameMessage;
import org.example.gameeservice.Models.GameSession;
import org.example.gameeservice.Repositories.GameMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameMessageService {
    private final GameMessageRepository gameMessageRepository;

    public GameMessageService(GameMessageRepository gameMessageRepository) {
        this.gameMessageRepository = gameMessageRepository;
    }

    public void processGameMessage(GameSession gameSession, List<GameMessageDTO> gameMessageDTO) {
        gameMessageRepository.saveAll(gameMessageDTO
                .stream()
                .map(dto -> new GameMessage(dto.role(), dto.message(), gameSession))
                .toList());
    }
}
