package ch.uzh.ifi.hase.soprafs24.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.uzh.ifi.hase.soprafs24.classes.Player;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;

public class Lobby {
    public static final int ID_LENGTH = 8;

    final String mLobbyID;
    final String mHost;
    Player[] mPlayers;
    GameType mType;
    SupportedLanguages mLanguage;
    Game mCurrentGame;

    // started implementing this because I need something so that i can start making the calls 

    public Lobby(String host, Player[] players, GameType type, SupportedLanguages language) {
        this.mLobbyID = UUID.randomUUID().toString().substring(0,ID_LENGTH);
        this.mHost = host;
        this.mPlayers = players;
        this.mType = type;
        this.mLanguage = language;
        this.mCurrentGame = null;
    }

    public String getLobbyID() {
        return mLobbyID;
    }

    public String getHost() {
        return mHost;
    }

    public GameType getGameType() {
        return mType;
    }

    public SupportedLanguages getLanguage() {
        return mLanguage;
    }

    public Player[] getPlayers() {
        return new Player[] {new Player(mPlayers[0]), new Player(mPlayers[1]), new Player(mPlayers[2]), new Player(mPlayers[3])};
    }

    public Game getCurrentGame() {
        return mCurrentGame;
    }

    // set this when we start a game from this lobby
    public void setCurrentGame(Game game) {
        this.mCurrentGame = game;
    }

    public int getPlayerCount() {
        int count = 0;
        for (Player p : mPlayers) {
            if (p != null) {
                count++;
            }
        }
        return count;
    }
    
}
