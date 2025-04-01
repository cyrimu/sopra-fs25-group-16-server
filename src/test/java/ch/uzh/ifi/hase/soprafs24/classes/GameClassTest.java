package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.classes.Game;
import ch.uzh.ifi.hase.soprafs24.classes.Player;
import ch.uzh.ifi.hase.soprafs24.classes.Team;
import ch.uzh.ifi.hase.soprafs24.classes.Board;

import ch.uzh.ifi.hase.soprafs24.rest.dto.GameConfigurationDTO;

import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;
import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;
import java.util.NoSuchElementException;
import java.lang.IllegalArgumentException;
import java.lang.RuntimeException;
import java.lang.NullPointerException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.IllegalArgumentException;
import java.util.UUID;


public class GameClassTest {
    @InjectMocks
    private Player[] testPlayers = {    new Player("A", PlayerRoles.BLUE_SPYMASTER), 
                                        new Player("B", PlayerRoles.BLUE_OPERATIVE), 
                                        new Player("C", PlayerRoles.RED_SPYMASTER), 
                                        new Player("D", PlayerRoles.RED_OPERATIVE)};

    private GameConfigurationDTO testConfiguration = new GameConfigurationDTO();
    private Game testGame = new Game("Me", testPlayers, GameType.TEXT, SupportedLanguages.ENGLISH);


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testPlayers = new Player[] {    new Player("A", PlayerRoles.BLUE_SPYMASTER), 
                                        new Player("B", PlayerRoles.BLUE_OPERATIVE), 
                                        new Player("C", PlayerRoles.RED_SPYMASTER), 
                                        new Player("D", PlayerRoles.RED_OPERATIVE)};

        testConfiguration.setID(UUID.randomUUID().toString().substring(0, Game.ID_LENGTH));
        testConfiguration.setHost("Me");
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

        testGame = new Game(UUID.randomUUID().toString().substring(0,Game.ID_LENGTH),"Me", testPlayers, GameType.TEXT, SupportedLanguages.ENGLISH);
    }

    @Test
    public void gameAllParameterConstructorSucceds() {
        assertNotNull(testGame.getGameID());
        assertNotNull(testGame.getCards());
        assertEquals(testGame.getHost(), "Me");
        assertEquals(testGame.getGameType(), GameType.TEXT);
        assertEquals(testGame.getLanguage(), SupportedLanguages.ENGLISH);
        assertEquals(testGame.getTurn(), PlayerRoles.BLUE_SPYMASTER);
        assertEquals(testGame.getRemainingGuesses(), 0);
        assertTrue(!(testGame.getWinner().isPresent()));
        assertEquals(testGame.getLog().length, 0);
        assertEquals(testGame.getFirstTeam(), TeamColor.BLUE);

        assertEquals(testGame.getNamebyRole(PlayerRoles.BLUE_SPYMASTER).get(), "A");
        assertEquals(testGame.getNamebyRole(PlayerRoles.BLUE_OPERATIVE).get(), "B");
        assertEquals(testGame.getNamebyRole(PlayerRoles.RED_SPYMASTER).get(), "C");
        assertEquals(testGame.getNamebyRole(PlayerRoles.RED_OPERATIVE).get(), "D");

        assertEquals(testGame.getRolebyName("A").get(), PlayerRoles.BLUE_SPYMASTER);
        assertEquals(testGame.getRolebyName("B").get(), PlayerRoles.BLUE_OPERATIVE);
        assertEquals(testGame.getRolebyName("C").get(), PlayerRoles.RED_SPYMASTER);
        assertEquals(testGame.getRolebyName("D").get(), PlayerRoles.RED_OPERATIVE);
    }

    @Test
    public void gameNoIDConstructorSucceds() {
        testGame = new Game("Me", testPlayers, GameType.TEXT, SupportedLanguages.ENGLISH);

        assertNotNull(testGame.getGameID());
        assertNotNull(testGame.getCards());
        assertEquals(testGame.getHost(), "Me");
        assertEquals(testGame.getGameType(), GameType.TEXT);
        assertEquals(testGame.getLanguage(), SupportedLanguages.ENGLISH);
        assertEquals(testGame.getTurn(), PlayerRoles.BLUE_SPYMASTER);
        assertEquals(testGame.getRemainingGuesses(), 0);
        assertTrue(!(testGame.getWinner().isPresent()));
        assertEquals(testGame.getLog().length, 0);
        assertEquals(testGame.getFirstTeam(), TeamColor.BLUE);

        assertEquals(testGame.getNamebyRole(PlayerRoles.BLUE_SPYMASTER).get(), "A");
        assertEquals(testGame.getNamebyRole(PlayerRoles.BLUE_OPERATIVE).get(), "B");
        assertEquals(testGame.getNamebyRole(PlayerRoles.RED_SPYMASTER).get(), "C");
        assertEquals(testGame.getNamebyRole(PlayerRoles.RED_OPERATIVE).get(), "D");

        assertEquals(testGame.getRolebyName("A").get(), PlayerRoles.BLUE_SPYMASTER);
        assertEquals(testGame.getRolebyName("B").get(), PlayerRoles.BLUE_OPERATIVE);
        assertEquals(testGame.getRolebyName("C").get(), PlayerRoles.RED_SPYMASTER);
        assertEquals(testGame.getRolebyName("D").get(), PlayerRoles.RED_OPERATIVE);
    }

    @Test
    public void gameGameConfigurationNoIDConstructorSucceds() {
        testGame = new Game(testConfiguration);

        assertNotNull(testGame.getGameID());
        assertNotNull(testGame.getCards());
        assertEquals(testGame.getHost(), "Me");
        assertEquals(testGame.getGameType(), GameType.TEXT);
        assertEquals(testGame.getLanguage(), SupportedLanguages.ENGLISH);
        assertEquals(testGame.getTurn(), PlayerRoles.BLUE_SPYMASTER);
        assertEquals(testGame.getRemainingGuesses(), 0);
        assertTrue(!(testGame.getWinner().isPresent()));
        assertEquals(testGame.getLog().length, 0);
        assertEquals(testGame.getFirstTeam(), TeamColor.BLUE);

        assertEquals(testGame.getNamebyRole(PlayerRoles.BLUE_SPYMASTER).get(), "A");
        assertEquals(testGame.getNamebyRole(PlayerRoles.BLUE_OPERATIVE).get(), "B");
        assertEquals(testGame.getNamebyRole(PlayerRoles.RED_SPYMASTER).get(), "C");
        assertEquals(testGame.getNamebyRole(PlayerRoles.RED_OPERATIVE).get(), "D");

        assertEquals(testGame.getRolebyName("A").get(), PlayerRoles.BLUE_SPYMASTER);
        assertEquals(testGame.getRolebyName("B").get(), PlayerRoles.BLUE_OPERATIVE);
        assertEquals(testGame.getRolebyName("C").get(), PlayerRoles.RED_SPYMASTER);
        assertEquals(testGame.getRolebyName("D").get(), PlayerRoles.RED_OPERATIVE);
    }

    @Test
    public void gameGameConfigurationWithIDConstructorSucceds() {
        testGame = new Game(UUID.randomUUID().toString().substring(0,Game.ID_LENGTH), testConfiguration);

        assertNotNull(testGame.getGameID());
        assertNotNull(testGame.getCards());
        assertEquals(testGame.getHost(), "Me");
        assertEquals(testGame.getGameType(), GameType.TEXT);
        assertEquals(testGame.getLanguage(), SupportedLanguages.ENGLISH);
        assertEquals(testGame.getTurn(), PlayerRoles.BLUE_SPYMASTER);
        assertEquals(testGame.getRemainingGuesses(), 0);
        assertTrue(!(testGame.getWinner().isPresent()));
        assertEquals(testGame.getLog().length, 0);
        assertEquals(testGame.getFirstTeam(), TeamColor.BLUE);

        assertEquals(testGame.getNamebyRole(PlayerRoles.BLUE_SPYMASTER).get(), "A");
        assertEquals(testGame.getNamebyRole(PlayerRoles.BLUE_OPERATIVE).get(), "B");
        assertEquals(testGame.getNamebyRole(PlayerRoles.RED_SPYMASTER).get(), "C");
        assertEquals(testGame.getNamebyRole(PlayerRoles.RED_OPERATIVE).get(), "D");

        assertEquals(testGame.getRolebyName("A").get(), PlayerRoles.BLUE_SPYMASTER);
        assertEquals(testGame.getRolebyName("B").get(), PlayerRoles.BLUE_OPERATIVE);
        assertEquals(testGame.getRolebyName("C").get(), PlayerRoles.RED_SPYMASTER);
        assertEquals(testGame.getRolebyName("D").get(), PlayerRoles.RED_OPERATIVE);
    }

    @Test
    public void gameConstructorDeepCopiesParameters() {
        Player[] storedPlayers = testGame.getPlayers();
        assertNotSame(storedPlayers, testPlayers);

        for (int i = 0; i < testPlayers.length; i++) {
            assertNotSame(storedPlayers[i], testPlayers[i]);
            assertEquals(storedPlayers[i].getPlayerName(), testPlayers[i].getPlayerName());
            assertEquals(storedPlayers[i].getRole().get(), testPlayers[i].getRole().get());
            assertEquals(storedPlayers[i].getTeam().get(), testPlayers[i].getTeam().get());
        }
    }

    @Test
    public void gameConstructorDoesNotAcceptGameIDNull() {
        Exception exception = assertThrows( 
            NullPointerException.class, 
            () -> {
            Game testGame = new Game(null, "Me", testPlayers, GameType.TEXT, SupportedLanguages.ENGLISH);
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Class Game; Game Constructor: GameID cannot be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void gameConstructorDoesNotAcceptHostNull() {
        Exception exception = assertThrows( 
            NullPointerException.class, 
            () -> {
            Game testGame = new Game(UUID.randomUUID().toString().substring(0, Game.ID_LENGTH), null, testPlayers, GameType.TEXT, SupportedLanguages.ENGLISH);
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Class Game; Game Constructor: Host parameter cannot be null or empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void gameConstructorDoesNotAcceptGameTypeNull() {
        Exception exception = assertThrows( 
            NullPointerException.class, 
            () -> {
            Game testGame = new Game(UUID.randomUUID().toString().substring(0, Game.ID_LENGTH), "Me", testPlayers, null, SupportedLanguages.ENGLISH);
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Class Game; Game Constructor: GameType parameter cannot be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void gameConstructorDoesNotAcceptLanguageNull() {
        Exception exception = assertThrows( 
            NullPointerException.class, 
            () -> {
            Game testGame = new Game(UUID.randomUUID().toString().substring(0, Game.ID_LENGTH), "Me", testPlayers, GameType.TEXT, null);
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Class Game; Game Constructor: Language parameter cannot be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void gameConstructorDoesNotAcceptPlayersNull() {
        Exception exception = assertThrows( 
            NullPointerException.class, 
            () -> {
            Game testGame = new Game(UUID.randomUUID().toString().substring(0, Game.ID_LENGTH), "Me", null, GameType.TEXT, SupportedLanguages.ENGLISH);
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Class Game; createTeams: List of Players cannot be null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void gameConstructorGameIDTooLong() {
        Exception exception = assertThrows( 
            IllegalArgumentException.class, 
            () -> {
            Game testGame = new Game("aaaaaaaaaa", "Me", testPlayers, GameType.TEXT, SupportedLanguages.ENGLISH);
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = String.format("Class Game; Game Constructor: GameID needs to be of correct length: %2d", Game.ID_LENGTH);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void gameConstructorGameIDTooShort() {
        Exception exception = assertThrows( 
            IllegalArgumentException.class, 
            () -> {
            Game testGame = new Game("a", "Me", testPlayers, GameType.TEXT, SupportedLanguages.ENGLISH);
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = String.format("Class Game; Game Constructor: GameID needs to be of correct length: %2d", Game.ID_LENGTH);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void gameConstructorNotAcceptPlayerWithoutRoleInPlayers() {
        Exception exception = assertThrows( 
            IllegalArgumentException.class, 
            () -> {
            Player[] testPlayers = {    new Player("A", PlayerRoles.BLUE_SPYMASTER), 
                                        new Player("B"), 
                                        new Player("C", PlayerRoles.RED_SPYMASTER), 
                                        new Player("D", PlayerRoles.RED_OPERATIVE)};
                                        
            testGame = new Game("Me", testPlayers, GameType.TEXT, SupportedLanguages.ENGLISH);
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Class Game; createTeams: All Players need an assigned Role";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void gameConstructorNotAcceptDuplicateRolesInPlayers() {
        Exception exception = assertThrows( 
            IllegalArgumentException.class, 
            () -> {
            Player[] testPlayers = {    new Player("A", PlayerRoles.BLUE_SPYMASTER), 
                                        new Player("B", PlayerRoles.BLUE_SPYMASTER), 
                                        new Player("C", PlayerRoles.RED_SPYMASTER), 
                                        new Player("D", PlayerRoles.RED_OPERATIVE)};
                                        
            testGame = new Game("Me", testPlayers, GameType.TEXT, SupportedLanguages.ENGLISH);
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Class Game; createTeams: To each role only one Player may be assigned";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void gameConstructorNotAcceptNotEnoughPlayersInPlayers() {
        Exception exception = assertThrows( 
            IllegalArgumentException.class, 
            () -> {
            Player[] testPlayers = {    new Player("A", PlayerRoles.BLUE_SPYMASTER), 
                                        new Player("C", PlayerRoles.RED_SPYMASTER), 
                                        new Player("D", PlayerRoles.RED_OPERATIVE)};
                                        
            testGame = new Game("Me", testPlayers, GameType.TEXT, SupportedLanguages.ENGLISH);
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = String.format("Class Game; fillPlayerArray: A Minimum of %2d Players is needed", Game.MIN_NUM_PLAYERS);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void gameConstructorNotAcceptNonUniquePlayerNames() {
        Exception exception = assertThrows( 
            IllegalArgumentException.class, 
            () -> {
            Player[] testPlayers = {    new Player("A", PlayerRoles.BLUE_SPYMASTER), 
                                        new Player("A", PlayerRoles.BLUE_OPERATIVE), 
                                        new Player("C", PlayerRoles.RED_SPYMASTER), 
                                        new Player("D", PlayerRoles.RED_OPERATIVE)};
                                        
            testGame = new Game("Me", testPlayers, GameType.TEXT, SupportedLanguages.ENGLISH);
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Class Game; fillPlayerArray: All playerNames must be unique";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}