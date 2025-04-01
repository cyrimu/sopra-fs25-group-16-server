package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.classes.Player;
import ch.uzh.ifi.hase.soprafs24.classes.Team;

import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;
import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;

import java.util.Optional;
import java.util.NoSuchElementException;
import java.lang.NullPointerException;
import java.lang.IllegalArgumentException;
import java.lang.IllegalStateException;
import java.lang.RuntimeException;

public class GameConfigurationDTO {
    private String gameID = null;
    private String host = null;
    private Player[] players = {null, null, null, null};
    private GameType gameType = null;
    private SupportedLanguages language = null;
    private Team blueTeam = new Team(TeamColor.BLUE);
    private Team redTeam = new Team(TeamColor.RED);

    public void addPlayer(Player newPlayer) throws IllegalStateException, NullPointerException, IllegalStateException {
        if (newPlayer == null) {throw new NullPointerException("Class GameConfigurationDTO; addPlayer: Null was provided as Parameter");}
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                players[i] = new Player(newPlayer.getPlayerName());
                return;
            }
            else {
                continue;
            }
        }
        throw new IllegalStateException("Class GameConfigurationDTO; addPlayer: The Maximum number of Players is already reached");
    }

    public void setPlayerRole(String playerName, PlayerRoles role) throws NoSuchElementException, NullPointerException {
        if (playerName == null) {throw new NullPointerException("Class GameConfigurationDTO; setPlayerRole: Null was provided as Parameter Playername");}
        else if (role == null) {throw new NullPointerException("Class GameConfigurationDTO; setPlayerRole: Null was provided as Parameter Role");}
        else if (this.getNamebyRole(role).isPresent()) {
            throw new IllegalStateException("Class GameConfigurationDTO; setPlayerRole: The desired role is already occupied");
        }

        boolean playerFound = false;
        for (Player player : players) {
            if (player == null) {continue;}
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

    private void updateTeamRoleChange(PlayerRoles role, String playerName) {
        switch (role) {
            case BLUE_SPYMASTER:
                blueTeam.setSpymaster(playerName);
                break;
            case BLUE_OPERATIVE:
                blueTeam.setOperative(playerName);
                    break;
            case RED_SPYMASTER:
                redTeam.setSpymaster(playerName);
                break;
            case RED_OPERATIVE:
                redTeam.setOperative(playerName);
                break;
        }
    }

    public Optional<PlayerRoles> getRolebyName (String playerName) {
        Optional<PlayerRoles> opt = Optional.empty();
        for (Player player : players) {
            if (playerName.equals(player.getPlayerName())) {
                if (player.getRole().isPresent()){
                    opt = Optional.of(player.getRole().get());
                    break;
                }
            }
        }
        return opt;
    }

    public Optional<String> getNamebyRole (PlayerRoles role) {
        Optional<String> opt = Optional.empty();
        for (Player player : players) {
            if (player.getRole().isPresent()) {
                if (role == player.getRole().get()) {
                    opt = Optional.of(player.getPlayerName());
                    break;
                }
            }
        }
        return opt;
    }

    public String getID() {
        return gameID;
    }

    public void setID(String gameID) {
        this.gameID = gameID;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Player[] getPlayers() {
        Player a = (players[0] == null) ? null : new Player(players[0]);
        Player b = (players[1] == null) ? null : new Player(players[1]);
        Player c = (players[2] == null) ? null : new Player(players[2]);
        Player d = (players[3] == null) ? null : new Player(players[3]);
        // String aName = (a != null) ? a.getPlayerName() : null;
        // String bName = (b != null) ? b.getPlayerName() : null;
        // String cName = (c != null) ? c.getPlayerName() : null;
        // String dName = (d != null) ? d.getPlayerName() : null;
        // if (1==1){throw new IllegalStateException(String.format("%s | %s | %s | %s", aName, bName, cName, dName));}
        return new Player[] {a, b, c, d};
    }

    public GameType getType() {
        return gameType;
    }

    public void setType(GameType type) {
        this.gameType = type;
    }

    public SupportedLanguages getLanguage() {
        return language;
    }

    public void setLanguage(SupportedLanguages language) {
        this.language = language;
    }
}
