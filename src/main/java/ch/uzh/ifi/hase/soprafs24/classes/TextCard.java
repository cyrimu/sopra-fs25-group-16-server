package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.classes.Card;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.CardColor;

import java.lang.IllegalArgumentException;
import java.lang.NullPointerException;

// In Order to avoid creating factories (Which are somewhat enforced by Java) null fields are temporaryly used for Exception handling
public class TextCard extends Card<String> {

    public TextCard(CardColor color, String word, boolean bool) {
        super(GameType.TEXT, color, word, bool);
    }

    public TextCard(CardColor color, String word) {
        super(GameType.TEXT, color, word, false);
    }

    public TextCard(Card that) {
        super(that.getType(), that.getColor(), (String) that.getContent(), that.getIsRevealed());
    }

    public static void isValidWord(String word) throws IllegalArgumentException, NullPointerException {
        if (word == null) { throw new NullPointerException("Class TextCard; isValidWord: Null is not accepted");}
        else if(word.equals("")) { throw new IllegalArgumentException("Class TextCard; isValidWord: String cannot be empty.");}

        //Maybe later write proper Regex for this but disabled for now since translation uses a lot more symbols...
        // else if (!(word.matches("[a-zA-Z]+"))) { 
        //     String message = String.format("Class TextCard; isValidWord: Only Strings which consist solely out of letters can be used not: %s", word);
        //     throw new IllegalArgumentException(message);};
    }

    public void setContent(String word) throws IllegalArgumentException, NullPointerException {
        try {isValidWord(word);}
        catch (Exception e) {throw e;}
        this.mContent = word;
    }
}