package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;
import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;

import java.util.Optional;

public class Team {
    final TeamColor mColor;
    String mSpymaster;
    String mOperative;

    public Team(TeamColor color) {
        this(color, null, null);
    }

    protected Team(TeamColor color, String spymaster, String operative) {
        this.mColor = color;
        this.mSpymaster = spymaster;
        this.mOperative = operative;
    }

    public Team(Team that) {
        // Because this is Java no variables for simplified syntax is possible (Without creating another Factory ...)
        this(that.getColor(), (that.getSpymaster().isPresent()) ? that.getSpymaster().get() : null, (that.getOperative().isPresent()) ? that.getOperative().get() : null);
    }

    // ATTENTION: this method is potentially unsafe and array might contain null!!!!
    // Therefore manual verification must be handled by caller!
    // Cannot figure out how to create non generic array with Optionals.
    public String[] getMembers() {
        String spymaster = (getSpymaster().isPresent()) ? getSpymaster().get(): null;
        String operative = (getOperative().isPresent()) ? getOperative().get(): null;
        return new String[] {spymaster, operative};
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
}
