package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.classes.Card;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.CardColor;

import java.lang.IllegalArgumentException;
import java.lang.NullPointerException;

// In Order to avoid creating factories (Which are somewhat enforced by Java) null fields are temporaryly used for Exception handling
public class ImageCard extends Card<String> {

    public ImageCard(CardColor color, String encodedPicture, boolean bool) {
        super(GameType.IMAGE, color, encodedPicture, bool);
    }

    public ImageCard(CardColor color, String encodedPicture) {
        super(GameType.IMAGE, color, encodedPicture, false);
    }

    public ImageCard(Card that) {
        super(that.getType(), that.getColor(), (String) that.getContent(), that.getIsRevealed());
    }

    public void setContent(String encodedPicture) throws IllegalArgumentException, NullPointerException {
        if(encodedPicture == null) {throw new NullPointerException("ImageCard: setContent; Encoded picture cannot be null!");}
        if(encodedPicture.equals("")) {throw new IllegalArgumentException("ImageCard: setContent; Encoded picture cannot be empty String!");}
        this.mContent = encodedPicture;
    }
}