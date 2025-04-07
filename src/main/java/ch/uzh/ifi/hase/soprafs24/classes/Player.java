package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;

import java.util.Optional;
import java.lang.IllegalArgumentException;
import java.lang.NullPointerException;

public class Player {
    final String mPlayerName;
    PlayerRoles mRole;
    TeamColor mTeam;


    public Player(String name) throws IllegalArgumentException {
        this(name, null);
    }

    public Player(String name, PlayerRoles role) throws IllegalArgumentException, NullPointerException {
        if (name == null) {throw new NullPointerException("Class Player; Player Constructor: Playername cannot be null");}
        if (name.equals("")) {throw new IllegalArgumentException("Class Player; Player Constructor: Playername cannot be empty");}
        this.mPlayerName = name;
        this.setRole(role);
    }

    public Player(Player that) {
        // Because this is Java no variables for simplified syntax is possible (Without creating another Factory ...) Also null check via static Method ...
        this(checkIfIsNull(that),(that.getRole().isPresent()) ? that.getRole().get() : null);
    }

    public String getPlayerName(){
        return mPlayerName;
    }

    public void setRole(PlayerRoles role) {
        this.mRole = role;
        if (role != null) {
            this.mTeam = (role == PlayerRoles.BLUE_SPYMASTER || role == PlayerRoles.BLUE_OPERATIVE) ? TeamColor.BLUE : TeamColor.RED;
        }
        else {
            this.mTeam = null;
        }
    }

    public Optional<PlayerRoles> getRole() {
        return (mRole == null) ? Optional.empty() : Optional.of(mRole);
    }

    public Optional<TeamColor> getTeam() {
        return (mRole == null) ? Optional.empty() : Optional.of(mTeam);
    }

    // Since Java forces one to call constructor as first expression this monstosity was created.
    private static String checkIfIsNull (Player p) throws NullPointerException {
        if (p == null) {throw new NullPointerException("Class Player; Copy Constructor: Was used with null vallue");}
        return p.getPlayerName();
    }
}