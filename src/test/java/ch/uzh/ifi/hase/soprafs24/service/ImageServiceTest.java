package ch.uzh.ifi.hase.soprafs24.service;

import org.junit.jupiter.api.BeforeEach;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.mongodb.client.MongoCollection;

import ch.uzh.ifi.hase.soprafs24.classes.InMemoryStore;
import ch.uzh.ifi.hase.soprafs24.classes.MongoDB;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;
import java.util.UUID;

public class ImageServiceTest {

    @InjectMocks
    private ImageService imageService;
    private MongoCollection<Document> imagesCollection;
    

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        imagesCollection = new MongoDB().getDatabase().getCollection("images");

        InMemoryStore.clear();
    }

    @AfterEach
    public void tearDown() {
        InMemoryStore.clear();
    }


    // integration tests for saveImage and retrieveImage
    @Test
    public void saveImage_storesSingleImage() {
        String imageId = "test-image-" + UUID.randomUUID();
        String base64 = "data:image/png;base64,test123";

        imageService.saveImage(imageId, base64);

        Document doc = imagesCollection.find(new Document("mImageID", imageId)).first();
        assertNotNull(doc);
        assertEquals(imageId, doc.getString("mImageID"));
        assertEquals(base64, doc.getString("base64"));

        imagesCollection.deleteOne(new Document("mImageID", imageId));
    }

    @Test
    public void retrieveImage_returnsExistingImage() {

        Map<String, String> result = imageService.retrieveImage();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsKey("imageId"));
        assertTrue(result.containsKey("base64"));

        // since retrieveImage returns a random image, we cannot check for exact values
        // but we can check if the imageId and base64 are not null and not empty
        assertNotNull(result.get("imageId"));
        assertNotNull(result.get("base64"));

    }
}
