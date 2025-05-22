package ch.uzh.ifi.hase.soprafs24.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import ch.uzh.ifi.hase.soprafs24.classes.GameIdUsername;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class LobbyControllerTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    private LobbyController lobbyController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        lobbyController = new LobbyController(messagingTemplate);
    }

    @Test
    void handleUpdateReadyUser_sendsReturnLobbyMessage() {
        String lobbyId = "testlobby123";

        lobbyController.handleUpdateReadyUser(lobbyId);

        ArgumentCaptor<Map<String, Object>> messageCaptor = ArgumentCaptor.forClass(Map.class);
        verify(messagingTemplate, times(1))
            .convertAndSend(eq("/topic/lobby/" + lobbyId), messageCaptor.capture());



        Map<String, Object> sent = messageCaptor.getValue();
        assertNotNull(sent, "Message payload null");
        assertEquals("returnLobby", sent.get("type"), "wrong payload type");
    }
    
    
    @Test
    void handlePlayOldGame_sendsGameMessage() {
        String lobbyId = "testlobby123";
        GameIdUsername payload = new GameIdUsername();
        payload.gameId = "testGameID";
        payload.username = "Alice";

        lobbyController.handlePlayOldGame(lobbyId, payload);

        ArgumentCaptor<Map<String, Object>> messageCaptor = ArgumentCaptor.forClass(Map.class);
        verify(messagingTemplate, times(1))
            .convertAndSend(eq("/topic/lobby/" + lobbyId), messageCaptor.capture());

        Map<String, Object> sent = messageCaptor.getValue();
        assertNotNull(sent);
        assertEquals("game", sent.get("type"));
        assertEquals("testGameID", sent.get("gameId"));
        assertEquals("Alice", sent.get("username"));
}

}
