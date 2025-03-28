package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;

import java.util.Optional;


public class Player implements Cloneable {
    final String mPlayerName;
    PlayerRoles mRole;
    TeamColor mTeam;

    private boolean teamAndRoleMatch(PlayerRoles role, TeamColor color) {
        if (mRole == null || mTeam == null ) {
            return true;
        }

        if (color.equals(TeamColor.RED)){
            return role == PlayerRoles.RED_SPYMASTER || role == PlayerRoles.RED_OPERATIVE;
        }
        else {
            return role == PlayerRoles.BLUE_SPYMASTER || role == PlayerRoles.BLUE_OPERATIVE;
        }
    }

    public Player(String name, PlayerRoles role, TeamColor color) {
        assert !(name.equals("")) && name != null : "Playername cannot be empty";
        assert teamAndRoleMatch(role, color) : "Team Color and role must match!";
        this.mPlayerName = name;
        this.mRole = role;
        this.mTeam = color;
    }

    public Player (String name) {
        assert !(name.equals("")) && name != null : "Playername cannot be empty";
        this.mPlayerName = name;
        this.mRole = null;
        this.mTeam = null;
    }

    public String getPlayerName(){
        return mPlayerName;
    }

    public void setRole(PlayerRoles role) {
        this.mRole = role;
        this.mTeam = (role == PlayerRoles.BLUE_SPYMASTER || role == PlayerRoles.BLUE_OPERATIVE) ? TeamColor.BLUE : TeamColor.RED;
    }

    public Optional<PlayerRoles> getRole() {
        return (mRole == null) ? Optional.empty() : Optional.of(mRole);
    }

    public Optional<TeamColor> getTeam() {
        return (mRole == null) ? Optional.empty() : Optional.of(mTeam);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        // Returning a clone of the current object
        return super.clone();
    }
}