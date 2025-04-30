package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.classes.DeepLTranslator;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

public class DeepLTranslatorTest {

    @Test
    public void TranslationSucceeds() {
        String translatedWord = DeepLTranslator.translateWord("eye", SupportedLanguages.GERMAN);;
        assertEquals("Auge", translatedWord);
    }
}