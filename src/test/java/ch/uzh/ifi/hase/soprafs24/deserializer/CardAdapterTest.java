package ch.uzh.ifi.hase.soprafs24.deserializer;

import ch.uzh.ifi.hase.soprafs24.classes.Card;
import ch.uzh.ifi.hase.soprafs24.classes.TextCard;
import ch.uzh.ifi.hase.soprafs24.classes.CardFactory;
import ch.uzh.ifi.hase.soprafs24.constant.CardColor;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;

import com.google.gson.Gson; 
import com.google.gson.GsonBuilder; 
import com.google.gson.TypeAdapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

public class CardAdapterTest {
    private final GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Card.class, new CardAdapter());  
    private final Gson gson = builder.create();
    private final CardFactory factory = new CardFactory();
    private Card testTextCard = (Card) factory.createTextCard(CardColor.BLUE, "cat");


    @BeforeEach
    public void setup() {
       testTextCard = (Card) factory.createTextCard(CardColor.BLUE, "cat");
    }

    @Test
    public void deserializerProducesCorrectJSONStringFromJavaObject() {
        String json = gson.toJson(testTextCard);

        assertEquals("{\"mType\":\"TEXT\",\"mColor\":\"BLUE\",\"mContent\":\"cat\",\"mIsRevealed\":false}", json);
    }

    @Test
    public void deserializerProducesCorrectJavaObjectFromJSONString() {
        Card parsedCard = gson.fromJson("{\"mType\":\"TEXT\",\"mColor\":\"BLUE\",\"mContent\":\"cat\",\"mIsRevealed\":false}", Card.class);

        assertEquals(false, parsedCard.getIsRevealed());
    assertEquals(CardColor.BLUE, parsedCard.getColor());
    assertEquals(GameType.TEXT, parsedCard.getType());
    assertEquals("cat", (String) parsedCard.getContent());
    }
}