package ch.uzh.ifi.hase.soprafs24.classes;

public class OldGame {
    private String gameId;
    private String username;

    public OldGame(String gameId, String username) {
        this.gameId = gameId;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) { this.gameId = gameId; }

    public void setUsername(String username) {
        this.username = username;
    }
}
