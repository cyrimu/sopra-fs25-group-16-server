package ch.uzh.ifi.hase.soprafs24.deserializer;

import java.io.IOException;  

import ch.uzh.ifi.hase.soprafs24.classes.Card;
import ch.uzh.ifi.hase.soprafs24.classes.TextCard;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.CardColor;

import com.google.gson.Gson; 
import com.google.gson.GsonBuilder; 
import com.google.gson.TypeAdapter; 
import com.google.gson.stream.JsonReader; 
import com.google.gson.stream.JsonToken; 
import com.google.gson.stream.JsonWriter; 

public class CardAdapter extends TypeAdapter<Card> { 
    @Override 
    public Card read(JsonReader reader) throws IOException { 
        reader.beginObject();
        String fieldname = null;
        GameType gameMode = null;
        CardColor color = null;
        boolean isRevealed = false;

        //Seperate by Card Type
        String sContent = null;
    
        while (reader.hasNext()) {        
            JsonToken token = reader.peek();

            if (token.equals(JsonToken.NAME)) {
                fieldname = reader.nextName();
            }

            if ("mType".equals(fieldname)) {
                token = reader.peek();
                gameMode = GameType.valueOf(reader.nextString());
            }

            if ("mColor".equals(fieldname)) {
                token = reader.peek();
                color = CardColor.valueOf(reader.nextString());
            }

            if ("mContent".equals(fieldname)) {
                token = reader.peek();
                if (gameMode == GameType.TEXT){
                    sContent = reader.nextString();
                }

                // Implement later
            }

            if ("mIsRevealed".equals(fieldname)) {
                token = reader.peek();
                isRevealed = reader.nextBoolean();
            }
        }
        reader.endObject(); 

        if (sContent != null) {
            return new TextCard(color, sContent, isRevealed);
        }

        else {
            // Implemented later properly (Needed for Compilation)
            return new TextCard(color, sContent, isRevealed);
        }

   } 
   
    @Override 
    public void write(JsonWriter writer, Card card) throws IOException { 
        writer.beginObject(); 
        writer.name("mType"); 
        writer.value(card.getType().name()); 
        writer.name("mColor"); 
        writer.value(card.getColor().name());
        writer.name("mContent"); 
        if (card.getType() == GameType.TEXT){
            writer.value((String) card.getContent()); 
        }
        // Add Image case late!
        writer.name("mIsRevealed"); 
        writer.value(card.getIsRevealed()); 
        writer.endObject(); 
   } 
}