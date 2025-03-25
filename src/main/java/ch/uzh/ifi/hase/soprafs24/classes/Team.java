package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;
import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;

import java.util.Optional;

public class Team implements Cloneable {
    final TeamColor mColor;
    String mSpymaster;
    String mOperative;

    public Team(TeamColor color) {
        this.mColor = color;
        this.mSpymaster = null;
        this.mOperative = null;
    }

    public String[] getMembers() {
        return new String[]{mSpymaster, mOperative};
    }

    public TeamColor getColor() {
        return mColor;
    }

    public void setSpymaster(String playerName) {
        assert !(playerName.equals("")) : "Playername cannot be empty";
        this.mSpymaster = playerName;
    }

    public Optional<String> getSpymaster() {
        return (mSpymaster == null) ? Optional.empty() : Optional.of(mSpymaster);
    }

    public void setOperative(String playerName) {
        assert !(playerName.equals("")) : "Playername cannot be empty";
        this.mOperative = playerName;
    }

    public Optional<String> getOperative() {
        return (mOperative == null) ? Optional.empty() : Optional.of(mOperative);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        // Returning a clone of the current object
        return super.clone(); 
    }
}
