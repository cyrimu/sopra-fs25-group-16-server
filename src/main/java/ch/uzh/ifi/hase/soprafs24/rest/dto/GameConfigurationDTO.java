package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.classes.Player;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;

public class GameConfigurationDTO {

    private String host;
    private PlayerDTO[] players;
    private GameType gameType;
    private SupportedLanguages language;

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

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public SupportedLanguages getLanguage() {
        return language;
    }

    public void setLanguage(SupportedLanguages language) {
        this.language = language;
    }
}
