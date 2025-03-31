package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.classes.Player;
import ch.uzh.ifi.hase.soprafs24.classes.Team;
import ch.uzh.ifi.hase.soprafs24.classes.Board;

import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;
import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.ArrayList;
import java.util.Optional;
import java.util.NoSuchElementException;


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
        this.mGameID = Integer.parseInt(UUID.randomUUID().toString().substring(0,8)); // Shortens UUID for convenience and enables storage as int
        assert !(host.equals("")) : "Hostname cannot be empty";
        this.mHost = host;
        assert players.length == 4 : "The number of Players is incorrect";
        this.mPlayers = new Player[] {new Player(players[0]), new Player(players[1]), new Player(players[2]), new Player(players[3])};
        this.mType = type;
        this.mLanguage = language;
        this.mBlueTeam = new Team(blueTeam);
        this.mRedTeam = new Team(redTeam);
        this.mFirstTeam = TeamColor.BLUE;
        this.mBoard = new Board(type, this.mFirstTeam, this.mLanguage);
        this.mTurn = PlayerRoles.BLUE_SPYMASTER;
        this.mRemainingGuesses = 0;
        this.mWinner = Optional.empty();
        this.mLog = new ArrayList<String>();
    }

    public void setPlayerRole(String playerName, PlayerRoles role) throws NoSuchElementException {
        boolean playerFound = false;
        for (Player player : mPlayers){
            if (player.getPlayerName().equals(playerName)){
                playerFound = true;
                Optional<PlayerRoles> oldRole = player.getRole();

                if (oldRole.isPresent()) {
                    updateTeamRoleChange(oldRole.get(), null);
                }

                player.setRole(role);
                updateTeamRoleChange(role, playerName);
                break;
            }
        }

        if (!playerFound) {throw new NoSuchElementException("No Player with given playerName found!"); }
    }

    public Optional<PlayerRoles> getRolebyName (String playerName) {
        Optional<PlayerRoles> opt = Optional.empty();
        for (Player player : mPlayers) {
            if (playerName.equals(player.getPlayerName())) {
                if (player.getRole().isPresent())
                    opt = Optional.of(player.getRole().get());
                break;
            }
        }
        return opt;
    }

    public Optional<String> getNamebyRole (PlayerRoles role) {
        Optional<String> opt = Optional.empty();

        for (Player player : mPlayers) {
            if (role.equals(player.getRole())) {
                opt = Optional.of(player.getPlayerName());
                break;
            }
        }
        return opt;
    }

    private void logOperativeTurn(PlayerRoles role, String word) {
        String logMessage = "%s guessed the following word: %s";
        String playerName = getNamebyRole(role).get();
        mLog.add(String.format(logMessage, playerName, word));
    }

    private void logSpyMasterTurn(PlayerRoles role, String word) {
        String logMessage = "%s provided the following Hint: %s";
        String playerName = getNamebyRole(role).get();
        mLog.add(String.format(logMessage, playerName, word));
    }

    public String[] getLog() {
        String[] log = new String[mLog.size()];
        log = mLog.toArray(log);
        return log;
    }

    public int getGameID() {
        return mGameID;
    }

    public String getHost() {
        return mHost;
    }

    public void setPlayers(Player[] players) {
        assert players.length == 4 : "The number of Players is incorrect";
        this.mPlayers = new Player[] {new Player(players[0]), new Player(players[1]), new Player(players[2]), new Player(players[3])};
    }

    public Player[] getPlayers() {
        return new Player[] {new Player(mPlayers[0]), new Player(mPlayers[1]), new Player(mPlayers[2]), new Player(mPlayers[3])};
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
            case BLUE_SPYMASTER:
                mBlueTeam.setSpymaster(playerName);
                break;
            case BLUE_OPERATIVE:
                mBlueTeam.setOperative(playerName);
                    break;
            case RED_SPYMASTER:
                mRedTeam.setSpymaster(playerName);
                break;
            case RED_OPERATIVE:
                mRedTeam.setOperative(playerName);
                break;
        }
    }
}