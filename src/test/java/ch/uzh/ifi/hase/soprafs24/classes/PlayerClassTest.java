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


public class PlayerClassTest {

    @Test
    public void playerOneParameterConstructor_Succeds() {

        Player testPlayer = new Player("kevin");

        assertEquals(testPlayer.getPlayerName(), "kevin");
        assertEquals(testPlayer.getRole().isPresent(), false);
        assertEquals(testPlayer.getTeam().isPresent(), false);
    }

    @Test
    public void playerTwoParameterConstructor_Succeds() {

        Player testPlayer = new Player("kevin", PlayerRoles.BLUE_SPYMASTER);

        assertEquals(testPlayer.getPlayerName(), "kevin");
        assertEquals(testPlayer.getRole().get(), PlayerRoles.BLUE_SPYMASTER);
        assertEquals(testPlayer.getTeam().get(), TeamColor.BLUE);
    }

    @Test
    public void playerConstructorNotAcceptEmptyString() {
        Exception exception = assertThrows( 
            IllegalArgumentException.class, 
            () -> {
            Player testPlayer = new Player("", PlayerRoles.BLUE_SPYMASTER);
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Playername cannot be empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void playerConstructorNotAcceptNull() {
        Exception exception = assertThrows( 
            IllegalArgumentException.class, 
            () -> {
            Player testPlayer = new Player(null, PlayerRoles.BLUE_SPYMASTER);
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Playername cannot be empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void playerCopyConstructorCreatesDeepCopy() {
        
        Player testPlayer = new Player("kevin", PlayerRoles.BLUE_SPYMASTER);
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
}
