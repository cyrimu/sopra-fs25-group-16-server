package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.classes.Player;
import ch.uzh.ifi.hase.soprafs24.classes.Team;
import ch.uzh.ifi.hase.soprafs24.classes.Board;
import ch.uzh.ifi.hase.soprafs24.constant.Enums.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.Enums.SupportedLanguages;
import ch.uzh.ifi.hase.soprafs24.constant.Enums.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.constant.Enums.TeamColor;


import java.util.UUID;
import java.util.ArrayList;


public class Game {
    final int mGameID;
    final String mHost;
    Player[] mPlayers;
    GameType mType;
    SupportedLanguages mLanguage;
    Team mBlueTeam;
    Team mRedTeam;
    Board mBoard;
    PlayerRoles mTurn;
    TeamColor mFirstTeam;
    int mRemainingGuesses;
    Optional<TeamColor> mWinner;
    ArrayList<String> mLog;

    public Game(String host, Player[] players, GameType type, SupportedLanguages language, Team blueTeam, Team redTeam) {
        this.mGameID = UUID.randomUUID();
        assert !(host.isEqual("")) : "Hostname cannot be empty";
        this.mHost = host;
        assert players.length.isEqual(4) : "The number of Players is incorrect";
        this.mPlayers = {players[0].clone(), players[1].clone(), players[2].clone(), players[3].clone()};
        this.mType = type;
        this.mLanguage = language;
        this.mBlueTeam = blueTeam.clone();
        this.mRedTeam = redTeam.clone();
        this.mFirstTeam = TeamColor.BLUE;
        this.mBoard = Board(this.mFirstTeam);
        this.mTurn = PlayerRoles.BLUE_SPYMASTER;
        this.mRemainingGuesses = 0;
        this.mWinner = Optional.empty();
        this.mLog = new ArrayList<String>();
    }

    public void setPlayerRole(String playerName, PlayerRoles role) {
        boolean playerFound = false;
        for (Player player : mPlayers){
            if (player.getPlayerName().isEqual(playerName)){
                playerFound = true;
                PlayerRoles oldRole = player.getRole();

                updateTeamRoleChange(oldRole, "");
                player.setRole(role);
                updateTeamRoleChange(role, playerName);
                break;
            }
        }

        if (!playerFound) {throw new Exception("No Player with given playerName found!")}
    }

    public Optional<PlayerRoles> getRolebyName (String playerName) {
        Optional<PlayerRoles> opt = Optional.empty();

        for (Player player : mPlayers) {
            if (playerName.isEqual(player.getPlayerName())) {
                opt = Optional.of(player.getRole());
                break;
            }
        }
        return opt;
    }

    public Optional<String> getNamebyRole (PlayerRoles role) {
        Optional<String> opt = Optional.empty();

        for (Player player : mPlayers) {
            if (role.isEqual(player.getRole())) {
                opt = Optional.of(player.getPlayerName());
                break;
            }
        }
        return opt;
    }

    private void logOperativeTurn(PlayerRoles role, String word) {
        logMessage = "%s guessed the following word: %s"
        String playerName = getNamebyRole(role).get();
        mLog.add(String.format(logMessage, playerName, word));
    }

    private void logSpyMasterTurn(PlayerRoles role, String word) {
        logMessage = "%s provided the following Hint: %s"
        String playerName = getNamebyRole(role).get();
        mLog.add(String.format(logMessage, playerName, word));
    }

    public String[] getLog() {
        String[] log = mLog.toArray();
        return log;
    }

    public int getGameID() {
        return mGameID;
    }

    public String getHost() {
        return mHost;
    }

    public void setPlayers(Players[] players) {
        assert players.length.isEqual(4) : "The number of Players is incorrect";
        this.mPlayers = {players[0].clone(), players[1].clone(), players[2].clone(), players[3].clone()};
    }

    public Players[] getPlayers() {
        return {players[0].clone(), players[1].clone(), players[2].clone(), players[3].clone()};
    }

    public void setGameType(GameType type) {
        this.mType = type;
    }

    public GameType getGameType() {
        return mType;
    }

    public void setLanguage(SupportedLanguages language) {
        this.mLanguage = language;
    }

    public SupportedLanguages getLanguage() {
        return mLanguage;
    }

    public void setFirstTeam(TeamColor team) {
        this.mFirstTeam = team;
    }

    public TeamColor getFirstTeam() {
        return mFirstTeam;
    }

    public PlayerRoles getTurn() {
        return mTurn;
    }

    public int getRemainingGuesses() {
        return mRemainingGuesses;
    }

    public Optional<TeamColor> getWinner() {
        return mWinner;
    }

    private void updateTeamRoleChange(PlayerRoles role, String playerName) {
        switch (role) {
            case PlayerRoles.BLUE_SPYMASTER:
                mBlueTeam.setSpymaster("");
                break;
            case PlayerRoles.BLUE_OPERATIVE:
                mBlueTeam.setOperative("");
                    break;
            case PlayerRoles.RED_SPYMASTER:
                mRedTeam.setSpymaster("");
                break;
            case PlayerRoles.BLUE_OPERATIVE:
                mRedTeam.setOperative("");
                break;
        }
    }
}