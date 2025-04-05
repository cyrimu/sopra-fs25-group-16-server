package ch.uzh.ifi.hase.soprafs24.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs24.classes.Clue;
import ch.uzh.ifi.hase.soprafs24.classes.Game;
import ch.uzh.ifi.hase.soprafs24.classes.Player;
import ch.uzh.ifi.hase.soprafs24.classes.Team;

import ch.uzh.ifi.hase.soprafs24.rest.dto.GameConfigurationDTO;

import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;

@Service
@Transactional
public class GameService {
    // this is the class that is the Game worker.
    // program the game logic here.
    // this is where the controller endpoints and internal game representation meets
    public Game handleClue(Clue clue) {
        // handleClue logic
        System.out.println("Handling clue");

        // creating a new game as placeholder
        Game currentGame = createSampleGame();
        return currentGame;
    }

    public Game getGame(String gameId, String username) {
        // getGame logic
        System.out.println("Handling getGame request");
        // should we pass a UUID or is gameId fine? (question for the frontend)

        // creating a new game as placeholder (usually we would use gameId here to retrieve the game from storage)
        Game retrievedGame = createSampleGame();

        // verifying game existance
        if (retrievedGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found with ID: " + gameId);
        }

        // verifying user is host
        if (!retrievedGame.getHost().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not host of this game");
        }
        return retrievedGame;
    }

    // Created sample object feel free to modify GameConfigurationDTO however you want (i.e. instantiate it in Lobby and Lobby uses GmaeService etc.)
    public Game createGame(GameConfigurationDTO newGameData) {
        System.out.println("Handling createGame request");
        System.out.println(newGameData);
        // creating game placeholder for API development

        Game newGame = createSampleGame();
        
        return newGame;
    }

    // helper function for creating the API during development (will be removed later)
    public Game createSampleGame() {
        Player p1 = new Player("Alice", PlayerRoles.BLUE_SPYMASTER);
        Player p2 = new Player("Bob", PlayerRoles.BLUE_OPERATIVE);
        Player p3 = new Player("Carol", PlayerRoles.RED_SPYMASTER);
        Player p4 = new Player("David", PlayerRoles.RED_OPERATIVE);
        Player[] players = new Player[] { p1, p2, p3, p4 };

        // Build the new game with example values
        Game newGame = new Game(
            "Alice", 
            players, 
            GameType.TEXT, 
            SupportedLanguages.ENGLISH
        );

        return newGame;
    }
}
