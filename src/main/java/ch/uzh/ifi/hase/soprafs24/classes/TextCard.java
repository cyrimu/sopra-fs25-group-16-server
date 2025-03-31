package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.classes.Card;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.CardColor;

import java.lang.IllegalArgumentException;

// In Order to avoid creating factories (Which are somewhat enforced by Java) null fields are temporaryly used for Exception handling
public class TextCard extends Card<String> {

    public TextCard(CardColor color, String word) throws IllegalArgumentException {
        super(GameType.TEXT, color, word, false);
    }

    public TextCard(Card that) throws IllegalArgumentException {
        super(that.getType(), that.getColor(), (String) that.getContent(), that.getIsRevealed());
    }

    public static boolean isValidWord(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }

        return word.matches("[a-zA-Z]+");
    }

    public void setContent(String word) throws IllegalArgumentException {
        if (!(isValidWord(word))) {
            throw new IllegalArgumentException("Only Strings which consist solely out of letters can be used: i.e. tree or Mensch etc.");
        }

        this.mContent = word;
    }
}