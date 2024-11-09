package org.example.services;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    private static final String TOKEN_PREFIX = "user:token:";
    private static final String GAME_PREFIX = "user:game:";

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveToken(Long userId, String token) {
        String key = TOKEN_PREFIX + userId;
        redisTemplate.opsForValue().set(key, token);
    }

    public void saveGame(Long userId, String gameAnswer) {
        String key = GAME_PREFIX + userId;
        redisTemplate.opsForValue().set(key, gameAnswer);
    }

    public String getToken(Long userId) {
        return redisTemplate.opsForValue().get(TOKEN_PREFIX + userId);
    }

    public String getGameId(Long userId) {
        return redisTemplate.opsForValue().get(GAME_PREFIX + userId);
    }

    public void removeGameID(Long userId) {
        redisTemplate.delete(GAME_PREFIX + userId);
    }
}
