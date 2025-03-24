package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.constant.Enums.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.Enums.CardColor;


public class TextCard {
    final GameType mType;
    boolean mIsRevealed;
    final CardColor mColor;
    String mContent;

    private static boolean isValidWord(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }

        return word.matches("[a-zA-Z]+");
    }

    TextCard(String word, CardColor color) {
        assert isValidWord(word) : "Only Strings which consist solely out of letters can be used: i.e. tree or Mensch etc."
        this.mType = GameType.TEXT;
        this.mIsRevealed = false;
        this.mColor = color;
        this.mContent = word;
    }

    public GameType getType() {
        return mType;
    }

    public void setIsRevealed(boolean bool) {
        this.mIsRevealed = bool;
    }

    public boolean getIsRevealed() {
        return mIsRevealed;
    }

    public CardColor getColor() {
        return mColor;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    public String getContent() {
        return mContent;
    }
}