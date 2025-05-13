package ch.uzh.ifi.hase.soprafs24.service;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.model.ReplaceOptions;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.images.ImageGenerateParams;
import com.openai.models.images.ImageModel;

import ch.uzh.ifi.hase.soprafs24.classes.Card;
import ch.uzh.ifi.hase.soprafs24.classes.MongoDB;

import java.io.IOException;

@Service
@Transactional
public class ImageService {
    GameService gameService = new GameService();

    private static final String MODEL  = "gpt-image-1";
    private static final String SIZE   = "1024x1024";
    private static final String QUAL   = "low";

    private static final GsonBuilder builder = new GsonBuilder();
    private static final Gson gson = builder.create();
    private static MongoDB mongoDB = new MongoDB();
    private static MongoDatabase database = mongoDB.getDatabase();
    private static MongoCollection<Document> imagesCollection = database.getCollection("images");

    private static record StoredImage(String mImageID, String base64) {}

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

        String imageId = UUID.randomUUID().toString();
        saveImage(imageId, base64);
    
        return base64;
    }


    public Map<String, String> generateManyImages(Integer n) {
        OpenAIClient client = OpenAIOkHttpClient.fromEnv();
        Map<String, String> imageMap = new HashMap<>();
        System.out.println("Generating " + n + " images");
        
        if (n < 1 || n > 25) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "n must be between 1 and 25");
        }

        String a = pick();
        String b = pickDistinct(a);
    
        String prompt = """
            Use colors such as black and white and grey. No other intense colors.
            The picture should have an illustration of %s and %s with bold black outlines and minimal shading.
            Find a creative way to combine the two subjects so that they are not just side by side.
            """.formatted(a, b);
    
        ImageGenerateParams imageGenerateParams = ImageGenerateParams.builder()
                .prompt(prompt)
                .model(MODEL)
                .quality(ImageGenerateParams.Quality.LOW)
                .n(n)
                .build();
    
        List<String> base64List = client.images().generate(imageGenerateParams).data()
                .orElseThrow()
                .stream()
                .map(image -> image.b64Json().orElseThrow())
                .toList();
    
        for (String base64 : base64List) {
            String imageId = UUID.randomUUID().toString();
            saveImage(imageId, base64);
            imageMap.put(imageId, base64);
        }
    
        return imageMap;
    }
    
    public Map<String, String> retrieveImage() {
        Document randomDoc = imagesCollection.aggregate(List.of(new Document("$sample", new Document("size", 1)))).first();
    
        if (randomDoc == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No images found in the database");
        }
    
        String imageId = randomDoc.getString("mImageID");
        String base64 = randomDoc.getString("base64");
    
        Map<String, String> imageData = new HashMap<>();
        imageData.put("imageId", imageId);
        imageData.put("base64", base64);
        return imageData;
    }

    public String[] retrieve20Images() {
        List<Document> randomDocs = imagesCollection.aggregate(List.of(new Document("$sample", new Document("size", 20)))).into(new ArrayList<>()); 
        String[] images = new String[20];
        int index = 0;
        for (Document doc : randomDocs) {
            images[index] = doc.getString("mImageID");
            index += 1;
        }
        return images;
    }

    public String getImage(String imageID) {
        Document query = new Document("mImageID", imageID); 
        Document image = imagesCollection.find(query).first(); 
        if (image != null) {
            return image.getString("base64");
        } else {
            return "";
        }
    }
    
    public void saveImage(String imageId, String base64Str) {
        if (imageId == null || base64Str == null) {
            throw new IllegalArgumentException("imageId and base64Str must not be null");
        }
    
        String json   = gson.toJson(new StoredImage(imageId, base64Str));
        Document bson = Document.parse(json);
        Document filter        = new Document("mImageID", imageId);
        ReplaceOptions options = new ReplaceOptions().upsert(true);
        imagesCollection.replaceOne(filter, bson, options);

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
