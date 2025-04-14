package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.classes.Game;
import ch.uzh.ifi.hase.soprafs24.classes.Player;
import ch.uzh.ifi.hase.soprafs24.classes.Team;
import ch.uzh.ifi.hase.soprafs24.classes.Board;
import ch.uzh.ifi.hase.soprafs24.classes.Card;

import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;
import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;

public class GameDTO {
    String mGameID;
    String mHost;
    Player[] mPlayers;
    GameType mType;
    SupportedLanguages mLanguage;
    Team mBlueTeam;
    Team mRedTeam;
    Card[] mCards;
    PlayerRoles mTurn;
    int mRemainingGuesses;
    TeamColor mWinner;
    String[] mLog;

    public GameDTO(Game game) {
        this.mGameID = game.getGameID();
        this.mHost = game.getHost();
        this.mPlayers = game.getPlayers();
        this.mType = game.getGameType();
        this.mLanguage = game.getLanguage();
        this.mBlueTeam = game.getBlueTeam();
        this.mRedTeam = game.getRedTeam();
        this.mCards = game.getCards();
        this.mTurn = game.getTurn();
        this.mRemainingGuesses = game.getRemainingGuesses();
        this.mWinner = (game.getWinner().isPresent()) ? game.getWinner().get() : null;
        this.mLog = game.getLog();
    }

    public String getGameID() {
        return mGameID;
    }

    public String getHost() {
        return mHost;
    }

    public Player[] getPlayers() {
        return mPlayers;
    }

    public GameType getGameType() {
        return mType;
    }

    public SupportedLanguages getLanguage() {
        return mLanguage;
    }

    public Team getBlueTeam() {
        return mBlueTeam;
    }

    public Team getRedTeam() {
        return mRedTeam;
    }

    public PlayerRoles getTurn() {
        return mTurn;
    }

    public int getRemainingGuesses() {
        return mRemainingGuesses;
    }

    public TeamColor getWinner() {
        return mWinner;
    }

    public String[] getLog() {
        return mLog;
    }
}
