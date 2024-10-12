package org.example.gameeservice.Repositories;

import org.example.gameeservice.Models.GameMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameMessageRepository extends CrudRepository<GameMessage, Long> {
}
