package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.classes.TextCard;
import ch.uzh.ifi.hase.soprafs24.constant.CardColor;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;

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

    TextCard[] mCards;

    private boolean isUnique(String word, ArrayList<TextCard> list) {
        boolean solution = true;
        for (TextCard card : list) {
            if (card.getContent().equals(word)) {
                solution = false;
                break;
            }
        }
        return solution;
    }

    private void generateCards(int number, CardColor type, ArrayList<TextCard> list) {
        int wordIndex = 0;
        Random rand = new Random();
        for (int i = 0; i < number; i++) {
            while (!(isUnique(WORDS[wordIndex], list))) {
                wordIndex = rand.nextInt(WORDS.length);
            }
            list.add(new TextCard(WORDS[wordIndex], type));
        }
    }

    private TextCard[] shuffleCards(TextCard[] cards) {
        Random rand = new Random();
        final int NUMBER_SWAPS = 3;
        int swap1;
        int swap2;

        for (int i = 0; i < NUMBER_SWAPS*cards.length; i++){
            swap1 = rand.nextInt(cards.length);
            swap2 = rand.nextInt(cards.length);

            String tmp = cards[swap1].getContent();
            cards[swap1].setContent(cards[swap2].getContent());
            cards[swap2].setContent(tmp);
        }
        return cards;
    }

    public Board(TeamColor firstTeam) {
        ArrayList<TextCard> cardList = new ArrayList<TextCard>(BOARD_SIZE);
        this.mFirstTeam = firstTeam;
        this.mSecondTeam = (firstTeam.equals(TeamColor.RED)) ? TeamColor.BLUE : TeamColor.RED;

        assert (NUM_CARDS_FIRST_TEAM + NUM_CARDS_SECOND_TEAM + NUM_CARDS_BLACK + NUM_CARDS_WHITE) == (BOARD_SIZE) : "Constants do not add up!";
        assert WORDS.length >= BOARD_SIZE : "Not enough unique words are stored to generate a game board!";

        generateCards(NUM_CARDS_FIRST_TEAM, (mFirstTeam.equals(TeamColor.RED)) ? CardColor.RED : CardColor.BLUE, cardList);
        generateCards(NUM_CARDS_SECOND_TEAM, (mFirstTeam.equals(TeamColor.RED)) ? CardColor.BLUE : CardColor.RED, cardList);
        generateCards(NUM_CARDS_BLACK, CardColor.BLACK, cardList);
        generateCards(NUM_CARDS_WHITE, CardColor.WHITE, cardList);

        this.mCards = new TextCard[cardList.size()];
        this.mCards = cardList.toArray(this.mCards);
        this.mCards = shuffleCards(this.mCards);
    }
}