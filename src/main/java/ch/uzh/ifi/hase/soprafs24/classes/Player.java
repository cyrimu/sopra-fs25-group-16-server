package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;

import java.util.Optional;
import java.lang.IllegalArgumentException;


public class Player {
    final String mPlayerName;
    PlayerRoles mRole;
    TeamColor mTeam;

    public Player(String name) throws IllegalArgumentException {
        this(name, null);
    }

    public Player(String name, PlayerRoles role) throws IllegalArgumentException {
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException("Playername cannot be empty");
        }
        this.mPlayerName = name;
        this.setRole(role);
    }

    public Player(Player that) {
        // Because this is Java no variables for simplified syntax is possible (Without creating another Factory ...)
        this(that.getPlayerName(),(that.getRole().isPresent()) ? that.getRole().get() : null);
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
}