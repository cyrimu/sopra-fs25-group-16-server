package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.classes.Clue;
import ch.uzh.ifi.hase.soprafs24.classes.Guess;
import ch.uzh.ifi.hase.soprafs24.classes.Game;
import ch.uzh.ifi.hase.soprafs24.classes.Board;
import ch.uzh.ifi.hase.soprafs24.classes.Card;
import ch.uzh.ifi.hase.soprafs24.classes.Clue;
import ch.uzh.ifi.hase.soprafs24.classes.Guess;
import ch.uzh.ifi.hase.soprafs24.classes.GameConfiguration;
import ch.uzh.ifi.hase.soprafs24.classes.Player;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;
import ch.uzh.ifi.hase.soprafs24.constant.CardColor;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.classes.DeepLTranslator;
import ch.uzh.ifi.hase.soprafs24.classes.InMemoryStore;

import java.lang.IllegalArgumentException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

public class GameServiceTest {
    @InjectMocks
    private GameService gameService = new GameService();

    private Game testGame;
    private GameConfiguration testConfiguration = new GameConfiguration();
    private Player[] testPlayers;

    @BeforeEach
    public void setup() {

        testPlayers = new Player[] {    new Player("A", PlayerRoles.BLUE_SPYMASTER), 
                                        new Player("B", PlayerRoles.BLUE_OPERATIVE), 
                                        new Player("C", PlayerRoles.RED_SPYMASTER), 
                                        new Player("D", PlayerRoles.RED_OPERATIVE)};

        testConfiguration.setID(UUID.randomUUID().toString().substring(0, Game.ID_LENGTH));
        testConfiguration.setHost("A");
        testConfiguration.addPlayer(testPlayers[0]);
        testConfiguration.addPlayer(testPlayers[1]);
        testConfiguration.addPlayer(testPlayers[2]);
        testConfiguration.addPlayer(testPlayers[3]);

        testConfiguration.setPlayerRole("A", PlayerRoles.BLUE_SPYMASTER);
        testConfiguration.setPlayerRole("B", PlayerRoles.BLUE_OPERATIVE);
        testConfiguration.setPlayerRole("C", PlayerRoles.RED_SPYMASTER);
        testConfiguration.setPlayerRole("D", PlayerRoles.RED_OPERATIVE);
        testConfiguration.setType(GameType.TEXT);
        testConfiguration.setLanguage(SupportedLanguages.ENGLISH);
        testGame = new Game(testConfiguration);
    }

    // @Test
    // public void createGameValidReturnedGameByMethodCorrect() {
    //     Game createdGame = gameService.createGame(testGame.getGameID(), testConfiguration);
    //     Game storedGame = InMemoryStore.getGame(testGame.getGameID());

    //     assertEquals(createdGame.getGameID(), testGame.getGameID());
    //     assertEquals(createdGame.getHost(), testGame.getHost());
    //     assertEquals(createdGame.getGameType(), testGame.getGameType());
    //     assertEquals(createdGame.getLanguage(), testGame.getLanguage());

    //     assertEquals(createdGame.getNamebyRole(PlayerRoles.BLUE_SPYMASTER).get(), testGame.getNamebyRole(PlayerRoles.BLUE_SPYMASTER).get());
    //     assertEquals(createdGame.getNamebyRole(PlayerRoles.BLUE_OPERATIVE).get(), testGame.getNamebyRole(PlayerRoles.BLUE_OPERATIVE).get());
    //     assertEquals(createdGame.getNamebyRole(PlayerRoles.RED_SPYMASTER).get(), testGame.getNamebyRole(PlayerRoles.RED_SPYMASTER).get());
    //     assertEquals(createdGame.getNamebyRole(PlayerRoles.RED_OPERATIVE).get(), testGame.getNamebyRole(PlayerRoles.RED_OPERATIVE).get());

    //     InMemoryStore.removeGame(testGame.getGameID());

    // }

    // @Test
    // public void createGameValidStoredGameByMethodCorrect() {
    //     Game createdGame = gameService.createGame(testGame.getGameID(), testConfiguration);
    //     Game storedGame = InMemoryStore.getGame(testGame.getGameID());

    //     assertEquals(storedGame.getGameID(), testGame.getGameID());
    //     assertEquals(storedGame.getHost(), testGame.getHost());
    //     assertEquals(storedGame.getGameType(), testGame.getGameType());
    //     assertEquals(storedGame.getLanguage(), testGame.getLanguage());

    //     assertEquals(storedGame.getNamebyRole(PlayerRoles.BLUE_SPYMASTER).get(), testGame.getNamebyRole(PlayerRoles.BLUE_SPYMASTER).get());
    //     assertEquals(storedGame.getNamebyRole(PlayerRoles.BLUE_OPERATIVE).get(), testGame.getNamebyRole(PlayerRoles.BLUE_OPERATIVE).get());
    //     assertEquals(storedGame.getNamebyRole(PlayerRoles.RED_SPYMASTER).get(), testGame.getNamebyRole(PlayerRoles.RED_SPYMASTER).get());
    //     assertEquals(storedGame.getNamebyRole(PlayerRoles.RED_OPERATIVE).get(), testGame.getNamebyRole(PlayerRoles.RED_OPERATIVE).get());

    //     InMemoryStore.removeGame(testGame.getGameID());
    // }

    // @Test
    // public void retrieveGameSucceedsFromInMemoryStorage() {
    //     Game createdGame = gameService.createGame(testGame.getGameID(), testConfiguration);
    //     Game retrievedGame = gameService.retrieveGame(createdGame.getGameID(), "A");

    //     assertEquals(retrievedGame.getGameID(), createdGame.getGameID());
    //     assertEquals(retrievedGame.getHost(), createdGame.getHost());
    //     assertEquals(retrievedGame.getGameType(), createdGame.getGameType());
    //     assertEquals(retrievedGame.getLanguage(), createdGame.getLanguage());

    //     assertEquals(retrievedGame.getNamebyRole(PlayerRoles.BLUE_SPYMASTER).get(), createdGame.getNamebyRole(PlayerRoles.BLUE_SPYMASTER).get());
    //     assertEquals(retrievedGame.getNamebyRole(PlayerRoles.BLUE_OPERATIVE).get(), createdGame.getNamebyRole(PlayerRoles.BLUE_OPERATIVE).get());
    //     assertEquals(retrievedGame.getNamebyRole(PlayerRoles.RED_SPYMASTER).get(), createdGame.getNamebyRole(PlayerRoles.RED_SPYMASTER).get());
    //     assertEquals(retrievedGame.getNamebyRole(PlayerRoles.RED_OPERATIVE).get(), createdGame.getNamebyRole(PlayerRoles.RED_OPERATIVE).get());

    //     InMemoryStore.removeGame(testGame.getGameID());
    // }

    // @Test
    // public void retrieveGameSucceedsFromDatabase() {
    //     Game createdGame = gameService.createGame(testGame.getGameID(), testConfiguration);
    //     InMemoryStore.removeGame(createdGame.getGameID());
    //     Game retrievedGame = gameService.retrieveGame(createdGame.getGameID(), "A");

    //     assertEquals(retrievedGame.getGameID(), createdGame.getGameID());
    //     assertEquals(retrievedGame.getHost(), createdGame.getHost());
    //     assertEquals(retrievedGame.getGameType(), createdGame.getGameType());
    //     assertEquals(retrievedGame.getLanguage(), createdGame.getLanguage());

    //     assertEquals(retrievedGame.getNamebyRole(PlayerRoles.BLUE_SPYMASTER).get(), createdGame.getNamebyRole(PlayerRoles.BLUE_SPYMASTER).get());
    //     assertEquals(retrievedGame.getNamebyRole(PlayerRoles.BLUE_OPERATIVE).get(), createdGame.getNamebyRole(PlayerRoles.BLUE_OPERATIVE).get());
    //     assertEquals(retrievedGame.getNamebyRole(PlayerRoles.RED_SPYMASTER).get(), createdGame.getNamebyRole(PlayerRoles.RED_SPYMASTER).get());
    //     assertEquals(retrievedGame.getNamebyRole(PlayerRoles.RED_OPERATIVE).get(), createdGame.getNamebyRole(PlayerRoles.RED_OPERATIVE).get());

    //     InMemoryStore.removeGame(createdGame.getGameID());
    // }


    // @Test
    // public void retrieveGameFailsWrongHost() {
    //     Exception exception = assertThrows( 
    //         ResponseStatusException.class, 
    //         () -> {
    //         Game createdGame = gameService.createGame(testGame.getGameID(), testConfiguration);
    //         Game retrievedGame = gameService.retrieveGame(createdGame.getGameID(), "B");
    //         },
    //         "Expected previous Instruction to throw, but it did not."
    //         );

    //     String expectedMessage = "User is not host of this game";
    //     String actualMessage = exception.getMessage();

    //     InMemoryStore.removeGame(testGame.getGameID());
    //     assertTrue(actualMessage.contains(expectedMessage));
    // }

    // FROM NOW ON CLUE HANDLE GUESS Tests

    @Test
    public void handleClueValidClueBlueSpymasterReturnedGame() {
        InMemoryStore.putGame(testGame.getGameID(), testGame);
        Game guessResult = gameService.handleClue(testGame.getGameID(), new Clue("shark", 5, "A"));

        String expectedMessage = String.format("%s provided the Clue: %s : %d", "A", "shark", 5);

        InMemoryStore.removeGame(testGame.getGameID());

        assertEquals(guessResult.getTurn(), PlayerRoles.BLUE_OPERATIVE);
        assertEquals(guessResult.getRemainingGuesses(), 6);
        String actualMessage = guessResult.getLog()[0];
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void handleClueValidClueRedSpymasterReturnedGame() {
        testGame.increaseTurn(2);
        InMemoryStore.putGame(testGame.getGameID(), testGame);
        Game guessResult = gameService.handleClue(testGame.getGameID(), new Clue("shark", 5, "C"));

        String expectedMessage = String.format("%s provided the Clue: %s : %d", "C", "shark", 5);

        InMemoryStore.removeGame(testGame.getGameID());

        assertEquals(guessResult.getTurn(), PlayerRoles.RED_OPERATIVE);
        assertEquals(guessResult.getRemainingGuesses(), 6);
        String actualMessage = guessResult.getLog()[0];
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void handleClueInvalidWordOnBoard() {
        InMemoryStore.putGame(testGame.getGameID(), testGame);

        String invalidGuess = (String) testGame.getCards()[0].getContent();

        Game guessResult = gameService.handleClue(testGame.getGameID(), new Clue(invalidGuess, 5, "A"));

        String expectedMessage = String.format("%s provided the INVALID Clue: %s : %d", "A", invalidGuess, 5);

        InMemoryStore.removeGame(testGame.getGameID());

        assertEquals(guessResult.getTurn(), PlayerRoles.RED_SPYMASTER);
        assertEquals(guessResult.getRemainingGuesses(), 0);
        String actualMessage = guessResult.getLog()[0];
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void handleClueValidClueSpymasterStoredGame() {
        InMemoryStore.putGame(testGame.getGameID(), testGame);
        Game guessResult = gameService.handleClue(testGame.getGameID(), new Clue("shark", 5, "A"));

        String expectedMessage = String.format("%s provided the Clue: %s : %d", "A", "shark", 5);

        Game storedGame = InMemoryStore.getGame(testGame.getGameID()).get();
        InMemoryStore.removeGame(testGame.getGameID());
        assertEquals(guessResult.getTurn(), PlayerRoles.BLUE_OPERATIVE);
        assertEquals(guessResult.getRemainingGuesses(), 6);
        String actualMessage = guessResult.getLog()[0];
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void handleClueInvalidPlayerIsNoSpymaster() {
        testGame.increaseTurn(1);
        InMemoryStore.putGame(testGame.getGameID(), testGame);

        Exception exception = assertThrows( 
            ResponseStatusException.class, 
            () -> {
            Game guessResult = gameService.handleClue(testGame.getGameID(), new Clue("shark", 5, "B"));
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Only Spymaster can provide clues";
        String actualMessage = exception.getMessage();

        InMemoryStore.removeGame(testGame.getGameID());
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void handleClueInvalidGuessNumberTooHigh() {
        InMemoryStore.putGame(testGame.getGameID(), testGame);
        Exception exception = assertThrows( 
            ResponseStatusException.class, 
            () -> {
            Game guessResult = gameService.handleClue(testGame.getGameID(), new Clue("shark", 100000, "A"));
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Only maximally the whole Boardsize can be used for the Guessnumber";
        String actualMessage = exception.getMessage();

        InMemoryStore.removeGame(testGame.getGameID());
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void handleClueInvalidWinnerAlreadyPresent() {
        testGame.setWinner(TeamColor.BLUE);
        InMemoryStore.putGame(testGame.getGameID(), testGame);

        Exception exception = assertThrows( 
            ResponseStatusException.class, 
            () -> {
            Game guessResult = gameService.handleClue(testGame.getGameID(), new Clue("shark", 5, "A"));
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Game is already concluded, please wait for host to close the Game Session";
        String actualMessage = exception.getMessage();

        InMemoryStore.removeGame(testGame.getGameID());
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void handleClueInvalidPlayerNotInGame() {
        InMemoryStore.putGame(testGame.getGameID(), testGame);

        Exception exception = assertThrows( 
            ResponseStatusException.class, 
            () -> {
            Game guessResult = gameService.handleClue(testGame.getGameID(), new Clue("shark", 5, "E"));
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "No user with the given username participates in the game";
        String actualMessage = exception.getMessage();

        InMemoryStore.removeGame(testGame.getGameID());
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void handleClueInvalidNotPlayerTurn() {
        InMemoryStore.putGame(testGame.getGameID(), testGame);

        Exception exception = assertThrows( 
            ResponseStatusException.class, 
            () -> {
            Game guessResult = gameService.handleClue(testGame.getGameID(), new Clue("shark", 5, "C"));
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "It is not yet the given users turn";
        String actualMessage = exception.getMessage();

        InMemoryStore.removeGame(testGame.getGameID());
        assertTrue(actualMessage.contains(expectedMessage));
    }

    // FROM NOW ON GUESS HANDLE GUESS Tests

    @Test
    public void handleGuessValidBlueOperativeBlueCardRemainingGuessesMoreThanOne() {
        testGame.increaseTurn(1);
        testGame.setRemainingGuesses(2);
        InMemoryStore.putGame(testGame.getGameID(), testGame);

        int guessIndex = 0;

        Card[] cards = testGame.getCards();
        for (int i = 0; i < cards.length; i++) {
            if (cards[i].getColor() == CardColor.BLUE) {
                guessIndex = i;
                break;
            }
        }

        Game guessResult = gameService.handleGuess(testGame.getGameID(), new Guess(guessIndex, "B"));
        Card guessedCard = guessResult.getCards()[guessIndex];

        String expectedMessage = String.format("%s made the guess: %s", "B", (String) guessedCard.getContent());

        InMemoryStore.removeGame(testGame.getGameID());

        assertEquals(guessResult.getTurn(), PlayerRoles.BLUE_OPERATIVE);
        assertEquals(guessResult.getRemainingGuesses(), 1);
        String actualMessage = guessResult.getLog()[0];
        assertTrue(actualMessage.contains(expectedMessage));
        assertEquals(guessedCard.getIsRevealed(), true);
    }

    @Test
    public void handleGuessValidBlueOperativeBlueCardRemainingGuessesOne() {
        testGame.increaseTurn(1);
        testGame.setRemainingGuesses(1);
        InMemoryStore.putGame(testGame.getGameID(), testGame);

        int guessIndex = 0;

        Card[] cards = testGame.getCards();
        for (int i = 0; i < cards.length; i++) {
            if (cards[i].getColor() == CardColor.BLUE) {
                guessIndex = i;
                break;
            }
        }

        Game guessResult = gameService.handleGuess(testGame.getGameID(), new Guess(guessIndex, "B"));
        Card guessedCard = guessResult.getCards()[guessIndex];

        String expectedMessage = String.format("%s made the guess: %s", "B", (String) guessedCard.getContent());

        InMemoryStore.removeGame(testGame.getGameID());

        assertEquals(guessResult.getTurn(), PlayerRoles.RED_SPYMASTER);
        assertEquals(guessResult.getRemainingGuesses(), 0);
        String actualMessage = guessResult.getLog()[0];
        assertTrue(actualMessage.contains(expectedMessage));
        assertEquals(guessedCard.getIsRevealed(), true);
    }

    @Test
    public void handleGuessValidBlueOperativeRedCard() {
        testGame.increaseTurn(1);
        testGame.setRemainingGuesses(2);
        InMemoryStore.putGame(testGame.getGameID(), testGame);

        int guessIndex = 0;

        Card[] cards = testGame.getCards();
        for (int i = 0; i < cards.length; i++) {
            if (cards[i].getColor() == CardColor.RED) {
                guessIndex = i;
                break;
            }
        }

        Game guessResult = gameService.handleGuess(testGame.getGameID(), new Guess(guessIndex, "B"));
        Card guessedCard = guessResult.getCards()[guessIndex];

        String expectedMessage = String.format("%s made the guess: %s", "B", (String) guessedCard.getContent());

        InMemoryStore.removeGame(testGame.getGameID());

        assertEquals(guessResult.getTurn(), PlayerRoles.RED_SPYMASTER);
        assertEquals(guessResult.getRemainingGuesses(), 0);
        String actualMessage = guessResult.getLog()[0];
        assertTrue(actualMessage.contains(expectedMessage));
        assertEquals(guessedCard.getIsRevealed(), true);
    }

    @Test
    public void handleGuessValidRedOperativeRedCardRemainingGuessesMoreThanOne() {
        testGame.increaseTurn(3);
        testGame.setRemainingGuesses(2);
        InMemoryStore.putGame(testGame.getGameID(), testGame);

        int guessIndex = 0;

        Card[] cards = testGame.getCards();
        for (int i = 0; i < cards.length; i++) {
            if (cards[i].getColor() == CardColor.RED) {
                guessIndex = i;
                break;
            }
        }

        Game guessResult = gameService.handleGuess(testGame.getGameID(), new Guess(guessIndex, "D"));
        Card guessedCard = guessResult.getCards()[guessIndex];

        String expectedMessage = String.format("%s made the guess: %s", "D", (String) guessedCard.getContent());

        InMemoryStore.removeGame(testGame.getGameID());

        assertEquals(guessResult.getTurn(), PlayerRoles.RED_OPERATIVE);
        assertEquals(guessResult.getRemainingGuesses(), 1);
        String actualMessage = guessResult.getLog()[0];
        assertTrue(actualMessage.contains(expectedMessage));
        assertEquals(guessedCard.getIsRevealed(), true);
    }

    @Test
    public void handleGuessValidRedOperativeRedCardRemainingGuessesOne() {
        testGame.increaseTurn(3);
        testGame.setRemainingGuesses(1);
        InMemoryStore.putGame(testGame.getGameID(), testGame);

        int guessIndex = 0;

        Card[] cards = testGame.getCards();
        for (int i = 0; i < cards.length; i++) {
            if (cards[i].getColor() == CardColor.RED) {
                guessIndex = i;
                break;
            }
        }

        Game guessResult = gameService.handleGuess(testGame.getGameID(), new Guess(guessIndex, "D"));
        Card guessedCard = guessResult.getCards()[guessIndex];

        String expectedMessage = String.format("%s made the guess: %s", "D", (String) guessedCard.getContent());

        InMemoryStore.removeGame(testGame.getGameID());

        assertEquals(guessResult.getTurn(), PlayerRoles.BLUE_SPYMASTER);
        assertEquals(guessResult.getRemainingGuesses(), 0);
        String actualMessage = guessResult.getLog()[0];
        assertTrue(actualMessage.contains(expectedMessage));
        assertEquals(guessedCard.getIsRevealed(), true);
    }

    @Test
    public void handleGuessValidRedOperativeBlueCard() {
        testGame.increaseTurn(3);
        testGame.setRemainingGuesses(2);
        InMemoryStore.putGame(testGame.getGameID(), testGame);

        int guessIndex = 0;

        Card[] cards = testGame.getCards();
        for (int i = 0; i < cards.length; i++) {
            if (cards[i].getColor() == CardColor.BLUE) {
                guessIndex = i;
                break;
            }
        }

        Game guessResult = gameService.handleGuess(testGame.getGameID(), new Guess(guessIndex, "D"));
        Card guessedCard = guessResult.getCards()[guessIndex];

        String expectedMessage = String.format("%s made the guess: %s", "D", (String) guessedCard.getContent());

        InMemoryStore.removeGame(testGame.getGameID());

        assertEquals(guessResult.getTurn(), PlayerRoles.BLUE_SPYMASTER);
        assertEquals(guessResult.getRemainingGuesses(), 0);
        String actualMessage = guessResult.getLog()[0];
        assertTrue(actualMessage.contains(expectedMessage));
        assertEquals(guessedCard.getIsRevealed(), true);
    }

    @Test
    public void handleGuessValidBlueWinsAfterGuess() {
        testGame.increaseTurn(1);
        testGame.setRemainingGuesses(1);
        
        Card[] generatedCards = testGame.getCards();
        int count = 0;
        for (int i = 0; i < generatedCards.length; i++) {
            if (generatedCards[i].getColor() == CardColor.BLUE) {
                count++;
                testGame.revealCard(i);
            }
            if (count == Board.NUM_CARDS_FIRST_TEAM - 1) {
                break;
            }
        }

        InMemoryStore.putGame(testGame.getGameID(), testGame);

        int guessIndex = 0;

        Card[] cards = testGame.getCards();
        for (int i = 0; i < cards.length; i++) {
            if (cards[i].getColor() == CardColor.BLUE && !cards[i].getIsRevealed()) {
                guessIndex = i;
                break;
            }
        }

        Game guessResult = gameService.handleGuess(testGame.getGameID(), new Guess(guessIndex, "B"));
        Card guessedCard = guessResult.getCards()[guessIndex];

        String expectedMessage = String.format("%s made the guess: %s", "B", (String) guessedCard.getContent());
        String actualMessage = guessResult.getLog()[0];

        InMemoryStore.removeGame(testGame.getGameID());
        assertEquals(guessResult.getTurn(), PlayerRoles.RED_SPYMASTER);
        assertEquals(guessResult.getRemainingGuesses(), 0);
        assertEquals(guessResult.getWinner().isPresent(), true);
        assertTrue(actualMessage.contains(expectedMessage));
        assertEquals(guessedCard.getIsRevealed(), true);
    }

    @Test
    public void handleGuessValidRedWinsAfterGuess() {
        testGame.increaseTurn(1);
        testGame.setRemainingGuesses(1);
        
        Card[] generatedCards = testGame.getCards();
        int count = 0;
        for (int i = 0; i < generatedCards.length; i++) {
            if (generatedCards[i].getColor() == CardColor.RED) {
                count++;
                testGame.revealCard(i);
            }
            if (count == Board.NUM_CARDS_SECOND_TEAM - 1) {
                break;
            }
        }

        InMemoryStore.putGame(testGame.getGameID(), testGame);

        int guessIndex = 0;

        Card[] cards = testGame.getCards();
        for (int i = 0; i < cards.length; i++) {
            if (cards[i].getColor() == CardColor.RED && !cards[i].getIsRevealed()) {
                guessIndex = i;
                break;
            }
        }

        Game guessResult = gameService.handleGuess(testGame.getGameID(), new Guess(guessIndex, "B"));
        Card guessedCard = guessResult.getCards()[guessIndex];

        String expectedMessage = String.format("%s made the guess: %s", "B", (String) guessedCard.getContent());

        InMemoryStore.removeGame(testGame.getGameID());

        assertEquals(guessResult.getTurn(), PlayerRoles.RED_SPYMASTER);
        assertEquals(guessResult.getRemainingGuesses(), 0);
        assertEquals(guessResult.getWinner().isPresent(), true);
        String actualMessage = guessResult.getLog()[0];
        assertTrue(actualMessage.contains(expectedMessage));
        assertEquals(guessedCard.getIsRevealed(), true);
    }

    @Test
    public void handleGuessValidOperativeWhiteCard() {
        testGame.increaseTurn(1);
        testGame.setRemainingGuesses(2);
        InMemoryStore.putGame(testGame.getGameID(), testGame);

        int guessIndex = 0;

        Card[] cards = testGame.getCards();
        for (int i = 0; i < cards.length; i++) {
            if (cards[i].getColor() == CardColor.WHITE) {
                guessIndex = i;
                break;
            }
        }

        Game guessResult = gameService.handleGuess(testGame.getGameID(), new Guess(guessIndex, "B"));
        Card guessedCard = guessResult.getCards()[guessIndex];

        String expectedMessage = String.format("%s made the guess: %s", "B", (String) guessedCard.getContent());

        InMemoryStore.removeGame(testGame.getGameID());

        assertEquals(guessResult.getTurn(), PlayerRoles.RED_SPYMASTER);
        assertEquals(guessResult.getRemainingGuesses(), 0);
        String actualMessage = guessResult.getLog()[0];
        assertTrue(actualMessage.contains(expectedMessage));
        assertEquals(guessedCard.getIsRevealed(), true);
    }

    @Test
    public void handleGuessValidOperativeBlackCard() {
        testGame.increaseTurn(1);
        testGame.setRemainingGuesses(2);
        InMemoryStore.putGame(testGame.getGameID(), testGame);

        int guessIndex = 0;

        Card[] cards = testGame.getCards();
        for (int i = 0; i < cards.length; i++) {
            if (cards[i].getColor() == CardColor.BLACK) {
                guessIndex = i;
                break;
            }
        }

        Game guessResult = gameService.handleGuess(testGame.getGameID(), new Guess(guessIndex, "B"));
        Card guessedCard = guessResult.getCards()[guessIndex];

        String expectedMessage = String.format("%s made the guess: %s", "B", (String) guessedCard.getContent());

        InMemoryStore.removeGame(testGame.getGameID());

        assertEquals(guessResult.getTurn(), PlayerRoles.BLUE_OPERATIVE);
        assertEquals(guessResult.getRemainingGuesses(), 0);
        String actualMessage = guessResult.getLog()[0];
        assertTrue(actualMessage.contains(expectedMessage));
        assertEquals(guessedCard.getIsRevealed(), true);
        assertEquals(guessResult.getWinner().isPresent(), true);
    }


    @Test
    public void handleGuessInvalidPlayerNotOperative() {
        InMemoryStore.putGame(testGame.getGameID(), testGame);
        Exception exception = assertThrows( 
            ResponseStatusException.class, 
            () -> {
            Game guessResult = gameService.handleGuess(testGame.getGameID(), new Guess(2, "A"));
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Only Operative can provide guesses";
        String actualMessage = exception.getMessage();

        InMemoryStore.removeGame(testGame.getGameID());
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void handleGuessInvalidCardIndexInGuess() {
        testGame.increaseTurn(1);
        InMemoryStore.putGame(testGame.getGameID(), testGame);
        Exception exception = assertThrows( 
            ResponseStatusException.class, 
            () -> {
            Game guessResult = gameService.handleGuess(testGame.getGameID(), new Guess(1000000, "B"));
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = String.format("Guesses can only have values 0 to %d", testGame.getBoardSize()-1);
        String actualMessage = exception.getMessage();

        InMemoryStore.removeGame(testGame.getGameID());
        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    public void handleGuessInvalidWinnerAlreadyPresent() {
        testGame.setWinner(TeamColor.BLUE);
        testGame.increaseTurn(1);
        InMemoryStore.putGame(testGame.getGameID(), testGame);

        Exception exception = assertThrows( 
            ResponseStatusException.class, 
            () -> {
            Game guessResult = gameService.handleGuess(testGame.getGameID(), new Guess(2, "B"));
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Game is already concluded, please wait for host to close the Game Session";
        String actualMessage = exception.getMessage();

        InMemoryStore.removeGame(testGame.getGameID());
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void handleGuessInvalidPlayerNotInGame() {
        InMemoryStore.putGame(testGame.getGameID(), testGame);
        Exception exception = assertThrows( 
            ResponseStatusException.class, 
            () -> {
            Game guessResult = gameService.handleGuess(testGame.getGameID(), new Guess(2, "E"));
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "No user with the given username participates in the game";
        String actualMessage = exception.getMessage();

        InMemoryStore.removeGame(testGame.getGameID());
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void handleGuessInvalidNotPlayerTurn() {
        InMemoryStore.putGame(testGame.getGameID(), testGame);
        testGame.increaseTurn(1);
        Exception exception = assertThrows( 
            ResponseStatusException.class, 
            () -> {
            Game guessResult = gameService.handleGuess(testGame.getGameID(), new Guess(2, "D"));
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "It is not yet the given users turn";
        String actualMessage = exception.getMessage();

        InMemoryStore.removeGame(testGame.getGameID());
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void handleGuessInvalidCardAlreadyRevealed() {
        testGame.increaseTurn(1);
        testGame.revealCard(0);
        InMemoryStore.putGame(testGame.getGameID(), testGame);
        Exception exception = assertThrows( 
            ResponseStatusException.class, 
            () -> {
            Game guessResult = gameService.handleGuess(testGame.getGameID(), new Guess(0, "B"));
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "This card has already been revealed!";
        String actualMessage = exception.getMessage();

        InMemoryStore.removeGame(testGame.getGameID());
        assertTrue(actualMessage.contains(expectedMessage));
    }

    // Handle Skip Guess

    @Test
    public void handleSkipGuessValidBlueOperativeSkip() {
        testGame.increaseTurn(1);
        testGame.setRemainingGuesses(2);
        InMemoryStore.putGame(testGame.getGameID(), testGame);

        Game guessResult = gameService.handleSkip(testGame.getGameID(), "B");

        String expectedMessage = String.format("%s skipped the turn", "B");

        InMemoryStore.removeGame(testGame.getGameID());

        assertEquals(guessResult.getTurn(), PlayerRoles.RED_SPYMASTER);
        assertEquals(guessResult.getRemainingGuesses(), 0);
        String actualMessage = guessResult.getLog()[0];
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void handleSkipGuessValidRedOperativeSkip() {
        testGame.increaseTurn(3);
        testGame.setRemainingGuesses(2);
        InMemoryStore.putGame(testGame.getGameID(), testGame);

        Game guessResult = gameService.handleSkip(testGame.getGameID(), "D");

        String expectedMessage = String.format("%s skipped the turn", "D");

        InMemoryStore.removeGame(testGame.getGameID());

        assertEquals(guessResult.getTurn(), PlayerRoles.BLUE_SPYMASTER);
        assertEquals(guessResult.getRemainingGuesses(), 0);
        String actualMessage = guessResult.getLog()[0];
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void handleSkipGuessInvalidWinnerAlreadyPresent() {
        testGame.setWinner(TeamColor.BLUE);
        testGame.increaseTurn(1);
        InMemoryStore.putGame(testGame.getGameID(), testGame);

        Exception exception = assertThrows( 
            ResponseStatusException.class, 
            () -> {
            Game guessResult = gameService.handleSkip(testGame.getGameID(), "B");
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Game is already concluded, please wait for host to close the Game Session";
        String actualMessage = exception.getMessage();

        InMemoryStore.removeGame(testGame.getGameID());
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void handleSkipGuessInvalidPlayerNotInGame() {
        InMemoryStore.putGame(testGame.getGameID(), testGame);
        testGame.increaseTurn(1);
        Exception exception = assertThrows( 
            ResponseStatusException.class, 
            () -> {
            Game guessResult = gameService.handleSkip(testGame.getGameID(), "E");
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "No user with the given username participates in the game";
        String actualMessage = exception.getMessage();

        InMemoryStore.removeGame(testGame.getGameID());
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void handleSkipGuessInvalidNotPlayerTurn() {
        InMemoryStore.putGame(testGame.getGameID(), testGame);
        testGame.increaseTurn(1);
        Exception exception = assertThrows( 
            ResponseStatusException.class, 
            () -> {
            Game guessResult = gameService.handleSkip(testGame.getGameID(), "D");
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "It is not yet the given users turn";
        String actualMessage = exception.getMessage();

        InMemoryStore.removeGame(testGame.getGameID());
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void handleSkipGuessInvalidSpymasterTurn() {
        InMemoryStore.putGame(testGame.getGameID(), testGame);
        Exception exception = assertThrows( 
            ResponseStatusException.class, 
            () -> {
            Game guessResult = gameService.handleSkip(testGame.getGameID(), "A");
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Only Operatives can skip turns";
        String actualMessage = exception.getMessage();

        InMemoryStore.removeGame(testGame.getGameID());
        assertTrue(actualMessage.contains(expectedMessage));
    }
    
}