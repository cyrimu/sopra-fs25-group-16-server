package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.classes.Game;
import ch.uzh.ifi.hase.soprafs24.classes.Player;
import ch.uzh.ifi.hase.soprafs24.classes.Team;
import ch.uzh.ifi.hase.soprafs24.classes.Board;
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

    private GameConfiguration testConfiguration = new GameConfiguration();
    private Game testTextGame = new Game("A", testPlayers, GameType.TEXT, SupportedLanguages.ENGLISH);
    private Game testImageGame = new Game("A", testPlayers, GameType.IMAGE, SupportedLanguages.ENGLISH);


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

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

        testTextGame = new Game(UUID.randomUUID().toString().substring(0,Game.ID_LENGTH),"A", testPlayers, GameType.TEXT, SupportedLanguages.ENGLISH);
        testImageGame = new Game(UUID.randomUUID().toString().substring(0,Game.ID_LENGTH),"A", testPlayers, GameType.IMAGE, SupportedLanguages.ENGLISH);
    }

    @Test
    public void gameAllParameterConstructorSucceds() {
        assertNotNull(testTextGame.getGameID());
        assertEquals(testTextGame.getCards().length, 25);
        assertEquals(testTextGame.getHost(), "A");
        assertEquals(testTextGame.getGameType(), GameType.TEXT);
        assertEquals(testTextGame.getLanguage(), SupportedLanguages.ENGLISH);
        assertEquals(testTextGame.getTurn(), PlayerRoles.BLUE_SPYMASTER);
        assertEquals(testTextGame.getRemainingGuesses(), 0);
        assertTrue(!(testTextGame.getWinner().isPresent()));
        assertEquals(testTextGame.getLog().length, 0);
        assertEquals(testTextGame.getFirstTeam(), TeamColor.BLUE);

        assertEquals(testTextGame.getNamebyRole(PlayerRoles.BLUE_SPYMASTER).get(), "A");
        assertEquals(testTextGame.getNamebyRole(PlayerRoles.BLUE_OPERATIVE).get(), "B");
        assertEquals(testTextGame.getNamebyRole(PlayerRoles.RED_SPYMASTER).get(), "C");
        assertEquals(testTextGame.getNamebyRole(PlayerRoles.RED_OPERATIVE).get(), "D");

        assertEquals(testTextGame.getRolebyName("A").get(), PlayerRoles.BLUE_SPYMASTER);
        assertEquals(testTextGame.getRolebyName("B").get(), PlayerRoles.BLUE_OPERATIVE);
        assertEquals(testTextGame.getRolebyName("C").get(), PlayerRoles.RED_SPYMASTER);
        assertEquals(testTextGame.getRolebyName("D").get(), PlayerRoles.RED_OPERATIVE);
    }

    @Test
    public void gameAllParameterImageConstructorSucceds() {
        assertNotNull(testImageGame.getGameID());
        assertEquals(testImageGame.getCards().length, 20);
        assertEquals(testImageGame.getHost(), "A");
        assertEquals(testImageGame.getGameType(), GameType.IMAGE);
        assertEquals(testImageGame.getLanguage(), SupportedLanguages.ENGLISH);
        assertEquals(testImageGame.getTurn(), PlayerRoles.BLUE_SPYMASTER);
        assertEquals(testImageGame.getRemainingGuesses(), 0);
        assertTrue(!(testImageGame.getWinner().isPresent()));
        assertEquals(testImageGame.getLog().length, 0);
        assertEquals(testImageGame.getFirstTeam(), TeamColor.BLUE);

        assertEquals(testImageGame.getNamebyRole(PlayerRoles.BLUE_SPYMASTER).get(), "A");
        assertEquals(testImageGame.getNamebyRole(PlayerRoles.BLUE_OPERATIVE).get(), "B");
        assertEquals(testImageGame.getNamebyRole(PlayerRoles.RED_SPYMASTER).get(), "C");
        assertEquals(testImageGame.getNamebyRole(PlayerRoles.RED_OPERATIVE).get(), "D");

        assertEquals(testImageGame.getRolebyName("A").get(), PlayerRoles.BLUE_SPYMASTER);
        assertEquals(testImageGame.getRolebyName("B").get(), PlayerRoles.BLUE_OPERATIVE);
        assertEquals(testImageGame.getRolebyName("C").get(), PlayerRoles.RED_SPYMASTER);
        assertEquals(testImageGame.getRolebyName("D").get(), PlayerRoles.RED_OPERATIVE);
    }

    @Test
    public void gameNoIDConstructorSucceds() {
        testTextGame = new Game("A", testPlayers, GameType.TEXT, SupportedLanguages.ENGLISH);

        assertNotNull(testTextGame.getGameID());
        assertNotNull(testTextGame.getCards());
        assertEquals(testTextGame.getHost(), "A");
        assertEquals(testTextGame.getGameType(), GameType.TEXT);
        assertEquals(testTextGame.getLanguage(), SupportedLanguages.ENGLISH);
        assertEquals(testTextGame.getTurn(), PlayerRoles.BLUE_SPYMASTER);
        assertEquals(testTextGame.getRemainingGuesses(), 0);
        assertTrue(!(testTextGame.getWinner().isPresent()));
        assertEquals(testTextGame.getLog().length, 0);
        assertEquals(testTextGame.getFirstTeam(), TeamColor.BLUE);

        assertEquals(testTextGame.getNamebyRole(PlayerRoles.BLUE_SPYMASTER).get(), "A");
        assertEquals(testTextGame.getNamebyRole(PlayerRoles.BLUE_OPERATIVE).get(), "B");
        assertEquals(testTextGame.getNamebyRole(PlayerRoles.RED_SPYMASTER).get(), "C");
        assertEquals(testTextGame.getNamebyRole(PlayerRoles.RED_OPERATIVE).get(), "D");

        assertEquals(testTextGame.getRolebyName("A").get(), PlayerRoles.BLUE_SPYMASTER);
        assertEquals(testTextGame.getRolebyName("B").get(), PlayerRoles.BLUE_OPERATIVE);
        assertEquals(testTextGame.getRolebyName("C").get(), PlayerRoles.RED_SPYMASTER);
        assertEquals(testTextGame.getRolebyName("D").get(), PlayerRoles.RED_OPERATIVE);
    }

    @Test
    public void gameGameConfigurationNoIDConstructorSucceds() {
        testTextGame = new Game(testConfiguration);

        assertNotNull(testTextGame.getGameID());
        assertNotNull(testTextGame.getCards());
        assertEquals(testTextGame.getHost(), "A");
        assertEquals(testTextGame.getGameType(), GameType.TEXT);
        assertEquals(testTextGame.getLanguage(), SupportedLanguages.ENGLISH);
        assertEquals(testTextGame.getTurn(), PlayerRoles.BLUE_SPYMASTER);
        assertEquals(testTextGame.getRemainingGuesses(), 0);
        assertTrue(!(testTextGame.getWinner().isPresent()));
        assertEquals(testTextGame.getLog().length, 0);
        assertEquals(testTextGame.getFirstTeam(), TeamColor.BLUE);

        assertEquals(testTextGame.getNamebyRole(PlayerRoles.BLUE_SPYMASTER).get(), "A");
        assertEquals(testTextGame.getNamebyRole(PlayerRoles.BLUE_OPERATIVE).get(), "B");
        assertEquals(testTextGame.getNamebyRole(PlayerRoles.RED_SPYMASTER).get(), "C");
        assertEquals(testTextGame.getNamebyRole(PlayerRoles.RED_OPERATIVE).get(), "D");

        assertEquals(testTextGame.getRolebyName("A").get(), PlayerRoles.BLUE_SPYMASTER);
        assertEquals(testTextGame.getRolebyName("B").get(), PlayerRoles.BLUE_OPERATIVE);
        assertEquals(testTextGame.getRolebyName("C").get(), PlayerRoles.RED_SPYMASTER);
        assertEquals(testTextGame.getRolebyName("D").get(), PlayerRoles.RED_OPERATIVE);
    }

    @Test
    public void gameGameConfigurationWithIDConstructorSucceds() {
        testTextGame = new Game(UUID.randomUUID().toString().substring(0,Game.ID_LENGTH), testConfiguration);

        assertNotNull(testTextGame.getGameID());
        assertNotNull(testTextGame.getCards());
        assertEquals(testTextGame.getHost(), "A");
        assertEquals(testTextGame.getGameType(), GameType.TEXT);
        assertEquals(testTextGame.getLanguage(), SupportedLanguages.ENGLISH);
        assertEquals(testTextGame.getTurn(), PlayerRoles.BLUE_SPYMASTER);
        assertEquals(testTextGame.getRemainingGuesses(), 0);
        assertTrue(!(testTextGame.getWinner().isPresent()));
        assertEquals(testTextGame.getLog().length, 0);
        assertEquals(testTextGame.getFirstTeam(), TeamColor.BLUE);

        assertEquals(testTextGame.getNamebyRole(PlayerRoles.BLUE_SPYMASTER).get(), "A");
        assertEquals(testTextGame.getNamebyRole(PlayerRoles.BLUE_OPERATIVE).get(), "B");
        assertEquals(testTextGame.getNamebyRole(PlayerRoles.RED_SPYMASTER).get(), "C");
        assertEquals(testTextGame.getNamebyRole(PlayerRoles.RED_OPERATIVE).get(), "D");

        assertEquals(testTextGame.getRolebyName("A").get(), PlayerRoles.BLUE_SPYMASTER);
        assertEquals(testTextGame.getRolebyName("B").get(), PlayerRoles.BLUE_OPERATIVE);
        assertEquals(testTextGame.getRolebyName("C").get(), PlayerRoles.RED_SPYMASTER);
        assertEquals(testTextGame.getRolebyName("D").get(), PlayerRoles.RED_OPERATIVE);
    }

    @Test
    public void gameConstructorDeepCopiesParameters() {
        Player[] storedPlayers = testTextGame.getPlayers();
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
            Game testTextGame = new Game(null, "A", testPlayers, GameType.TEXT, SupportedLanguages.ENGLISH);
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
            Game testTextGame = new Game(UUID.randomUUID().toString().substring(0, Game.ID_LENGTH), null, testPlayers, GameType.TEXT, SupportedLanguages.ENGLISH);
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
            Game testTextGame = new Game(UUID.randomUUID().toString().substring(0, Game.ID_LENGTH), "A", testPlayers, null, SupportedLanguages.ENGLISH);
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
            Game testTextGame = new Game(UUID.randomUUID().toString().substring(0, Game.ID_LENGTH), "A", testPlayers, GameType.TEXT, null);
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
            Game testTextGame = new Game(UUID.randomUUID().toString().substring(0, Game.ID_LENGTH), "A", null, GameType.TEXT, SupportedLanguages.ENGLISH);
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
            Game testTextGame = new Game("aaaaaaaaaa", "A", testPlayers, GameType.TEXT, SupportedLanguages.ENGLISH);
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
            Game testTextGame = new Game("a", "A", testPlayers, GameType.TEXT, SupportedLanguages.ENGLISH);
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
                                        
            testTextGame = new Game("A", testPlayers, GameType.TEXT, SupportedLanguages.ENGLISH);
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
                                        
            testTextGame = new Game("A", testPlayers, GameType.TEXT, SupportedLanguages.ENGLISH);
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
                                        
            testTextGame = new Game("A", testPlayers, GameType.TEXT, SupportedLanguages.ENGLISH);
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
                                        
            testTextGame = new Game("A", testPlayers, GameType.TEXT, SupportedLanguages.ENGLISH);
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Class Game; fillPlayerArray: All playerNames must be unique";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void gameConstructorNotAcceptHostNotInPlayers() {
        Exception exception = assertThrows( 
            IllegalArgumentException.class, 
            () -> {
            Player[] testPlayers = {    new Player("A", PlayerRoles.BLUE_SPYMASTER), 
                                        new Player("B", PlayerRoles.BLUE_OPERATIVE), 
                                        new Player("C", PlayerRoles.RED_SPYMASTER), 
                                        new Player("D", PlayerRoles.RED_OPERATIVE)};
                                        
            testTextGame = new Game("Me", testPlayers, GameType.TEXT, SupportedLanguages.ENGLISH);
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Class Game; Game Constructor: Host must be a Player";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}