package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.classes.TextCard;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.CardColor;

import java.lang.IllegalArgumentException;

public abstract class Card<T> {
    final GameType mType;
    final CardColor mColor;
    T mContent;
    boolean mIsRevealed;
    
    protected Card(GameType type, CardColor color, T content, boolean isRevealed) throws IllegalArgumentException {
        String errorMessage = null;
        boolean validInput = true;
        if (type == null) {validInput = false; errorMessage = "Class Card; Card Constructor: GameType parameter may not be null";}
        else if (content == null) {validInput = false; errorMessage = "Class Card; Card Constructor: CardColor parameter may not be null";}
        else if (color == null) {validInput = false; errorMessage = "Class Card; Card Constructor: Content parameter may not be null";}
        if (!validInput) {throw new IllegalArgumentException(errorMessage);}
        
        this.mType = type;
        this.mColor = color;
        this.mContent = content;
        this.setIsRevealed(isRevealed);
    }

    public GameType getType() {
        return mType;
    }

    public CardColor getColor() {
        return mColor;
    }

    public T getContent() {
        return mContent;
    }

    public void setIsRevealed(boolean bool) throws IllegalArgumentException {
        this.mIsRevealed = bool;
    }

    public boolean getIsRevealed() {
        return mIsRevealed;
    }
}