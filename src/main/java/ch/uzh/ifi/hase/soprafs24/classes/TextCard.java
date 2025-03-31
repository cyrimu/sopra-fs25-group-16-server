package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.classes.Card;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.CardColor;


public class TextCard extends Card<String> {

    // Input validation is ommitted for now (Since this is Java sadly eventually Card Factory needs to be created!)
    public TextCard(CardColor color, String word) {
        super(GameType.TEXT, color, word);
    }

    public static boolean isValidWord(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }

        return word.matches("[a-zA-Z]+");
    }

    @Override
    public void setContent(String word) {
        assert (isValidWord(word)) : "Only Strings which consist solely out of letters can be used: i.e. tree or Mensch etc.";
        this.mContent = word;
    }
}