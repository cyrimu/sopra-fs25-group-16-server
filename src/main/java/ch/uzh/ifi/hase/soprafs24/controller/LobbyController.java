package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.classes.GameIdUsername;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class LobbyController {

    private final SimpMessagingTemplate messagingTemplate;

    public LobbyController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/lobby/{lobbyId}/returnLobby")
    public void handleUpdateReadyUser(@DestinationVariable String lobbyId) {
        System.out.println("The host wants to return to lobby: " + lobbyId);

        // Send updated ready list to all clients in the lobby
        Map<String, Object> message = new HashMap<>();
        message.put("type", "returnLobby");

        messagingTemplate.convertAndSend("/topic/lobby/" + lobbyId, message);
    }

    @MessageMapping("/lobby/{lobbyId}/oldGame")
    public void handlePlayOldGame(@DestinationVariable String lobbyId, GameIdUsername gameIdUsername) {
        System.out.println("LobbyId " + lobbyId + " wants to play an old game: " + lobbyId);

        // Send updated ready list to all clients in the lobby
        Map<String, Object> message = new HashMap<>();
        message.put("type", "game");
        message.put("gameId", gameIdUsername.gameId);
        message.put("username", gameIdUsername.username);

        messagingTemplate.convertAndSend("/topic/lobby/" + lobbyId, message);
    }
}
