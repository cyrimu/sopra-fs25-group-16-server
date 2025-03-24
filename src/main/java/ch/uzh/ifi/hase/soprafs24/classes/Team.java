package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.constant.Enums.TeamColor;
import ch.uzh.ifi.hase.soprafs24.constant.Enums.PlayerRoles;

public class Team implements Cloneable {
    final TeamColor mColor;
    String mSpymaster;
    String mOperative;

    public Team(TeamColor color) {
        this.mColor = color;
        this.mSpymaster = "";
        this.mOperative = "";
    }

    public String[] getMembers() {
        return {mSpymaster, mOperative};
    }

    public TeamColor getColor() {
        return mColor;
    }

    public void setSpymaster(String playerName) {
        this.mSpymaster = playerName;
    }

    public String getSpymaster() {
        return mSpymaster;
    }

    public void setOperative(String playerName) {
        this.mOperative = playerName;
    }

    public String getOperative() {
        return mOperative;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        // Returning a clone of the current object
        return super.clone(); 
    }
}
