package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;

public class LobbyUpdateDTO {
    private String lobbyID;
    private String host;
    private PlayerDTO[] players;
    private GameType type;
    private SupportedLanguages language;
    
    // Getters and setters
    public String getLobbyID() {
        return lobbyID;
    }
    
    public void setLobbyID(String lobbyID) {
        this.lobbyID = lobbyID;
    }
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public PlayerDTO[] getPlayers() {
        return players;
    }
    
    public void setPlayers(PlayerDTO[] players) {
        this.players = players;
    }
    
    public GameType getType() {
        return type;
    }
    
    public void setType(GameType type) {
        this.type = type;
    }
    
    public SupportedLanguages getLanguage() {
        return language;
    }
    
    public void setLanguage(SupportedLanguages language) {
        this.language = language;
    }
}