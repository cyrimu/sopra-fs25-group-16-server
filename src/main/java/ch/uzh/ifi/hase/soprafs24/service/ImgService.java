package ch.uzh.ifi.hase.soprafs24.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs24.classes.Clue;
import ch.uzh.ifi.hase.soprafs24.classes.Guess;
import ch.uzh.ifi.hase.soprafs24.classes.InMemoryStore;
import ch.uzh.ifi.hase.soprafs24.classes.MongoDB;
import ch.uzh.ifi.hase.soprafs24.classes.Game;
import ch.uzh.ifi.hase.soprafs24.classes.Card;
import ch.uzh.ifi.hase.soprafs24.classes.GameConfiguration;
import ch.uzh.ifi.hase.soprafs24.classes.Player;
import ch.uzh.ifi.hase.soprafs24.classes.Team;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.CardColor;
import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;
import ch.uzh.ifi.hase.soprafs24.deserializer.CardAdapter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
@Transactional
public class ImgService {

    //@Value("${openai.api.key}")
    private String API_KEY = "xx";

    public String generateBase64(String prompt, String size)
        throws IOException, InterruptedException {

    if (API_KEY == null || API_KEY.isBlank()) {
        throw new IllegalStateException("OPENAI_API_KEY is missing");
    }

    String body = String.format(
        "{\"model\":\"dall-e-3\",\"prompt\":\"%s\",\"response_format\":\"b64_json\",\"n\":1}",
        prompt.replace("\"", "\\\""));

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.openai.com/v1/images/generations"))
            .header("Authorization", "Bearer " + API_KEY)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();

    HttpResponse<String> response =
            client.send(request, HttpResponse.BodyHandlers.ofString());

    System.out.println("Response code: " + response.statusCode());
    System.out.println("Response body: " + response.body());

    JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
    String b64 = json.getAsJsonArray("data")
                     .get(0).getAsJsonObject()
                     .get("b64_json").getAsString();

    return "data:image/png;base64," + b64;
}
}