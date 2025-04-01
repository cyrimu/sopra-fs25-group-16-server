package ch.uzh.ifi.hase.soprafs24.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Lobby {
    final UUID mLobbyID;
    final String mHost;

    // started implementing this because I need something so that i can start making the calls 

    public Lobby() {
        this.mLobbyID = UUID.randomUUID();
        this.mHost = "Alice";
    }

    public String getHost() {
        return mHost;
    }

    // this will obviously be replaced
    public List<String> getPlayers() {
        List<String> players = new ArrayList<>();
        players.add("Alice");
        return players;
    }
}
