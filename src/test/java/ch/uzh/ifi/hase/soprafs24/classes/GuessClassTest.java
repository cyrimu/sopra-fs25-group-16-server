package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.classes.Guess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

public class GuessClassTest {
    private Guess testGuess = new Guess(new Integer(5), "Alberto");

    @BeforeEach
    public void setup() {
        testGuess = new Guess(new Integer(5), "Alberto");
    }

    @Test
    public void ClueConstructorSucceeds() {
        testGuess = new Guess(new Integer(5), "Alberto");
        assertEquals(new Integer(5), testGuess.getCardNumber());
        assertEquals("Alberto", testGuess.getUsername());
    }
}