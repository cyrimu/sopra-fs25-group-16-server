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

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import javax.imageio.ImageIO;

import ch.uzh.ifi.hase.soprafs24.classes.Card;
import ch.uzh.ifi.hase.soprafs24.classes.MongoDB;

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

    private String compressBase64(String base64) throws IOException {
        System.out.println("Compressing base64 imageee...");

        // getting image bytes
        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        InputStream inputStream = new ByteArrayInputStream(decodedBytes);
        BufferedImage image = ImageIO.read(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        javax.imageio.ImageWriteParam jpegParams = ImageIO.getImageWritersByFormatName("jpg").next().getDefaultWriteParam();
        jpegParams.setCompressionMode(javax.imageio.ImageWriteParam.MODE_EXPLICIT);
        // compression quality 
        jpegParams.setCompressionQuality(0.3f);

        ImageIO.write(image, "jpg", outputStream);
        byte[] compressedBytes = outputStream.toByteArray();

        return Base64.getEncoder().encodeToString(compressedBytes);
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
        
        String compressedBase64;
        try {
            compressedBase64 = compressBase64(base64);
        } catch (IOException e) {
            throw new RuntimeException("Failed to compress base64 image", e);
        }
        saveImage(imageId, compressedBase64);

        return compressedBase64;
    }


    public Map<String, String> generateManyImages(Integer n) {
        OpenAIClient client = OpenAIOkHttpClient.fromEnv();
        Map<String, String> imageMap = new HashMap<>();
        System.out.println("Generating " + n + " images");

        if (n < 1 || n > 25) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "n must be between 1 and 25");
        }

        for (int i = 0; i < n; i++) {
            String a = pick();
            String b = pickDistinct(a);

            String prompt = """
                Use colors such as black and white and grey. No other intense colors.
                The picture should have an illustration of %s and %s with bold black outlines and minimal shading.
                Find a creative way to combine the two subjects so that they are not just side by side.
                """.formatted(a, b);

            ImageGenerateParams params = ImageGenerateParams.builder()
                    .prompt(prompt)
                    .model(MODEL)
                    .quality(ImageGenerateParams.Quality.LOW)
                    .n(1)
                    .build();

            try {
                String base64 = client.images().generate(params).data()
                        .orElseThrow()
                        .stream()
                        .findFirst()
                        .flatMap(image -> image.b64Json())
                        .orElseThrow();

                String compressed = compressBase64(base64);
                String imageId = UUID.randomUUID().toString();
                saveImage(imageId, compressed);
                imageMap.put(imageId, compressed);
            } catch (Exception e) {
                throw new RuntimeException("Failed to generate/compress image " + (i + 1), e);
            }
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
        "bicycle","helmet","ball","glove","skateboard","television","speaker",
        "headphones","charger","cable","battery","clock","calendar","plant","flower",
        "candle","lighter","match","camera","photo","painting","keychain","ring",
        "bracelet","necklace","coin","paper","envelope","stamp","letter","magazine",
        "newspaper","tissue","napkin","sponge","bucket","broom","mop","vacuum",
        "detergent","trashcan","recycling bin","ladder","hammer","nail","screw",
        "screwdriver","wrench","drill","saw","glue","string","rope","chain",
        "basket","sheet","mattress","door","window","curtain","blind","lock","handle",
        "switch","outlet","bulb","fan","heater","thermostat","alarm","whistle","dice",
        "playing card","puzzle","sticker","marker","notepad","highlighter","whiteboard",
        "chalk","blackboard","desk","monitor","keyboard",
        "mousepad","printer","scanner","paperclip","binder","file","folder","stapler",
        "staple","rubber band","clipboard","USB stick","hard drive","router","modem",
        "antenna","screen","tripod","lens","mic","laundry basket","hanger",
        "clothesline","iron","ironing board","dryer","washing machine",
        "dish","pan","pot","oven","microwave","fridge","freezer","stove","blender","mixer",
        "whisk","spatula","ladle","tongs","cup","spoon","cutting board",
        "grater","strainer","colander","peeler","can opener","bottle opener","corkscrew",
        "ice tray","thermos","lunchbox","food container","jar","lid","coaster","placemat",
        "toaster","kettle","coffee maker","espresso machine","tea bag","teapot","salt shaker",
        "pepper grinder","egg timer","pizza cutter","rolling pin",
        "pastry brush","baking tray","muffin tin","cake stand","cookie jar",
        "kitchen scale","fire extinguisher","first aid kit","bandage",
        "thermometer","medicine bottle","pillbox","hand sanitizer","face mask","tweezers",
        "nail clipper","razor","shaving cream","lip balm","makeup brush","eyeliner","mirror compact",
        "perfume","nail polish","hair tie","hair clip","toilet paper","plunger","bath mat",
        "toilet brush","air freshener","scale","bathrobe",
        "slippers","fabric softener","flashlight","lantern",
        "power strip","extension cord","fuse","toolbox","measuring tape",
        "level","work gloves","paintbrush","roller","paint tray","drop cloth","light switch",
        "doorbell","doormat","mailbox","fence","gate","hose","sprinkler","watering can",
        "shovel","rake","hoe","trowel","wheelbarrow","compost bin","bird feeder","bird bath",
        "lawn mower","hedge trimmer","leaf blower","ice scraper","windshield wiper",
        "car key","steering wheel","seatbelt","gas can","toolbelt","helmet light",
        "schedule","nametag","badge","ID card","lanyard","whiteboard eraser",
        "projector","laser pointer","remote control","headrest",
        "passport","boarding pass","suitcase","duffel bag"
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
