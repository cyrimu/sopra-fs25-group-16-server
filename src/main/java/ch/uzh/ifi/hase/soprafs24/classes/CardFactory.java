package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.classes.Card;
import ch.uzh.ifi.hase.soprafs24.classes.TextCard;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.CardColor;

import java.lang.IllegalArgumentException;

// For the unitiaded this design Pattern is enforced by Java. Checkout the Famous Design Patterns of Programming Book
// Strictly OOP speaking we would have to separate this into multiple Factories but... I think this is enough.
public class CardFactory {

    public TextCard createTextCard(CardColor color, String word) throws IllegalArgumentException {
        if (!TextCard.isValidWord(word)) {
            throw new IllegalArgumentException("Only Strings which consist solely out of letters can be used: i.e. tree or Mensch etc.");
        }
        return new TextCard(color, word);
    }

    // Will be implemented later!
    // public createImageCard() {
    //     // 
    // }

    public Card copyCard(Card card) {
        Card copy = (card.getType() == GameType.TEXT) ? new TextCard(card) : null; // TODO: Implement Image Card and Substitute;
        return copy;
    }
}