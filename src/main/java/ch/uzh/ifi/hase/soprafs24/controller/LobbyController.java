package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.classes.OldGame;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class LobbyController {

    private final SimpMessagingTemplate messagingTemplate;

    // In-memory map: lobbyId -> set of ready usernames
    private final Map<String, Set<String>> readyPlayersByLobby = new ConcurrentHashMap<>();

    public LobbyController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/lobby/{lobbyId}/ready")
    public void handleUpdateReadyUser(@DestinationVariable String lobbyId, String username) {
        System.out.println("User " + username + " is ready to play: " + lobbyId);

        // Get or create the ready set for the lobby
        readyPlayersByLobby.putIfAbsent(lobbyId, ConcurrentHashMap.newKeySet());
        Set<String> readyPlayers = readyPlayersByLobby.get(lobbyId);

        // Toggle readiness
        if (readyPlayers.contains(username)) {
            readyPlayers.remove(username);
        } else {
            readyPlayers.add(username);
        }

        // Send updated ready list to all clients in the lobby
        Map<String, Object> message = new HashMap<>();
        message.put("type", "ready");
        message.put("readyPlayers", readyPlayers);

        messagingTemplate.convertAndSend("/topic/lobby/" + lobbyId, message);
    }

    @MessageMapping("/lobby/{lobbyId}/oldGame")
    public void handlePlayOldGame(@DestinationVariable String lobbyId, OldGame oldGame) {
        System.out.println("LobbyId " + lobbyId + " wants to play an old game: " + lobbyId);

        // Send updated ready list to all clients in the lobby
        Map<String, Object> message = new HashMap<>();
        message.put("type", "game");
        message.put("gameId", oldGame.getGameId());
        message.put("username", oldGame.getUsername());

        messagingTemplate.convertAndSend("/topic/lobby/" + lobbyId, message);
    }
}
