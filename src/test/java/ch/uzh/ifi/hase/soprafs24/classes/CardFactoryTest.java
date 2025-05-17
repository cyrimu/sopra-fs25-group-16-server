package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.classes.CardFactory;
import ch.uzh.ifi.hase.soprafs24.classes.TextCard;
import ch.uzh.ifi.hase.soprafs24.classes.ImageCard;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;
import ch.uzh.ifi.hase.soprafs24.constant.CardColor;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.IllegalArgumentException;
import java.lang.NullPointerException;

public class CardFactoryTest {

    @InjectMocks
    private CardFactory creator = new CardFactory();
    private TextCard testTextCard = creator.createTextCard(CardColor.WHITE, "Tree");
    private ImageCard testImageCard = creator.createImageCard(CardColor.WHITE, "abc-1234");


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        testTextCard = creator.createTextCard(CardColor.WHITE, "Tree");
        testImageCard = creator.createImageCard(CardColor.WHITE, "abc-1234");
    }

    @Test
    public void imageCardConstructor_Succeds() {
        testImageCard = creator.createImageCard(CardColor.WHITE, "abc-1234");
        assertEquals(testImageCard.getType(), GameType.IMAGE);
        assertEquals(testImageCard.getColor(), CardColor.WHITE);
        assertEquals(testImageCard.getContent(), "abc-1234");
        assertEquals(testImageCard.getIsRevealed(), false);
    }

    @Test
    public void imageCardConstructorNotAcceptEmptyString() {
        Exception exception = assertThrows( 
            IllegalArgumentException.class, 
            () -> {
            testImageCard = creator.createImageCard(CardColor.WHITE, "");
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Class CardFactory; createImageCard: encodedPicture parameter cannot be empty string";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void imageCardConstructorNotAcceptNullWord() {
        Exception exception = assertThrows( 
            NullPointerException.class, 
            () -> {
            testImageCard = creator.createImageCard(CardColor.WHITE, null);
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Class CardFactory; createImageCard: encodedPicture parameter cannot be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void imageCardConstructorNotAcceptNullColor() {
        Exception exception = assertThrows( 
            NullPointerException.class, 
            () -> {
            testImageCard = creator.createImageCard(null, "abc-1234");
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Class CardFactory; createImageCard: CardColor parameter cannot be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void imageCardCopyConstructorCreatesDeepCopy() {
        ImageCard copyCard = (ImageCard) creator.copyCard(testImageCard);

        assertEquals(testImageCard.getType(), copyCard.getType());
        assertEquals(testImageCard.getColor(), copyCard.getColor());
        assertEquals(testImageCard.getContent(), copyCard.getContent());
        assertEquals(testImageCard.getIsRevealed(), copyCard.getIsRevealed());

        // Modify copy -> No modification of original should occur.
        copyCard.setIsRevealed(true);
        copyCard.setContent("cda-1234");

        assertNotSame(testImageCard.getContent(), copyCard.getContent());
        assertNotSame(testImageCard.getIsRevealed(), copyCard.getIsRevealed());
    }

    @Test
    public void textCardConstructor_Succeds() {
        testTextCard = creator.createTextCard(CardColor.WHITE, "Tree");
        assertEquals(testTextCard.getType(), GameType.TEXT);
        assertEquals(testTextCard.getColor(), CardColor.WHITE);
        assertEquals(testTextCard.getContent(), "Tree");
        assertEquals(testTextCard.getIsRevealed(), false);
    }

    @Test
    public void textCardConstructorNotAcceptEmptyString() {
        Exception exception = assertThrows( 
            IllegalArgumentException.class, 
            () -> {
            testTextCard = creator.createTextCard(CardColor.WHITE, "");
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Class TextCard; isValidWord: String cannot be empty.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void textCardConstructorNotAcceptNullWord() {
        Exception exception = assertThrows( 
            NullPointerException.class, 
            () -> {
            testTextCard = creator.createTextCard(CardColor.WHITE, null);
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Class TextCard; isValidWord: Null is not accepted";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void textCardConstructorNotAcceptNullColor() {
        Exception exception = assertThrows( 
            NullPointerException.class, 
            () -> {
            testTextCard = creator.createTextCard(null, "Tree");
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Class CardFactory; createTextCard: CardColor parameter cannot be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void copyCardNotAcceptNullCard() {
        Exception exception = assertThrows( 
            NullPointerException.class, 
            () -> {
            Card testCopy = creator.copyCard(null);
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Class CardFactory; copyCard: Card parameter may never be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    public void textCardCopyConstructorCreatesDeepCopy() {
        TextCard copyCard = (TextCard) creator.copyCard(testTextCard);

        assertEquals(testTextCard.getType(), copyCard.getType());
        assertEquals(testTextCard.getColor(), copyCard.getColor());
        assertEquals(testTextCard.getContent(), copyCard.getContent());
        assertEquals(testTextCard.getIsRevealed(), copyCard.getIsRevealed());

        // Modify copy -> No modification of original should occur.
        copyCard.setIsRevealed(true);
        copyCard.setContent("Tower");

        assertNotSame(testTextCard.getContent(), copyCard.getContent());
        assertNotSame(testTextCard.getIsRevealed(), copyCard.getIsRevealed());
    }
}
