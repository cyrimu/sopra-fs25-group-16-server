package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.classes.Clue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

public class ClueClassTest {
    private Clue testClue = new Clue("hello", 5, "Alberto");

    @BeforeEach
    public void setup() {
        testClue = new Clue("hello", 5, "Alberto");
    }

    @Test
    public void ClueConstructorSucceeds() {
        testClue = new Clue("hello", 5, "Alberto");
        assertEquals(testClue.getClueText(), "hello");
        assertEquals(testClue.getClueNumber(), 5);
        assertEquals(testClue.getUsername(), "Alberto");
    }
}