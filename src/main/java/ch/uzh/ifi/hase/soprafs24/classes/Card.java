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
        this.mType = type;
        this.mColor = color;
        this.mContent = content;
        this.mIsRevealed = isRevealed;
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

    public void setIsRevealed(boolean bool) {
        this.mIsRevealed = bool;
    }

    public boolean getIsRevealed() {
        return mIsRevealed;
    }
}