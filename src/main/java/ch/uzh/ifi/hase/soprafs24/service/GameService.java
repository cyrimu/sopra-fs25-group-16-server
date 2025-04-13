package ch.uzh.ifi.hase.soprafs24.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs24.classes.Clue;
import ch.uzh.ifi.hase.soprafs24.classes.Guess;
import ch.uzh.ifi.hase.soprafs24.classes.InMemoryStore;
import ch.uzh.ifi.hase.soprafs24.classes.Game;
import ch.uzh.ifi.hase.soprafs24.classes.Card;
import ch.uzh.ifi.hase.soprafs24.classes.GameConfiguration;
import ch.uzh.ifi.hase.soprafs24.classes.Player;
import ch.uzh.ifi.hase.soprafs24.classes.Team;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.CardColor;
import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;
import ch.uzh.ifi.hase.soprafs24.deserializer.CardAdapter;



import java.util.Optional;

import com.google.gson.Gson; 
import com.google.gson.GsonBuilder; 
import com.google.gson.TypeAdapter; 
import com.google.gson.Gson;

import com.deepl.api.*;

@Service
@Transactional
public class GameService {
    private static final GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Card.class, new CardAdapter());  
    private static final Gson gson = builder.create();
    String connectionString = "mongodb+srv://<codenamesClient>:<codenamesClient>@codenamesdbcluser.dh3iiy8.mongodb.net/?appName=CodenamesDBCluser";

    public static Game createGame(GameConfiguration gameConfig) {
        System.out.println("Handling createGame request");
        
        Game newGame = new Game(gameConfig);
        String json = gson.toJson(newGame);

        //if (1==1) {
        //    throw new ResponseStatusException(HttpStatus.NOT_FOUND, json);
        //}

        // String json = gson.toJson(newGame);
        // Store in database somehow
        InMemoryStore.putGame(newGame.getGameID(), newGame);

        return newGame;
    }

    private static Game loadFromDatabase(String gameId) {

        // String json = {Insert MongoDB query}
        // Game loadedGame = gson.fromJson(json, Game.class);

        Game game = createSampleGame(0);
        InMemoryStore.putGame(game.getGameID(), game);

        return game;
    }

    public static Game retrieveGame(String gameId, String username) {
        System.out.println("Handling getGame request for game: " + gameId); 
        
        Game retrievedGame = InMemoryStore.getGame(gameId);
        if (retrievedGame == null) {
            retrievedGame = loadFromDatabase(gameId);
        }

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

    // this is the class that is the Game worker.
    // program the game logic here.
    // this is where the controller endpoints and internal game representation meets
    public Game handleClue(String gameId, Clue clue) {
        System.out.println("Handling clue");

        String username = clue.getUsername(); 
        String clueMessage = clue.getClueText();
        int guesses = clue.getClueNumber().intValue();
        
        // retrieving the game from the InMemoryStore or database
        Game currentGame = InMemoryStore.getGame(gameId);
        if (currentGame == null) {
            currentGame = loadFromDatabase(gameId);
        }
        if (currentGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found with ID: " + gameId);
        }

        verifyAction(currentGame, username);

        Optional<PlayerRoles> playerRole = currentGame.getRolebyName(username);

        if (playerRole.get() == PlayerRoles.BLUE_OPERATIVE || playerRole.get() == PlayerRoles.RED_OPERATIVE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only Spymaster can provide clues");
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
                    currentGame.setRemainingGuesses(0);
                    isValidClue = false;
                }
            }
            else { break;}
        }

        if (isValidClue){
            currentGame.increaseTurn(1);
            currentGame.setRemainingGuesses(guesses+1);
        }

        String logMessage = String.format("%s provided the Clue: %s : %d", username, clueMessage, guesses);
        currentGame.logTurn(logMessage);

        // TODO: Update Database with info
        InMemoryStore.putGame(currentGame.getGameID(), currentGame);
        
        return currentGame;
    }

    public Game handleGuess(String gameId, Guess guess) {
        // handleClue logic
        System.out.println("Handling guess");

        String username = guess.getUsername(); 
        int cardIndex = guess.getCardNumber();

        // retrieving the game from the InMemoryStore or database
        Game currentGame = InMemoryStore.getGame(gameId);
        if (currentGame == null) {
            currentGame = loadFromDatabase(gameId);
        }
        if (currentGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found with ID: " + gameId);
        }

        verifyAction(currentGame, username);

        Optional<PlayerRoles> playerRole = currentGame.getRolebyName(username);

        //Bad Request need to be handled diffrently since Websocket communication. Just providing Error flow.
        if (playerRole.get() == PlayerRoles.BLUE_SPYMASTER || playerRole.get() == PlayerRoles.RED_SPYMASTER) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only Operative can provide guesses");
        }

        if (currentGame.getBoardSize() <= cardIndex || 0 > cardIndex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Guesses can only have values 0 to %d", currentGame.getBoardSize()-1));
        }
        
        Card[] cards = currentGame.getCards();
        Card guessedCard = cards[cardIndex];

        currentGame.revealCard(cardIndex);

        TeamColor playerColor = (playerRole.get() == PlayerRoles.RED_OPERATIVE) ? TeamColor.RED : TeamColor.BLUE;
        int remainingGuesses = currentGame.getRemainingGuesses();

        // Update Game State dependent on Type of revealed Card
        switch (guessedCard.getColor()){
            case WHITE:
                currentGame.increaseTurn(1);
                currentGame.setRemainingGuesses(0);
                break;

            case BLACK:
                currentGame.setRemainingGuesses(0);
                currentGame.setWinner((playerColor == TeamColor.RED) ? TeamColor.BLUE : TeamColor.RED);
                break;

            case RED:
                if (playerColor == TeamColor.RED) {
                    currentGame.setRemainingGuesses(remainingGuesses-1);
                    if (remainingGuesses-1 == 0) {
                        currentGame.increaseTurn(1);
                    }
                }
                else {
                    currentGame.setRemainingGuesses(0);
                    currentGame.increaseTurn(1);
                }
                break;
                
            case BLUE:
                if (playerColor == TeamColor.BLUE) {
                    currentGame.setRemainingGuesses(remainingGuesses-1);
                    if (remainingGuesses-1 == 0) {
                        currentGame.increaseTurn(1);
                    }
                }
                else {
                    currentGame.setRemainingGuesses(0);
                    currentGame.increaseTurn(1);
                }
                break;
        }

        // Check if either Player has won
        int countUnrevealedRedCards = 0;
        int countUnrevealedBlueCards = 0;
        for (Card card : cards) {
            if (card.getColor() == CardColor.RED) {
                if (card.getIsRevealed() == false) {
                    countUnrevealedRedCards += 1;
                }
            }
            else if (card.getColor() == CardColor.BLUE) {
                if (card.getIsRevealed() == false) {
                    countUnrevealedBlueCards += 1;
                }
            }
        }
        if (countUnrevealedRedCards == 0) {currentGame.setWinner(TeamColor.RED);}
        else if (countUnrevealedBlueCards == 0) {currentGame.setWinner(TeamColor.BLUE);}

        //Log Turn
        String guessMessage = "";
        if (guessedCard.getType() == GameType.TEXT){
            guessMessage = (String) guessedCard.getContent();
        }

        //Was used for debugging will be deleted later when proper tests can be written
        // String debug = String.format("Role:%s | Color:%s | Bool:%s | Winner:%s | Turn:%s | Guesses:%s", playerRole.get(), guessedCard.getColor(), currentGame.getCards()[cardIndex].getIsRevealed(), currentGame.getWinner().isPresent(), currentGame.getTurn(), currentGame.getRemainingGuesses());

        // if (1==1) {
        //     throw new ResponseStatusException(HttpStatus.BAD_REQUEST, debug);
        // }

        String logMessage = String.format("%s made the guess: %s", username, guessMessage);
        currentGame.logTurn(logMessage);

        // TODO: Update Database with info
        InMemoryStore.putGame(currentGame.getGameID(), currentGame);
        
        return currentGame;
    }

    public Game handleSkip(String gameId, String username) {
        // handle Skip logic
        System.out.println("Handling skip Turn");

        // retrieving the game from the InMemoryStore or database
        Game currentGame = InMemoryStore.getGame(gameId);
        if (currentGame == null) {
            currentGame = loadFromDatabase(gameId);
        }
        if (currentGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found with ID: " + gameId);
        }

        verifyAction(currentGame, username);

        Optional<PlayerRoles> playerRole = currentGame.getRolebyName(username);
        if (playerRole.get() == PlayerRoles.BLUE_SPYMASTER || playerRole.get() == PlayerRoles.RED_SPYMASTER) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only Operatives can skip turns");
        }

        currentGame.increaseTurn(1);
        currentGame.setRemainingGuesses(0);

        String logMessage = String.format("%s skipped the turn", username);
        currentGame.logTurn(logMessage);

        // TODO: Update Database with info
        InMemoryStore.putGame(currentGame.getGameID(), currentGame);
        
        return currentGame;
    }

    private void verifyAction(Game currentGame, String username) {
        //Bad Request need to be handled diffrently since Websocket communication. Just providing Error flow.
        if (currentGame.getWinner().isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game is already concluded, please wait for host to close the Game Session");
        }

        Optional<PlayerRoles> playerRole = currentGame.getRolebyName(username);
        if (!playerRole.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user with the given username participates in the game");
        }

        if (playerRole.get() != currentGame.getTurn()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It is not yet the given users turn");
        }
    }

    
    // helper function for creating the API during development (will be removed later)
    public static Game createSampleGame(int num) {
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
        newGame.increaseTurn(num);

        return newGame;
    }
}
