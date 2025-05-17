package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.classes.Card;
import ch.uzh.ifi.hase.soprafs24.classes.CardFactory;
import ch.uzh.ifi.hase.soprafs24.classes.TextCard;
import ch.uzh.ifi.hase.soprafs24.classes.DeepLTranslator;
import ch.uzh.ifi.hase.soprafs24.service.ImageService;
import ch.uzh.ifi.hase.soprafs24.constant.CardColor;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;

import java.util.Random;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import java.lang.NullPointerException;
import java.lang.RuntimeException;
import java.lang.IllegalArgumentException;

public class Board {
    public final int BOARD_SIZE;
    public final int NUM_CARDS_FIRST_TEAM;
    public final int NUM_CARDS_SECOND_TEAM;
    public final int NUM_CARDS_BLACK;
    public final int NUM_CARDS_WHITE;

    private static final String[] WORDS = {
        "ace", "act", "age", "aid", "cab", "can", "cap", "car", "cat", "cog", "ear", "eel", "egg",
        "elf", "elk", "gap", "gas", "ice", "ink", "key", "bow", "box", "bus", "day", "dog", "eye",
        "ship", "plane", "rainbow", "tornado", "weather", "city", "tower", "house", "whale",
        "pistol", "snake", "eagle", "wizard", "monkey", "sword", "bow", "king", "princess",
        "earthquake", "ocean", "bottle", "dinner", "map", "shadow", "ghost", "telephone",
        "singer", "mermaid", "book", "pearl", "crown", "flower", "pencil", "horse", "space", "demon",
        "queen", "prince", "gun", "brazil", "germany", "france", "poland", "switzerland", "trousers",
        "shirt", "cowboy", "rainforest", "tree", "leaf", "coconut", "almond", "apple", "orange",
        "oil", "sunflower", "olive", "star", "river", "castle", "spider", "cactus", "knight",
        "plum", "carrot", "panda", "banana", "jungle", "shell", "turtle", "diamond", "tablet",
        "window", "cloud", "mountain", "bubble", "umbrella", "pillow", "sandal", "bicycle",
        "lighthouse", "television", "basket", "dragon", "robot", "glove", "piano", "brush",
        "squirrel", "peach", "socks", "scarf", "oyster", "flute", "ring", "trophy", "kite", 
        "mango", "taco", "carpet", "forest", "candle", "puzzle", "wallet", "tshirt", "blanket",
        "lobster", "bottle", "guitar", "umbrella", "ladder", "horizon", "keyboard", "microwave",
        "zebra", "cavern", "cupcake", "telescope", "icicle", "dolphin", "piano", "goblin", 
        "spaghetti", "vacation", "plaid", "goblet", "trumpet", "pyramid", "trolley", "cactus",
        "walnut", "yacht", "carousel", "hat", "wig", "notebook", "potion", "fountain", "pyramid",
        "quilt", "helicopter", "sandwich", "jacket", "cylinder", "ring", "skirt", "pendulum",
        "pickle", "pasta", "tambourine", "camera", "laser", "biscuit", "bell", "syrup", 
        "breeze", "chicken", "helmet","noodle", "taco", "yak", "feather", "spoon","tulip", 
        "symphony", "fossil", "cypress", "funnel", "plow", "button", "tiger", "elf",
        "yarn", "funnel", "sundial", "thimble", "nail", "dawn", "river", "mushroom", "spade", 
        "sailor", "shell", "turbine", "knot", "pebble", "talon", "styrofoam", "plaque", 
        "panel", "pepper", "crash", "butter", "turtle", "jet", "crane", "thermal", "grape", 
        "hive", "vortex", "weasel", "marble", "balloon", "yarn", "crystal", "turf", "whistle",
        "wrench", "mascot", "hurricane", "caviar", "crater", "scone", "jelly", "basil", 
        "shamrock", "inkling", "scimitar", "beetle", "steam", "carol", "rack", "raven", 
        "quasar", "cloak", "valve", "net", "quokka", "gondola", "torrid", "hemlock", "quiver"
    };

    private static final CardFactory creator =  new CardFactory();
    private static final ImageService imageService = new ImageService();
    private HashSet<Integer> usedIndexes;
    private String[] storedImages;
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

        if (gameType == GameType.IMAGE) {
            storedImages = imageService.retrieve20Images();
        }
        else {storedImages = new String[1];}

        this.usedIndexes = new HashSet<Integer>();
        ArrayList<Card> cardList = new ArrayList<Card>(BOARD_SIZE);
        CardColor colorFirstTeam = (firstTeam == TeamColor.RED) ? CardColor.RED : CardColor.BLUE;
        CardColor colorSecondTeam = (firstTeam == TeamColor.RED) ? CardColor.BLUE : CardColor.RED;

        generateCards(NUM_CARDS_FIRST_TEAM, gameType, colorFirstTeam, cardList, language);
        generateCards(NUM_CARDS_SECOND_TEAM, gameType, colorSecondTeam, cardList, language);
        generateCards(NUM_CARDS_BLACK, gameType, CardColor.BLACK, cardList, language);
        generateCards(NUM_CARDS_WHITE, gameType, CardColor.WHITE, cardList, language);

        storedImages = null;
        this.mCards = cardList.toArray(new Card[0]);
        shuffleCards();
    }

    private void generateCards(int number, GameType gameType, CardColor cardType, ArrayList<Card> list, SupportedLanguages language) {
        Random rand = new Random();
        int index = rand.nextInt(WORDS.length);
        String encodedPicture = "";
        for (int i = 0; i < number; i++) {
            if (gameType == GameType.TEXT) {
                index = rand.nextInt(WORDS.length);
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
                index = rand.nextInt(storedImages.length);
                while (usedIndexes.contains(index)) {
                    index = rand.nextInt(storedImages.length);
                }

                if (cardType == CardColor.BLACK){
                    // list.add(creator.createImageCard(cardType, imageService.generateBase64()));
                    usedIndexes.add(index);
                    list.add(creator.createImageCard(cardType, storedImages[index]));
                    storedImages[index] = null;
                }
                else {
                    usedIndexes.add(index);
                    list.add(creator.createImageCard(cardType, storedImages[index]));
                    storedImages[index] = null;
                }
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