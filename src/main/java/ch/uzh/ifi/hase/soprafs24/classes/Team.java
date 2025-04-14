package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;
import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;

import java.util.Optional;
import java.lang.IllegalArgumentException;
import java.lang.NullPointerException;

public class Team {
    final TeamColor mColor;
    String mSpymaster;
    String mOperative;

    public Team(TeamColor color) {
        this(color, null, null);
    }

    protected Team(TeamColor color, String spymaster, String operative) throws IllegalArgumentException {
        this.mColor = color;
        this.setSpymaster(spymaster);
        this.setOperative(operative);
    }

    public Team(Team that) {
        // Because this is Java no variables for simplified syntax is possible (Without creating another Factory ...) Also null check via static Method ...
        this(checkIfIsNull(that), (that.getSpymaster().isPresent()) ? that.getSpymaster().get() : null, (that.getOperative().isPresent()) ? that.getOperative().get() : null);
    }

    public TeamColor getColor() {
        return mColor;
    }

    public void setSpymaster(String playerName) throws IllegalArgumentException {
        if (playerName != null && playerName.equals("")) { throw new IllegalArgumentException("Team Class; setSpymaster: Playername cannot be empty");}
        this.mSpymaster = playerName;
    }

    public Optional<String> getSpymaster() {
        return (mSpymaster == null) ? Optional.empty() : Optional.of(mSpymaster);
    }

    public void setOperative(String playerName) throws IllegalArgumentException  {
        if (playerName != null && playerName.equals("")) { throw new IllegalArgumentException("Team Class; setOperative: Playername cannot be empty");}
        this.mOperative = playerName;
    }

    public Optional<String> getOperative() {
        return (mOperative == null) ? Optional.empty() : Optional.of(mOperative);
    }

    // Since Java forces one to call constructor as first expression this monstosity was created.
    private static TeamColor checkIfIsNull (Team t) throws NullPointerException {
        if (t == null) {throw new NullPointerException("Class Team; Copy Constructor: Was used with null vallue");}
        return t.getColor();
    }
}
