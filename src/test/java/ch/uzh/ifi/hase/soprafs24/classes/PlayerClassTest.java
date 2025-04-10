package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.classes.Player;
import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.IllegalArgumentException;
import java.lang.NullPointerException;

public class PlayerClassTest {
    @InjectMocks
    private Player testPlayer = new Player("kevin", PlayerRoles.BLUE_SPYMASTER);


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        testPlayer = new Player("kevin", PlayerRoles.BLUE_SPYMASTER);
    }

    @Test
    public void playerOneParameterConstructor_Succeds() {
        testPlayer = new Player("kevin");
        assertEquals(testPlayer.getPlayerName(), "kevin");
        assertEquals(testPlayer.getRole().isPresent(), false);
        assertEquals(testPlayer.getTeam().isPresent(), false);
    }

    @Test
    public void playerTwoParameterConstructor_Succeds() {
        testPlayer = new Player("kevin", PlayerRoles.BLUE_SPYMASTER);
        assertEquals(testPlayer.getPlayerName(), "kevin");
        assertEquals(testPlayer.getRole().get(), PlayerRoles.BLUE_SPYMASTER);
        assertEquals(testPlayer.getTeam().get(), TeamColor.BLUE);
    }

    @Test
    public void playerConstructorNotAcceptEmptyString() {
        Exception exception = assertThrows( 
            IllegalArgumentException.class, 
            () -> {
            testPlayer = new Player("", PlayerRoles.BLUE_SPYMASTER);
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Class Player; Player Constructor: Playername cannot be empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void playerConstructorNotAcceptNull() {
        Exception exception = assertThrows( 
            NullPointerException.class, 
            () -> {
            testPlayer = new Player(null, PlayerRoles.BLUE_SPYMASTER);
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Class Player; Player Constructor: Playername cannot be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void playerCopyConstructorCreatesDeepCopy() { 
        Player testCopy = new Player(testPlayer);
        
        // Assert fields are correctly copied.
        assertEquals(testPlayer.getPlayerName(), testCopy.getPlayerName());
        assertEquals(testPlayer.getRole().get(), testCopy.getRole().get());
        assertEquals(testPlayer.getTeam().get(), testCopy.getTeam().get());

        // Modify copy -> No modification of original should occur.
        testCopy.setRole(PlayerRoles.RED_OPERATIVE);

        assertNotSame(testPlayer.getRole().get(), testCopy.getRole().get());
        assertNotSame(testPlayer.getTeam().get(), testCopy.getTeam().get());
    }

    @Test
    public void playerCopyConstructorNotAcceptNull() { 
        Exception exception = assertThrows( 
            NullPointerException.class, 
            () -> {
            testPlayer = new Player((Player) null);
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Class Player; Copy Constructor: Was used with null vallue";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
