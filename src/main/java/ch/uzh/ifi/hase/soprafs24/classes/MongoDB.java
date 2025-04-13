package ch.uzh.ifi.hase.soprafs24.classes;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoDB {

    private static final Logger log = LoggerFactory.getLogger(MongoDB.class);
    private MongoClient mongoClient;
    private MongoDatabase database;

    // Replace with your actual connection string and database name
    private static final String CONNECTION_STRING = "mongodb+srv://codenamesClient:codenamesClient@codenamesdbcluser.dh3iiy8.mongodb.net/?retryWrites=true&w=majority&appName=CodenamesDBCluser"; 
    private static final String DATABASE_NAME = "codenamesDB"; 

    public MongoDB() {
        try {
            log.info("Connecting to MongoDB...");
            this.mongoClient = MongoClients.create(CONNECTION_STRING);
            this.database = mongoClient.getDatabase(DATABASE_NAME);
            log.info("Connected to MongoDB database: {}", DATABASE_NAME);
        } catch (MongoException e) {
            log.error("Failed to connect to MongoDB: {}", e.getMessage());
            this.mongoClient = null; 
            this.database = null;
        }
    }

    public MongoDatabase getDatabase() {
        return this.database; // Returns null if connection failed
    }

    public void close() {
        if (this.mongoClient != null) {
            this.mongoClient.close();
            log.info("MongoDB connection closed.");
            this.mongoClient = null;
            this.database = null;
        }
    }
}
