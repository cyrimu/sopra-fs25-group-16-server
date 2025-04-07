package ch.uzh.ifi.hase.soprafs24.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs24.classes.Game;
import ch.uzh.ifi.hase.soprafs24.classes.Lobby;
import ch.uzh.ifi.hase.soprafs24.classes.Player;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;

@Service
@Transactional
public class LobbyService {
    GameService gameService = new GameService();
    
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
        // seems like we can remove this actually its not very useful and would simplify the other implementation
        if (java.util.Arrays.stream(retrievedLobby.getPlayers()).noneMatch(player -> player.getPlayerName().equals(username))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not a part of this game");
        }
        return retrievedLobby;
    }

    public Lobby createLobby(String username) {
        System.out.println("Creating lobby for user: " + username);
        
        // For now, just use the sample lobby method
        Lobby newLobby = createSampleLobby();
              
        return newLobby;
    }

    public Lobby updateLobby(String lobbyId, Lobby lobby, String username) {
        System.out.println("Handling updateLobby request for lobby: " + lobbyId + " by user: " + username);
        


        // Real implementation coming next week

        Lobby newLobby = createSampleLobby();
        
        // this line of code simualtes what happens when the game is already being played
        // newLobby.setCurrentGame(gameService.createSampleGame());

        if (newLobby.getCurrentGame() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, 
                "Cannot update lobby that has a game in progress");
        }
        
        return newLobby;
    }

    public Lobby joinLobby(String lobbyId, String username) {
        System.out.println("Handling joinLobby request for lobby: " + lobbyId + " by user: " + username);
        
        Lobby lobby = retrieveLobby(lobbyId, username);
        Player newPlayer = new Player(username);

        // checking if the game already has 4 players (exact implementation up to debate next week)

        //if (lobby.getPlayerCount() >= 4) {
        //    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
        //        "Cannot join lobby because it is already full");
        //}

        // Check if player with same username is already in the lobby
        if (java.util.Arrays.stream(lobby.getPlayers())
            .filter(player -> player != null)
            .anyMatch(player -> player.getPlayerName().equals(username))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, 
                "A player with this username is already in the lobby");
        }


        // checking if game in progress
        if (lobby.getCurrentGame() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, 
                "Cannot join lobby with a game in progress");
        }
        
        return lobby;
    }

    public Lobby leaveLobby(String lobbyId, String username) {
        System.out.println("Handling leaveLobby request for lobby: " + lobbyId + " by user: " + username);
        
        // retrieve lobby
        Lobby lobby = retrieveLobby(lobbyId, username);
        
        // progress check
        // toggle this on and off to test the live game
        // lobby.setCurrentGame(gameService.createSampleGame());
        if (lobby.getCurrentGame() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, 
                "Cannot leave lobby with a game in progress");
        }
        
        // for now we are just returning a sample lobby, exact implementation up to debate later
        return createSampleLobby();
    }

    private Lobby retrieveLobby(String lobbyId, String username) {
        System.out.println("Retrieving lobby: " + lobbyId);
        
        // For now, just return a sample lobby
        Lobby lobby = createSampleLobby();
        
        if (lobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Lobby not found with ID: " + lobbyId);
        }
        
        return lobby;
    }

    private static Lobby createSampleLobby() {
        Player p1 = new Player("Alice", PlayerRoles.BLUE_SPYMASTER);
        Player p2 = new Player("Bob", PlayerRoles.BLUE_OPERATIVE);
        Player p3 = new Player("Carol", PlayerRoles.RED_SPYMASTER);
        Player p4 = new Player("David", PlayerRoles.RED_OPERATIVE);
        Player[] players = new Player[] { p1, p2, p3, p4 };

        // Build the new game with example values
        Lobby newLobby = new Lobby(
            "Alice", 
            players, 
            GameType.TEXT, 
            SupportedLanguages.ENGLISH
        );
        return newLobby;
    }

}
