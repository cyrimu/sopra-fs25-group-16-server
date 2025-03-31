package ch.uzh.ifi.hase.soprafs24.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs24.classes.Lobby;

@Service
@Transactional
public class LobbyService {
    
    public Lobby getLobby(String LobbyId, String username) {
        // getLobby logic
        System.out.println("Handling getLobby request");


        // PLACEHOLDER OBVIOUSLY HERE WE look for the lobby
        Lobby retrievedLobby = createSampleLobby();

        // verifying Lobby existance
        if (retrievedLobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found with ID: " + LobbyId);
        }

        // verifying user is in the lobby
        if (!retrievedLobby.getPlayers().contains(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not a part of this game");
        }
        return retrievedLobby;
    }

    public static Lobby createSampleLobby() {
        Lobby sampleLobby = new Lobby();
        // Add any example logic or default properties if needed
        return sampleLobby;
    }

}
