package ch.uzh.ifi.hase.soprafs24.classes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import java.lang.IllegalArgumentException;

public class InMemoryStore {
    private static Map<String, Lobby> lobbyMap = new HashMap<>();
    private static Map<String, Game> gameMap = new HashMap<>();

    // Lobby methods
    public static Optional<Lobby> getLobby(String lobbyId) {
        return (lobbyMap.get(lobbyId) == null) ? Optional.empty() : Optional.of(lobbyMap.get(lobbyId));
    }
    
    public static void putLobby(String lobbyId, Lobby lobby) throws IllegalArgumentException {
        if (!lobbyId.equals(lobby.getLobbyID())) {throw new IllegalArgumentException("InMemoryStore; putLobby; provided lobbyID and ID of provided Lobby do not match!");}
        lobbyMap.put(lobbyId, lobby);
    }
    
    public static void removeLobby(String lobbyId) {
        lobbyMap.remove(lobbyId);
    }

    // Game methods
    public static Optional<Game> getGame(String gameId) {
        return (gameMap.get(gameId) == null) ? Optional.empty() : Optional.of(gameMap.get(gameId));
    }
    
    public static void putGame(String gameId, Game game) throws IllegalArgumentException {
        if (!gameId.equals(game.getGameID())) {throw new IllegalArgumentException("InMemoryStore; putGame; provided gameID and ID of provided Game do not match!");}
        gameMap.put(gameId, game);
    }
    
    public static void removeGame(String gameId) {
        gameMap.remove(gameId);
    }

    public static void clear() {
        lobbyMap.clear();
        gameMap.clear();
    }
    
    // Debugging getter; REMOVE WHEN BUILD IS STABLE
    public static Map<String, Game> getMap() {
        return gameMap;
    }
}

