package ch.uzh.ifi.hase.soprafs24.integration;

import ch.uzh.ifi.hase.soprafs24.classes.Clue;
import ch.uzh.ifi.hase.soprafs24.classes.Guess;
import ch.uzh.ifi.hase.soprafs24.classes.Game;
import ch.uzh.ifi.hase.soprafs24.classes.Board;
import ch.uzh.ifi.hase.soprafs24.classes.Card;
import ch.uzh.ifi.hase.soprafs24.classes.Clue;
import ch.uzh.ifi.hase.soprafs24.classes.Guess;
import ch.uzh.ifi.hase.soprafs24.classes.GameConfiguration;
import ch.uzh.ifi.hase.soprafs24.classes.Player;
import ch.uzh.ifi.hase.soprafs24.classes.Lobby;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;
import ch.uzh.ifi.hase.soprafs24.constant.CardColor;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.classes.InMemoryStore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

public class IntegrationTests {
    private Game testGame;
    private Lobby testLobby;
    private GameConfiguration testConfiguration = new GameConfiguration();
    private Player[] testPlayers;
    private GameService testGameService = new GameService();
    private LobbyService testLobbyService = new LobbyService();


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

        testLobby = new Lobby("A", testPlayers, GameType.TEXT, SupportedLanguages.ENGLISH);
    }

    @Test
    public void properTurnAllPlayersAct() {
        Game newGame = testGameService.createGame(testConfiguration);
        Game modifiedGame = testGameService.handleClue(newGame.getGameID(), new Clue("shark", 2, "A"));
        int guessIndex = 0;
        Card[] board = modifiedGame.getCards();
        for (int i=0; i < board.length; i++) { 
            if (board[i].getIsRevealed() == false && board[i].getColor() == CardColor.BLUE) {guessIndex = i; break;}
        }
        modifiedGame = testGameService.handleGuess(modifiedGame.getGameID(), new Guess(guessIndex, "B"));
        board = modifiedGame.getCards();
        for (int i=0; i < board.length; i++) { 
            if (board[i].getIsRevealed() == false && board[i].getColor() == CardColor.BLUE) {guessIndex = i; break;}
        }
        modifiedGame = testGameService.handleGuess(modifiedGame.getGameID(), new Guess(guessIndex, "B"));
        modifiedGame = testGameService.handleSkip(modifiedGame.getGameID(), "B");
        modifiedGame = testGameService.handleClue(newGame.getGameID(), new Clue("Winnie Puh", 1, "C"));
        board = modifiedGame.getCards();
        for (int i=0; i < board.length; i++) { 
            if (board[i].getIsRevealed() == false && board[i].getColor() == CardColor.RED) {guessIndex = i; break;}
        }
        modifiedGame = testGameService.handleGuess(modifiedGame.getGameID(), new Guess(guessIndex, "D"));
        board = modifiedGame.getCards();
        for (int i=0; i < board.length; i++) { 
            if (board[i].getIsRevealed() == false && board[i].getColor() == CardColor.WHITE) {guessIndex = i; break;}
        }
        modifiedGame = testGameService.handleGuess(modifiedGame.getGameID(), new Guess(guessIndex, "D"));

        assertEquals(modifiedGame.getTurn(), PlayerRoles.BLUE_SPYMASTER);
        assertEquals(modifiedGame.getRemainingGuesses(), 0);
        assertEquals(modifiedGame.getWinner().isPresent(), false);
    }

    @Test
    public void properCreationOfLobbyTransitionToGame() {
        Lobby createdLobby = testLobbyService.createLobby(testConfiguration.getHost());
        for (Player player : testPlayers) {
            if (player.getPlayerName().equals(testConfiguration.getHost())) {continue;}
            testLobbyService.joinLobby(createdLobby.getLobbyID(), player.getPlayerName());
        }
        Lobby gameSetUpStart = testLobbyService.updateLobby(createdLobby.getLobbyID(), testLobby, testConfiguration.getHost());

        GameConfiguration dataStoredInLobby = new GameConfiguration();
        dataStoredInLobby.setHost(gameSetUpStart.getHost());
        for (Player player : gameSetUpStart.getPlayers()) {
            dataStoredInLobby.addPlayer(player);
        }
        dataStoredInLobby.setPlayerRole("A", PlayerRoles.BLUE_SPYMASTER);
        dataStoredInLobby.setPlayerRole("B", PlayerRoles.BLUE_OPERATIVE);
        dataStoredInLobby.setPlayerRole("C", PlayerRoles.RED_SPYMASTER);
        dataStoredInLobby.setPlayerRole("D", PlayerRoles.RED_OPERATIVE);
        dataStoredInLobby.setType(gameSetUpStart.getGameType());
        dataStoredInLobby.setLanguage(gameSetUpStart.getLanguage());

        Game createdGame = testGameService.createGame(dataStoredInLobby);
        assertNotNull(createdGame.getGameID());
        assertEquals(createdGame.getHost(), "A");
        assertEquals(createdGame.getGameType(), GameType.TEXT);
        assertEquals(createdGame.getLanguage(), SupportedLanguages.ENGLISH);

        assertEquals(createdGame.getNamebyRole(PlayerRoles.BLUE_SPYMASTER).get(), "A");
        assertEquals(createdGame.getNamebyRole(PlayerRoles.BLUE_OPERATIVE).get(), "B");
        assertEquals(createdGame.getNamebyRole(PlayerRoles.RED_SPYMASTER).get(), "C");
        assertEquals(createdGame.getNamebyRole(PlayerRoles.RED_OPERATIVE).get(), "D");

        assertEquals(createdGame.getRolebyName("A").get(), PlayerRoles.BLUE_SPYMASTER);
        assertEquals(createdGame.getRolebyName("B").get(), PlayerRoles.BLUE_OPERATIVE);
        assertEquals(createdGame.getRolebyName("C").get(), PlayerRoles.RED_SPYMASTER);
        assertEquals(createdGame.getRolebyName("D").get(), PlayerRoles.RED_OPERATIVE);
    }

}