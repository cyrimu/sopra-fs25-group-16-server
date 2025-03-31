package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.classes.TextCard;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.CardColor;

public abstract class Card<T> {
    final GameType mType;
    final CardColor mColor;
    T mContent;
    boolean mIsRevealed;

    public Card(GameType type, CardColor color, T content) {
        this.mType = type;
        this.mColor = color;
        this.mContent = content;
        this.mIsRevealed = false;
    }

    protected Card(GameType type, CardColor color, T content, boolean isRevealed) {
        this.mType = type;
        this.mColor = color;
        this.mContent = content;
        this.mIsRevealed = isRevealed;
    }

    public Card(Card that) {
        this(that.getType(), that.getColor(), (T) that.getContent(), that.getIsRevealed());
    }

    public GameType getType() {
        return mType;
    }

    public CardColor getColor() {
        return mColor;
    }

    public void setContent(T content) {
        this.mContent = content;
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