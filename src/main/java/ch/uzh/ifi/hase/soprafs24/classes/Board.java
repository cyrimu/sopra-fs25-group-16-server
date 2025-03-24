package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.classes.TextCard;
import ch.uzh.ifi.hase.soprafs24.constant.Enums.CardColor;
import ch.uzh.ifi.hase.soprafs24.constant.Enums.TeamColor;

import java.util.Random;
import java.util.ArrayList;

public class Board {
    public static final int BOARD_SIZE = 25;
    public static final int NUM_CARDS_FIRST_TEAM = 9;
    public static final int NUM_CARDS_SECOND_TEAM = 8;
    public static final int NUM_CARDS_BLACK = 1;
    public static final int NUM_CARDS_WHITE = 7;

    private static final String[] WORDS = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

    final TeamColor mFirstTeam;
    final TeamColor mSecondTeam;

    ArrayList<TextCard> mCards;

    private boolean isUnique(String word) {
        boolean solution = true;
        for (TextCard card : mCards) {
            if (card.getContent().isEqual(word)) {
                solution = false;
                break;
            }
        }
        return solution;
    }

    private generateCards(int number, CardType type) {
        int wordIndex = 0;
        for (int i = 0; i < number; i++) {
            while (!(isUnique(WORDS[wordIndex]))) {
                wordIndex = rand.NextInt(WORDS.length);
            }
            mCards.add(new TextCard(WORDS[wordIndex], type));
        }
    }

    public Board(TeamColor firstTeam) {
        this.mCards = new ArrayList<TextCard>(BOARD_SIZE);
        this.mFirstTeam = firstTeam;
        this.mSecondTeam = (firstTeam.isEqual(TeamColor.RED)) ? TeamColor.BLUE : TeamColor.RED;
        
        Random rand = new Random();

        assert (NUM_CARDS_FIRST_TEAM + NUM_CARDS_SECOND_TEAM + NUM_CARDS_BLACK + NUM_CARDS_WHITE).isEqual(BOARD_SIZE) : "Constants do not add up!";
        assert WORDS.length >= BOARD_SIZE : "Not enough unique words are stored to generate a game board!";

        generateCards(NUM_CARDS_FIRST_TEAM, (mFirstTeam.isEqual(TeamColor.RED)) ? CardColor.RED : CardColor.BLUE);
        generateCards(NUM_CARDS_Second_TEAM, (mFirstTeam.isEqual(TeamColor.RED)) ? CardColor.BLUE : CardColor.RED);
        generateCards(NUM_CARDS_BLACK, CardColor.BLACK);
        generateCards(NUM_CARDS_WHITE, CardColor.WHITE);
    }
}