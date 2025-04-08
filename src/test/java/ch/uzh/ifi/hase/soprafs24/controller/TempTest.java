package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.classes.Game;
import ch.uzh.ifi.hase.soprafs24.classes.Clue;
import ch.uzh.ifi.hase.soprafs24.classes.Guess;
import ch.uzh.ifi.hase.soprafs24.classes.GameConfiguration;
import ch.uzh.ifi.hase.soprafs24.classes.Player;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.classes.DeepLTranslator;


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

public class TempTest {
    @InjectMocks
    private GameService gameService = new GameService();

    @Test
    public void handleValidClue() {
        Clue clue = new Clue("Fish", 2, "Alice");
        Game modifiedGame = gameService.handleClue(clue);

        assertEquals(modifiedGame.getTurn(), PlayerRoles.BLUE_OPERATIVE);
        assertEquals(modifiedGame.getRemainingGuesses(), 3);
        assertEquals(modifiedGame.getLog().length, 1);
        assertEquals(modifiedGame.getLog()[0], String.format("%s provided the Clue: %s : %d", "Alice", "Fish", 2));
    }

    @Test
    public void handleInvalidClueWordOnBoard() {
        Clue clue = new Clue("ace", 2, "Alice");
        Game modifiedGame = gameService.handleClue(clue);

        assertEquals(modifiedGame.getTurn(), PlayerRoles.RED_SPYMASTER);
        assertEquals(modifiedGame.getRemainingGuesses(), 0);
        assertEquals(modifiedGame.getLog().length, 1);
        assertEquals(modifiedGame.getLog()[0], String.format("%s provided the Clue: %s : %d", "Alice", "ace", 2));
    }

    @Test
    public void handleInvalidClueNonExistantPlayer() {
        Exception exception = assertThrows( 
            ResponseStatusException.class, 
            () -> {
            Clue clue = new Clue("Fish", 2, "Beta");
            Game modifiedGame = gameService.handleClue(clue);
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "No user with the given username participates in the game";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void handleInvalidClueNonActivePlayer() {
        Exception exception = assertThrows( 
            ResponseStatusException.class, 
            () -> {
            Clue clue = new Clue("Fish", 2, "Carol");
            Game modifiedGame = gameService.handleClue(clue);
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "It is not yet the given users turn";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // Cannot be tested while mock Games are used!
    // @Test
    // public void handleInvalidClueNonSpymaster() {
    //     Exception exception = assertThrows( 
    //         ResponseStatusException.class, 
    //         () -> {
    //         Clue clue = new Clue("Fish", 2, "Bob");
    //         Game modifiedGame = gameService.handleClue(clue);
    //         },
    //         "Expected previous Instruction to throw, but it did not."
    //         );

    //     String expectedMessage = "Only Spymaster can provide clues";
    //     String actualMessage = exception.getMessage();

    //     if (1==1) {
    //         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, actualMessage);
    //     }

    //     assertTrue(actualMessage.contains(expectedMessage));
    // }
}