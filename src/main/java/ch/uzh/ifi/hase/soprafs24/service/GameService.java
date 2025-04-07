package ch.uzh.ifi.hase.soprafs24.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs24.classes.Clue;
import ch.uzh.ifi.hase.soprafs24.classes.Game;
import ch.uzh.ifi.hase.soprafs24.classes.Card;
import ch.uzh.ifi.hase.soprafs24.classes.GameConfiguration;
import ch.uzh.ifi.hase.soprafs24.classes.Player;
import ch.uzh.ifi.hase.soprafs24.classes.Team;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;

import java.util.Optional;

import com.google.gson.Gson;

@Service
@Transactional
public class GameService {
    static final Gson gson = new Gson();
    // this is the class that is the Game worker.
    // program the game logic here.
    // this is where the controller endpoints and internal game representation meets
    public Game handleClue(Clue clue) {
        // handleClue logic
        System.out.println("Handling clue");

        String username = clue.getUsername(); 
        String clueMessage = clue.getClueText();
        int guesses = clue.getClueNumber().intValue();

        // creating a new game as placeholder
        Game currentGame = createSampleGame();

        //Bad Request need to be handled diffrently since Websocket communication. Just providing Error flow.
        Optional<PlayerRoles> playerRole = currentGame.getRolebyName(username);
        if (!playerRole.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user with the given username participates in the game");
        }

        if (playerRole.get() == PlayerRoles.BLUE_OPERATIVE || playerRole.get() == PlayerRoles.RED_OPERATIVE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only Spymaster can provide clues");
        }

        if (playerRole.get() != currentGame.getTurn()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It is not yet the given users turn");
        }

        if (currentGame.getBoardSize() < guesses) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only maximally the whole Boardsize can be used for the Guessnumber");
        }
        
        Card[] cards = currentGame.getCards();

        boolean isValidClue = true;

        // Verification that clue Message not a Word on Board -
        for (Card card : cards) {
            if (card.getType() == GameType.TEXT) {
                // banal check because otherwise either too restrictive or expansive without complicated testing procedure
                if (((String) card.getContent()).equals(clueMessage)) {
                    // Probably we should notify user somehow that illegal word was used
                    currentGame.increaseTurn(2);
                    isValidClue = false;
                }
            }
            else { break;}
        }

        if (isValidClue){
            currentGame.increaseTurn(1);
            currentGame.setRemainingGuesses(guesses);
        }

        String logMessage = String.format("%s provided the Clue: %s : %d", username, clueMessage, guesses);
        currentGame.logTurn(logMessage);

        // TODO: Update Database with info
        
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

    // Created sample object feel free to modify GameConfiguration however you want (i.e. instantiate it in Lobby and Lobby uses GmaeService etc.)
    public Game createGame(GameConfiguration gameConfiguration) {
        System.out.println("Handling createGame request");
        
        Game newGame = new Game(gameConfiguration);

        //Store in database
        
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
