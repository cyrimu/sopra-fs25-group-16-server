package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.classes.Card;
import ch.uzh.ifi.hase.soprafs24.classes.TextCard;
import ch.uzh.ifi.hase.soprafs24.classes.ImageCard;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.CardColor;

import java.lang.IllegalArgumentException;
import java.lang.NullPointerException;

// For the unitiaded this design Pattern is enforced by Java. Checkout the Famous Design Patterns of Programming Book
// Strictly OOP speaking we would have to separate this into multiple Factories but... I think this is enough.
public class CardFactory {

    public TextCard createTextCard(CardColor color, String word) throws IllegalArgumentException, NullPointerException {
        try{TextCard.isValidWord(word);} 
        catch (Exception e) {throw e;}
        if (color == null) {throw new NullPointerException("Class CardFactory; createTextCard: CardColor parameter cannot be null");}
        return new TextCard(color, word);
    }

    public ImageCard createImageCard(CardColor color, String encodedPicture) throws IllegalArgumentException, NullPointerException {
        if(color == null) {throw new NullPointerException("Class CardFactory; createImageCard: CardColor parameter cannot be null");}
        if(encodedPicture == null) {throw new NullPointerException("Class CardFactory; createImageCard: encodedPicture parameter cannot be null");}
        if(encodedPicture.equals("")) {throw new IllegalArgumentException("Class CardFactory; createImageCard: encodedPicture parameter cannot be empty string");}
        return new ImageCard(color, encodedPicture);
    }

    public Card copyCard(Card card) throws NullPointerException {
        if (card == null) {throw new NullPointerException("Class CardFactory; copyCard: Card parameter may never be null");}
        Card copy = (card.getType() == GameType.TEXT) ? new TextCard(card) : new ImageCard(card);
        return copy;
    }
}