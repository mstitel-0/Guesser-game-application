package org.example.gameeservice;

import static org.mockito.Mockito.*;

import org.example.gameeservice.DTOs.GameMessageDTO;
import org.example.gameeservice.Models.GameSession;
import org.example.gameeservice.Repositories.GameMessageRepository;
import org.example.gameeservice.Services.GameMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

public class GameMessageServiceTest {
    @InjectMocks
    private GameMessageService gameMessageService;

    @Mock
    private GameMessageRepository gameMessageRepository;

    private static final Long TEST_USER_ID = 1L;

    private static final String TEST_USER_ROLE = "TEST_USER_ROLE";

    private static final String TEST_USER_MESSAGE = "TEST MESSAGE";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateGameMessagesSuccessful() {
        GameSession testGameSession = new GameSession(TEST_USER_ID);
        GameMessageDTO testGameMessage = new GameMessageDTO(TEST_USER_ROLE, TEST_USER_MESSAGE);

        List<GameMessageDTO> listWithTestMessage = Collections.singletonList(testGameMessage);

        gameMessageService.processGameMessages(testGameSession, listWithTestMessage);

        verify(gameMessageRepository).saveAll(anyIterable());

        verifyNoMoreInteractions(gameMessageRepository);
    }
}
