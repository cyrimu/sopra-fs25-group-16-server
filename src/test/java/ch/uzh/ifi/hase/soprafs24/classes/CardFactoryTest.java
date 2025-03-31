package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.classes.CardFactory;
import ch.uzh.ifi.hase.soprafs24.classes.TextCard;
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


public class CardFactoryTest {

    @Test
    public void textCardConstructor_Succeds() {

        CardFactory creator = new CardFactory();
        TextCard testCard = creator.createTextCard(CardColor.WHITE, "Tree");

        assertEquals(testCard.getType(), GameType.TEXT);
        assertEquals(testCard.getColor(), CardColor.WHITE);
        assertEquals(testCard.getContent(), "Tree");
        assertEquals(testCard.getIsRevealed(), false);
    }

    @Test
    public void textCardConstructorNotAcceptEmptyString() {
        Exception exception = assertThrows( 
            IllegalArgumentException.class, 
            () -> {
            CardFactory creator = new CardFactory();
            TextCard testCard = creator.createTextCard(CardColor.WHITE, "");
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Only Strings which consist solely out of letters can be used: i.e. tree or Mensch etc.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void textCardConstructorNotAcceptNull() {
        Exception exception = assertThrows( 
            IllegalArgumentException.class, 
            () -> {
            CardFactory creator = new CardFactory();
            TextCard testCard = creator.createTextCard(CardColor.WHITE, null);
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Only Strings which consist solely out of letters can be used: i.e. tree or Mensch etc.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void textCardConstructorOnlyAcceptLetterWord() {
        Exception exception = assertThrows( 
            IllegalArgumentException.class, 
            () -> {
            CardFactory creator = new CardFactory();
            TextCard testCard = creator.createTextCard(CardColor.WHITE, "123");
            },
            "Expected previous Instruction to throw, but it did not."
            );

        String expectedMessage = "Only Strings which consist solely out of letters can be used: i.e. tree or Mensch etc.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void textCardCopyConstructorCreatesDeepCopy() {
        
        CardFactory creator = new CardFactory();
        TextCard testCard = creator.createTextCard(CardColor.WHITE, "Tree");
        TextCard copyCard = (TextCard) creator.copyCard(testCard);

        assertEquals(testCard.getType(), copyCard.getType());
        assertEquals(testCard.getColor(), copyCard.getColor());
        assertEquals(testCard.getContent(), copyCard.getContent());
        assertEquals(testCard.getIsRevealed(), copyCard.getIsRevealed());

        // Modify copy -> No modification of original should occur.
        copyCard.setIsRevealed(true);
        copyCard.setContent("Tower");

        assertNotSame(testCard.getContent(), copyCard.getContent());
        assertNotSame(testCard.getIsRevealed(), copyCard.getIsRevealed());
    }
}
