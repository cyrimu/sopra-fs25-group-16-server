package ch.uzh.ifi.hase.soprafs24.classes;

import java.util.HashMap;
import java.util.Map;

public class InMemoryStore {
    private static Map<String, Lobby> lobbyMap = new HashMap<>();
    private static Map<String, Game> gameMap = new HashMap<>();

    // Lobby methods
    public static Lobby getLobby(String lobbyId) {
        return lobbyMap.get(lobbyId);
    }
    
    public static void putLobby(String lobbyId, Lobby lobby) {
        lobbyMap.put(lobbyId, lobby);
    }
    
    public static void removeLobby(String lobbyId) {
        lobbyMap.remove(lobbyId);
    }

    // Game methods
    public static Game getGame(String gameId) {
        return gameMap.get(gameId);
    }
    
    public static void putGame(String gameId, Game game) {
        gameMap.put(gameId, game);
    }
    
    public static void removeGame(String gameId) {
        gameMap.remove(gameId);
    }
}

