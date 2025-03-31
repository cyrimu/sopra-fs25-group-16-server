package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.classes.Board;
import ch.uzh.ifi.hase.soprafs24.classes.Card;
import ch.uzh.ifi.hase.soprafs24.classes.TextCard;
import ch.uzh.ifi.hase.soprafs24.constant.CardColor;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.IllegalArgumentException;


public class BoardClassTest {

    @Test
    public void boardConstructor_Succeds() {

        Board testBoard = new Board(GameType.TEXT, TeamColor.BLUE, SupportedLanguages.ENGLISH);
        assertNotNull(testBoard.getCards());
    }

    @Test
    public void boardCardsAreShuffledUponCreation() {

        Board testBoard = new Board(GameType.TEXT, TeamColor.BLUE, SupportedLanguages.ENGLISH);
        Card[] cards = testBoard.getCards();

        // Shuffling will be tested without rigurous statistical verification
        // We will just assume that aslong as the initial order of genereation was changed, shuffling was Successfull

        boolean isShuffled = false;
        for (int i = 0; i < Board.NUM_CARDS_FIRST_TEAM-1; i++) {
            if (cards[i].getColor() != cards[i+1].getColor()) {
                isShuffled = true;
            }
        }
        assertEquals(isShuffled, true);
    }

    @Test
    public void boardCardsAreUniqueUponCreation() {

        Board testBoard = new Board(GameType.TEXT, TeamColor.BLUE, SupportedLanguages.ENGLISH);
        Card[] cards = testBoard.getCards();

        boolean allUnique = true;
        for (int i = 0; i < cards.length; i++) {
            for (int j = i+1; j < cards.length; j++) {
                if (cards[i].getContent().equals(cards[j].getContent())) {
                    allUnique = false;
                    break;
                }
            }
        }
        assertEquals(allUnique, true);
    }

    @Test
    public void boardGetCardsCreatesDeepCopy() {
        
        Board testBoard = new Board(GameType.TEXT, TeamColor.BLUE, SupportedLanguages.ENGLISH);
        Card[] copy1 = testBoard.getCards();
        Card[] copy2 = testBoard.getCards();
        assertNotSame(copy1, copy2);

        for (int i = 0; i < copy1.length; i++) {
            assertNotSame(copy1[i], copy2[i]);
            assertEquals(copy1[i].getType(), copy2[i].getType());
            assertEquals(copy1[i].getColor(), copy2[i].getColor());
            assertEquals(copy1[i].getContent(), copy2[i].getContent());
            assertEquals(copy1[i].getIsRevealed(), copy2[i].getIsRevealed());
        }
    }
}
