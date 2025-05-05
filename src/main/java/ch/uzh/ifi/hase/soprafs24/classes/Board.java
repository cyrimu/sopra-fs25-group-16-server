package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.classes.Card;
import ch.uzh.ifi.hase.soprafs24.classes.CardFactory;
import ch.uzh.ifi.hase.soprafs24.classes.TextCard;
import ch.uzh.ifi.hase.soprafs24.classes.DeepLTranslator;
import ch.uzh.ifi.hase.soprafs24.constant.CardColor;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;

import java.util.Random;
import java.util.ArrayList;
import java.util.HashSet;

import java.lang.NullPointerException;
import java.lang.RuntimeException;
import java.lang.IllegalArgumentException;

public class Board {
    public final int BOARD_SIZE;
    public final int NUM_CARDS_FIRST_TEAM;
    public final int NUM_CARDS_SECOND_TEAM;
    public final int NUM_CARDS_BLACK;
    public final int NUM_CARDS_WHITE;

    private static final String[] WORDS = {"ace","act","age","aid","cab","can","cap","car","cat","cog","ear","eel","egg","elf","elk","gap","gas","ice","ink","key","bow","box","bus","day","dog","eye"};
    private static final String[] PICTURES = {"a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a"};

    private static final CardFactory creator =  new CardFactory();
    private HashSet<Integer> usedIndexes;
    private Card[] mCards;

    public Board(GameType gameType, TeamColor firstTeam, SupportedLanguages language) throws NullPointerException {
        String errorMessage = null;
        boolean validInput = true;
        if (gameType == null) {validInput = false; errorMessage = "Class Board; Board Constructor: GameType parameter may not be null";}
        else if (firstTeam == null) {validInput = false; errorMessage = "Class Board; Board Constructor: FirstTeam parameter may not be null";}
        else if (language == null) {validInput = false; errorMessage = "Class Board; Board Constructor: Language parameter may not be null";}
        if (!validInput) {throw new NullPointerException(errorMessage);}

        BOARD_SIZE = (gameType == GameType.TEXT) ? 25 : 20;
        NUM_CARDS_FIRST_TEAM = (gameType == GameType.TEXT) ? 9 : 7;
        NUM_CARDS_SECOND_TEAM = (gameType == GameType.TEXT) ? 8 : 6;
        NUM_CARDS_WHITE = (gameType == GameType.TEXT) ? 7 : 6;
        NUM_CARDS_BLACK = 1;

        if (!((NUM_CARDS_FIRST_TEAM + NUM_CARDS_SECOND_TEAM + NUM_CARDS_BLACK + NUM_CARDS_WHITE) == (BOARD_SIZE))) {
            validInput = false;
            errorMessage = "Class Board; Board Constructor: Board Constants do not add up!";
        }
        else if (!(WORDS.length >= BOARD_SIZE)) {validInput = false; errorMessage = "Class Board; Board Constructor: Not Enough Words for generation stored!";}
        if (!validInput) {throw new RuntimeException(errorMessage);}

        this.usedIndexes = new HashSet<Integer>();
        ArrayList<Card> cardList = new ArrayList<Card>(BOARD_SIZE);
        CardColor colorFirstTeam = (firstTeam == TeamColor.RED) ? CardColor.RED : CardColor.BLUE;
        CardColor colorSecondTeam = (firstTeam == TeamColor.RED) ? CardColor.BLUE : CardColor.RED;

        generateCards(NUM_CARDS_FIRST_TEAM, gameType, colorFirstTeam, cardList, language);
        generateCards(NUM_CARDS_SECOND_TEAM, gameType, colorSecondTeam, cardList, language);
        generateCards(NUM_CARDS_BLACK, gameType, CardColor.BLACK, cardList, language);
        generateCards(NUM_CARDS_WHITE, gameType, CardColor.WHITE, cardList, language);

        this.mCards = cardList.toArray(new Card[0]);
        shuffleCards();
    }

    private void generateCards(int number, GameType gameType, CardColor cardType, ArrayList<Card> list, SupportedLanguages language) {
        Random rand = new Random();
        int index = rand.nextInt(WORDS.length);
        for (int i = 0; i < number; i++) {
            if (gameType == GameType.TEXT) {
                while (usedIndexes.contains(index)) {
                    index = rand.nextInt(WORDS.length);
                }

                usedIndexes.add(index);
                String cardWord = WORDS[index];
                if (language != SupportedLanguages.ENGLISH) {
                    cardWord = DeepLTranslator.translateWord(cardWord, language);
                }
                list.add(creator.createTextCard(cardType, cardWord));
            }

            else {
                while (usedIndexes.contains(index)) {
                    index = rand.nextInt(PICTURES.length);
                }

                usedIndexes.add(index);
                String encodedPicture = PICTURES[index];
                list.add(creator.createImageCard(cardType, encodedPicture));
            }
        }
    }

    private void shuffleCards() {
        Random rand = new Random();
        final int NUMBER_SWAPS = 3;
        int swap1;
        int swap2;

        for (int i = 0; i < NUMBER_SWAPS * mCards.length; i++){
            swap1 = rand.nextInt(mCards.length);
            swap2 = rand.nextInt(mCards.length);
            while (swap2 == swap1) {
                swap2 = rand.nextInt(mCards.length);
            }
            Card tmp = mCards[swap1];
            mCards[swap1] = mCards[swap2];
            mCards[swap2] = tmp;
        }
    }

    public Card[] getCards() {
        ArrayList<Card> copiedCards = new ArrayList<Card>(mCards.length);
        for (Card card : mCards) {
            copiedCards.add(creator.copyCard(card));
        }
        return copiedCards.toArray(new Card[0]);
    }

    public void revealCard(int index) {
        mCards[index].setIsRevealed(true);
    }

    public int getBoardSize() {
        return this.BOARD_SIZE;
    }

    public int getNumFirstCards() {
        return this.NUM_CARDS_FIRST_TEAM;
    }

    public int getNumSecondCards() {
        return this.NUM_CARDS_SECOND_TEAM;
    }
}