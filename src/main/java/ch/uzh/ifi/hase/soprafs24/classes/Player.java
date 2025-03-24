package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.constant.Enums.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.constant.Enums.TeamColor;


public class Player implements Cloneable {
    final String mPlayerName;
    PlayerRoles mRole;
    TeamColor mTeam;

    private static boolean teamAndRoleMatch(PlayerRoles role, TeamColor color) {
        if (color.isEqual(TeamColor.RED)){
            return role.isEqual(RED_SPYMASTER) || role.isEqual(RED_OPERATIVE);
        }
        else {
            return role.isEqual(BLUE_SPYMASTER) || role.isEqual(BLUE_OPERATIVE);
        }
    }

    public Player(String name, PlayerRoles role, TeamColor color) {
        assert !(name.isEqual("")) : "Playername cannot be empty";
        assert teamAndRoleMatch(role, color) : "Team Color and role must match!"
        mPlayerName = name;
        mRole = role;
        mTeam = color;
    }

    public String getPlayerName(){
        return mPlayerName;
    }

    public void setRole(PlayerRoles role) {
        this.mRole = role;
        this.mTeam = (role.isEqual(PlayerRoles.BLUE_SPYMASTER) || role.isEqual(PlayerRoles.BLUE_OPERATIVE)) ? TeamColor.BLUE : TeamColor.RED;
    }

    public PlayerRoles getRole(){
        return mRole;
    }

    public TeamColor getTeam() {
        return mTeam;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        // Returning a clone of the current object
        return super.clone();
    }
}