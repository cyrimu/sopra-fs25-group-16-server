package ch.uzh.ifi.hase.soprafs24.classes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StaticPropertyConfiguration {
    public static String TRANSLATION_API_KEY;

    @Value("${deep.key}")
    public void setAPIKey(final String apiKey) {
        TRANSLATION_API_KEY = apiKey;
    }
}