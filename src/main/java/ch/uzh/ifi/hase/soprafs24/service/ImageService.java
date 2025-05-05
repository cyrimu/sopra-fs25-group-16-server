package ch.uzh.ifi.hase.soprafs24.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.images.ImageGenerateParams;
import com.openai.models.images.ImageModel;
import java.io.IOException;

@Service
@Transactional
public class ImageService {
    GameService gameService = new GameService();

    private static final String MODEL  = "gpt-image-1";
    private static final String SIZE   = "1024x1024";
    private static final String QUAL   = "low";
    

    public ImageService() {

    }

    public String generateBase64() {


        String a = pick();
        String b = pickDistinct(a);

        String prompt = """
            Use colors such as black and white and grey. No other intense colors.
            The picture should have an illustration of %s and %s with bold black outlines and minimal shading.
            Find a creative way to combine the two subjects so that they are not just side to side.
            """.formatted(a, b);
        
        // ImageRequest body = new ImageRequest(MODEL, prompt, QUAL, "b64_json", 1, SIZE);

        OpenAIClient client = OpenAIOkHttpClient.fromEnv();

        ImageGenerateParams imageGenerateParams = ImageGenerateParams.builder()
                .prompt(prompt)
                .model(MODEL)
                .quality(ImageGenerateParams.Quality.LOW)
//                .size(ImageGenerateParams.Size._1024X1024)
                .n(1)
                .build();


        String base64 = client.images().generate(imageGenerateParams).data().orElseThrow().stream()
                .findFirst()
                .flatMap(image -> image.b64Json())
                .orElseThrow();
    
        return base64;
    }

    private static final List<String> POOL = List.of(
        "car","cat","table","mouse","computer","phone","keys","wallet","bottle","cup",
        "spoon","fork","knife","plate","bowl","chair","couch","lamp","book","notebook",
        "pen","pencil","eraser","ruler","scissors","backpack","umbrella","hat","shoe",
        "sock","belt","watch","glasses","sunglasses","bag","pillow","blanket",
        "toothbrush","toothpaste","soap","shampoo","towel","mirror","comb","hairbrush",
        "bicycle","helmet","ball","glove","skateboard","television","remote","speaker",
        "headphones","charger","cable","battery","clock","calendar","plant","flower",
        "candle","lighter","match","camera","photo","painting","keychain","ring",
        "bracelet","necklace","coin","paper","envelope","stamp","letter","magazine",
        "newspaper","tissue","napkin","sponge","bucket","broom","mop","vacuum",
        "detergent","trashcan","recycling bin","ladder","hammer","nail","screw",
        "screwdriver","wrench","drill","saw","tape","glue","string","rope","chain",
        "basket","sheet","mattress","door","window","curtain","blind","lock","handle",
        "switch","outlet","bulb","fan","heater","thermostat","alarm","whistle","dice",
        "playing card","puzzle","sticker","marker"
    );

    private static String pick() {
        return POOL.get(ThreadLocalRandom.current().nextInt(POOL.size()));
    }
    private static String pickDistinct(String other) {
        String next;
        do next = pick(); while (next.equals(other));
        return next;
    }

    record ImageRequest(
            String model,
            String prompt,
            String quality,
            @JsonProperty("response_format") String responseFormat,
            int n,
            String size) {}

    record Choice(@JsonProperty("b64_json") String b64Json) {}
    record ImageResponse(List<Choice> data) {}
}
