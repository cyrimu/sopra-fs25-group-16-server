package ch.uzh.ifi.hase.soprafs24.service;

import java.util.Objects;
import java.util.Optional;

import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;

import ch.uzh.ifi.hase.soprafs24.classes.Card;
import ch.uzh.ifi.hase.soprafs24.classes.Game;
import ch.uzh.ifi.hase.soprafs24.classes.InMemoryStore;
import ch.uzh.ifi.hase.soprafs24.classes.Lobby;
import ch.uzh.ifi.hase.soprafs24.classes.MongoDB;
import ch.uzh.ifi.hase.soprafs24.classes.Player;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;
import ch.uzh.ifi.hase.soprafs24.deserializer.CardAdapter;

@Service
@Transactional
public class LobbyService {
    GameService gameService = new GameService();
    private static final GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Card.class, new CardAdapter());
    private static final Gson gson = builder.create();
    private static MongoDB mongoDB = new MongoDB();
    private static MongoDatabase database = mongoDB.getDatabase();
    private static MongoCollection<Document> lobbiesCollection = database.getCollection("lobbies");

    public Lobby getLobby(String LobbyId, String username) {
        System.out.println("Handling getLobby request");

        Lobby retrievedLobby = retrieveLobby(LobbyId, username);

        // verifying Lobby existance
        if (retrievedLobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found with ID: " + LobbyId);
        }

        // verifying user is in the lobby
        // seems like we can remove this actually its not very useful and would simplify the other implementation
        // if (java.util.Arrays.stream(retrievedLobby.getPlayers()).noneMatch(player -> player.getPlayerName().equals(username))) {
        // throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not a part of this game");
        // }
        return retrievedLobby;
    }

    public Lobby createLobby(String username) {
        System.out.println("Creating lobby for user: " + username);

        if (username == null) {
            throw new NullPointerException("Username cannot be null when creating a lobby.");
        }
        if (username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty when creating a lobby.");
        }
        

        Player hostPlayer = new Player(username, PlayerRoles.BLUE_SPYMASTER);
        Player[] players = new Player[] { hostPlayer, null, null, null };
        Lobby newLobby = new Lobby(
            username,
            players,
            GameType.TEXT,
            SupportedLanguages.ENGLISH);
        String json = gson.toJson(newLobby);
        Document lobbyDocument = Document.parse(json);

        Document filter = new Document("mlobbyID", newLobby.getLobbyID());
        ReplaceOptions options = new ReplaceOptions().upsert(true);
        lobbiesCollection.replaceOne(filter, lobbyDocument, options);
        InMemoryStore.putLobby(newLobby.getLobbyID(), newLobby);

        return newLobby;
    }

    public Lobby updateLobby(String lobbyId, Lobby lobbyUpdateRequest, String username) {
        // decided to handle the lobby changes like this for now, could also implement more endpoints for each change but this is more scalable mabye?
        System.out.println("Handling updateLobby request for lobby: " + lobbyId + " by user: " + username);
        boolean updated = false;
        Lobby existingLobby = retrieveLobby(lobbyId, username);

        if (existingLobby.getCurrentGame() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cannot update lobby that has a game in progress");
        }

        // Updating GameType
        System.out.println("Updating GameType from " + existingLobby.getGameType() + " to " + lobbyUpdateRequest.getGameType());
        if (lobbyUpdateRequest.getGameType() != null && !lobbyUpdateRequest.getGameType().equals(existingLobby.getGameType())) {
            System.out.println("Updating GameType from " + existingLobby.getGameType() + " to " + lobbyUpdateRequest.getGameType());
            existingLobby.setGameType(lobbyUpdateRequest.getGameType());
            updated = true;
        }

        // Updating Language
        if (lobbyUpdateRequest.getLanguage() != null && !lobbyUpdateRequest.getLanguage().equals(existingLobby.getLanguage())) {
            System.out.println("Updating Language from " + existingLobby.getLanguage() + " to " + lobbyUpdateRequest.getLanguage());
            existingLobby.setLanguage(lobbyUpdateRequest.getLanguage());
            updated = true;
        }

        if (lobbyUpdateRequest.getPlayers() != null) {
            System.out.println("Updating Players in lobby: " + lobbyId);
            Player[] requestedPlayers = lobbyUpdateRequest.getPlayers();
            Player[] existingPlayers = existingLobby.getPlayers();

            for (Player requestedPlayer : requestedPlayers) {
                // Skip null entries in the request array
                if (requestedPlayer == null || requestedPlayer.getPlayerName() == null) {
                    continue;
                }

                // looping through the players
                for (Player existingPlayer : existingPlayers) {
                    if (existingPlayer != null && requestedPlayer.getPlayerName().equals(existingPlayer.getPlayerName())) {

                        
                        PlayerRoles requestedRole = requestedPlayer.getRole().orElse(null);
                        PlayerRoles existingRoleValue = existingPlayer.getRole().orElse(null);

                        // Update Role if different and provided in request
                        if (requestedRole != null && !Objects.equals(requestedRole, existingRoleValue)) {
                            existingPlayer.setRole(requestedRole);
                            updated = true;
                        }


                        TeamColor requestedTeam = requestedPlayer.getTeam().orElse(null);
                        TeamColor existingTeamValue = existingPlayer.getTeam().orElse(null);

                        // Update Team if different and provided in request
                        if (requestedTeam != null && !Objects.equals(requestedTeam, existingTeamValue)) {
                            existingPlayer.setTeam(requestedTeam);
                            updated = true;
                        }
                        break;
                    }
                }
            }
        }

        // space for more updates in the future

        // saving changes if any were made
        if (updated) {
            System.out.println("Saving updated lobby: " + lobbyId);
            saveLobby(existingLobby);
        }

        return existingLobby;
    }

    private void saveLobby(Lobby lobby) {
        InMemoryStore.putLobby(lobby.getLobbyID(), lobby);

        // Updating DB
        String json = gson.toJson(lobby);
        Document lobbyDocument = Document.parse(json);

        Document filter = new Document("mlobbyID", lobby.getLobbyID());
        ReplaceOptions options = new ReplaceOptions().upsert(true);
        lobbiesCollection.replaceOne(filter, lobbyDocument, options);
    }

    private void deleteLobbyWithId(String lobbyId) {
        // Remove from in-memory store
        InMemoryStore.removeLobby(lobbyId);

        // Remove from MongoDB
        Document filter = new Document("mlobbyID", lobbyId); // Ensure this matches your save filter
        lobbiesCollection.deleteOne(filter);

        System.out.println("Lobby " + lobbyId + " deleted.");
    }

    public Lobby joinLobby(String lobbyId, String username) {
        System.out.println("Handling joinLobby request for lobby: " + lobbyId + " by user: " + username);

        Lobby lobby = retrieveLobby(lobbyId, username);
        Player newPlayer = new Player(username);

        // handling game in progress exception
        if (lobby.getCurrentGame() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cannot join lobby with a game in progress");
        }

        // checking if player with same username is already in the lobby
        if (java.util.Arrays.stream(lobby.getPlayers())
                .filter(player -> player != null)
                .anyMatch(player -> player.getPlayerName().equals(username))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A player with this username is already in the lobby");
        }

        boolean added = lobby.addPlayer(newPlayer);
        if (!added) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cannot join lobby because it is already full");
        }

        // saigng
        saveLobby(lobby);

        return lobby;
    }

    public Lobby leaveLobby(String lobbyId, String username) {
        System.out.println("Handling leaveLobby request for lobby: " + lobbyId + " by user: " + username);

        Lobby lobby = retrieveLobby(lobbyId, username);

        // checking if game is in progress (optional)
        if (lobby.getCurrentGame() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Cannot leave lobby with a game in progress");
        }

        // removing the player (set slot to null)
        boolean removed = false;
        Player[] players = lobby.getPlayers();
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null && players[i].getPlayerName().equals(username)) {
                players[i] = null;
                removed = true;
                break;
            }
        }

        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found in lobby.");
        }


        saveLobby(lobby);

        return lobby;
    }

    public void deleteLobby(String lobbyId, String username) {
        System.out.println("Handling deleteLobby request for lobby: " + lobbyId + " by user: " + username);

        Lobby lobby = retrieveLobby(lobbyId, username);

        // check if the username is not the host
        if (!Objects.equals(lobby.getHost(), username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Cannot delete the lobby if the user is not the host");
        }

        deleteLobbyWithId(lobby.getLobbyID());
    }

    private Lobby retrieveLobby(String lobbyId, String username) {

        Optional<Lobby> lobby = InMemoryStore.getLobby(lobbyId);
        if (!lobby.isPresent()) {
            lobby = loadLobbyFromDatabase(lobbyId);
        }

        if (!lobby.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Lobby not found with ID: " + lobbyId);
        }

        return lobby.get();
    }

    private Optional<Lobby> loadLobbyFromDatabase(String lobbyId) {
        Document query = new Document("lobbyID", lobbyId);
        Document lobbyDocument = lobbiesCollection.find(query).first();

        if (lobbyDocument != null) {
            String lobbyJson = lobbyDocument.toJson();
            Lobby loadedLobby = gson.fromJson(lobbyJson, Lobby.class);

            if (loadedLobby != null) {
                InMemoryStore.putLobby(loadedLobby.getLobbyID(), loadedLobby);
                return Optional.of(loadedLobby);
            }
        }
        return Optional.empty();
    }

    private static Lobby createSampleLobby() {
        Player p1 = new Player("Alice", PlayerRoles.BLUE_SPYMASTER);
        Player p2 = new Player("Bob", PlayerRoles.BLUE_OPERATIVE);
        Player p3 = new Player("Carol", PlayerRoles.RED_SPYMASTER);
        Player p4 = new Player("David", PlayerRoles.RED_OPERATIVE);
        Player[] players = new Player[] { p1, p2, p3, p4 };

        // Build the new game with example values
        Lobby newLobby = new Lobby(
                "Alice",
                players,
                GameType.TEXT,
                SupportedLanguages.ENGLISH);
        return newLobby;
    }

}
