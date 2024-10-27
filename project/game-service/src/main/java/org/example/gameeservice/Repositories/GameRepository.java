package org.example.gameeservice.Repositories;

import org.example.gameeservice.Enums.GameStatus;
import org.example.gameeservice.Models.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {
    Optional<List<Game>> findAllByGameSessionUserId(Long userId);
    Optional<List<Game>> findAllByGameSessionUserIdAndGameStatus(Long userId, GameStatus gameStatus);
    Optional<Game> findByGameSessionUserIdAndId(Long userId, Long gameId);

}
