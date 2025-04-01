package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.classes.Lobby;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
public class RestLobbyController {

    private final LobbyService lobbyService;

    public RestLobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }
    
    @GetMapping("/lobby/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Lobby getLobby(@PathVariable String lobbyId, String username) {
        
        // username check
        if (username == null || username.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }

        // Fetch the Lobby
        Lobby retrievedLobby = lobbyService.getLobby(lobbyId, username);
        
        return retrievedLobby;
    }
    
}
