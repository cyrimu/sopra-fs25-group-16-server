package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.classes.Card;
import ch.uzh.ifi.hase.soprafs24.classes.CardFactory;
import ch.uzh.ifi.hase.soprafs24.classes.TextCard;
import ch.uzh.ifi.hase.soprafs24.constant.CardColor;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;

import java.util.Random;
import java.util.ArrayList;

public class Board {
    public static final int BOARD_SIZE = 25;
    public static final int NUM_CARDS_FIRST_TEAM = 9;
    public static final int NUM_CARDS_SECOND_TEAM = 8;
    public static final int NUM_CARDS_BLACK = 1;
    public static final int NUM_CARDS_WHITE = 7;

    private static final String[] WORDS = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    private static final CardFactory creator =  new CardFactory();
    private final Random rand;

    Card[] mCards;

    public Board(GameType gameType, TeamColor firstTeam, SupportedLanguages language) {
        assert (NUM_CARDS_FIRST_TEAM + NUM_CARDS_SECOND_TEAM + NUM_CARDS_BLACK + NUM_CARDS_WHITE) == (BOARD_SIZE) : "Constants do not add up!";
        assert WORDS.length >= BOARD_SIZE : "Not enough unique words are stored to generate a game board!";
        rand = new Random();

        ArrayList<Card> cardList = new ArrayList<Card>(BOARD_SIZE);
        CardColor colorFirstTeam = (firstTeam == TeamColor.RED) ? CardColor.RED : CardColor.BLUE;
        CardColor colorSecondTeam = (firstTeam == TeamColor.RED) ? CardColor.BLUE : CardColor.RED;

        generateCards(NUM_CARDS_FIRST_TEAM, gameType, colorFirstTeam, cardList);
        generateCards(NUM_CARDS_SECOND_TEAM, gameType, colorSecondTeam, cardList);
        generateCards(NUM_CARDS_BLACK, gameType, CardColor.BLACK, cardList);
        generateCards(NUM_CARDS_WHITE, gameType, CardColor.WHITE, cardList);

        this.mCards = cardList.toArray(new Card[0]);
        shuffleCards();
        
        // Will be implemented later
        // if (gameType == GameType.TEXT) {
        //     if (language != SupportedLanguages.ENGLISH){
        //         translateCards(language);
        //     }
        // }
    }


    private void generateCards(int number, GameType gameType, CardColor cardType, ArrayList<Card> list) {
        int index = rand.nextInt(WORDS.length);
        for (int i = 0; i < number; i++) {
            if (gameType == GameType.TEXT) {
                while (!(isUniqueWord(WORDS[index], list))) {
                    index = rand.nextInt(WORDS.length);
                }
                list.add(creator.createTextCard(cardType, WORDS[index]));
            }

            else {
                // Image mode will be implemented later
            }
        }
    }

    private boolean isUniqueWord(String word, ArrayList<Card> list) {
        boolean solution = true;
        for (Card card : list) {
            if (card.getContent().equals(word)) {
                solution = false;
                break;
            }
        }
        return solution;
    }

    private void shuffleCards() {
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
}