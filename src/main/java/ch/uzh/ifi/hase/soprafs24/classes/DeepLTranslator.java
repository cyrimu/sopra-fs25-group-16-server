package ch.uzh.ifi.hase.soprafs24.classes;

import java.util.EnumMap;
import java.util.Map;
import java.lang.RuntimeException;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;
import com.deepl.api.*;

import org.springframework.beans.factory.annotation.Value;


public class DeepLTranslator {
 //   @Value("${DEEPL_API_KEY}") // deepl.key
 //   private static String DEEPL_API_KEY;

    private static final String authKey = "607f0b29-2e43-4455-adf8-938d1842bbcf:fx";
    private static final Translator translator = new Translator(authKey);
    private static final EnumMap<SupportedLanguages, String> languageMap = new EnumMap<>(Map.ofEntries(
        Map.entry(SupportedLanguages.ARABIC, "AR"),
        Map.entry(SupportedLanguages.BULGARIAN, "BG"),
        Map.entry(SupportedLanguages.CZECH, "CS"),
        Map.entry(SupportedLanguages.DANISH, "DA"),
        Map.entry(SupportedLanguages.GERMAN, "DE"),
        Map.entry(SupportedLanguages.GREEK, "EL"),
        Map.entry(SupportedLanguages.ENGLISH, "EN"),
        Map.entry(SupportedLanguages.SPANISH, "ES"),
        Map.entry(SupportedLanguages.ESTONIAN, "ET"),
        Map.entry(SupportedLanguages.FINNISH, "FI"),
        Map.entry(SupportedLanguages.FRENCH, "FR"),
        Map.entry(SupportedLanguages.HUNGARIAN, "HU"),
        Map.entry(SupportedLanguages.INDONESIAN, "ID"),
        Map.entry(SupportedLanguages.ITALIAN, "IT"),
        Map.entry(SupportedLanguages.JAPANESE, "JA"),
        Map.entry(SupportedLanguages.KOREAN, "KO"),
        Map.entry(SupportedLanguages.LITHUANIAN, "LT"),
        Map.entry(SupportedLanguages.LATVIAN, "LV"),
        Map.entry(SupportedLanguages.NORWEGIAN, "NB"),
        Map.entry(SupportedLanguages.DUTCH, "NL"),
        Map.entry(SupportedLanguages.POLISH, "PL"),
        Map.entry(SupportedLanguages.PORTUGUESE, "PT"),
        Map.entry(SupportedLanguages.ROMANIAN, "RO"),
        Map.entry(SupportedLanguages.RUSSIAN, "RU"),
        Map.entry(SupportedLanguages.SLOVAK, "SK"),
        Map.entry(SupportedLanguages.SLOVENIAN, "SL"),
        Map.entry(SupportedLanguages.SWEDISH, "SV"),
        Map.entry(SupportedLanguages.TURKISH, "TR"),
        Map.entry(SupportedLanguages.UKRAINIAN, "UK"),
        Map.entry(SupportedLanguages.CHINESE, "ZH")
    ));


    public static String translateWord(String word, SupportedLanguages language) {
        String translatedWord = "";
        
        try {
            translatedWord = translator.translateText(word, null, languageMap.get(language)).getText();
        }

        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return translatedWord;
    }
}