package ch.uzh.ifi.hase.soprafs24.service;

import java.util.Objects;
import java.util.Optional;

import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;





@Service
@Transactional
public class ImageService {
    GameService gameService = new GameService();

    //@Value("${openai.api.key}")
    private String API_KEY = "xx";

    public String generateBase64(String prompt, String size) {
        System.out.println("Generating image with prompt: " + prompt + " and size: " + size);

        if (API_KEY == null || API_KEY.isBlank()) {
            throw new IllegalStateException("OPENAI_API_KEY is missing");
        }
    

    
        return prompt;
    }
}
