package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;

public class PlayerDTO {
    private String playerName;
    private PlayerRoles role;
    private TeamColor team;
    
    // Default constructor
    public PlayerDTO() {}

    // Constructor with playerName
    public PlayerDTO(String playerName) {
        this.playerName = playerName;
    }

    public PlayerDTO(String playerName, PlayerRoles role) {
        this.playerName = playerName;
        this.role = role;
    }
    
    
    // Getters and setters
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    
    public PlayerRoles getRole() { return role; }
    public void setRole(PlayerRoles role) { this.role = role; }
    
    public TeamColor getTeam() { return team; }
    public void setTeam(TeamColor team) { this.team = team; }
}