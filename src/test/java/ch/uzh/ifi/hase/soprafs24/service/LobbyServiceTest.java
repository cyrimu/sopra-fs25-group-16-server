package ch.uzh.ifi.hase.soprafs24.service;

import java.util.UUID;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import ch.uzh.ifi.hase.soprafs24.classes.Game;
import ch.uzh.ifi.hase.soprafs24.classes.GameConfiguration;
import ch.uzh.ifi.hase.soprafs24.classes.Lobby;
import ch.uzh.ifi.hase.soprafs24.classes.Player;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ch.uzh.ifi.hase.soprafs24.classes.InMemoryStore;

public class LobbyServiceTest {

    // Easter Eggs for people who read the code!

    @InjectMocks
    private LobbyService lobbyService;

    private Lobby testLobby;
    private String testLobbyId;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        InMemoryStore.clear();

        String hostUsername = "testHost";
        Player hostPlayer = new Player(hostUsername, PlayerRoles.BLUE_SPYMASTER);
        hostPlayer.setTeam(TeamColor.BLUE);
        Player player1 = new Player("player1", PlayerRoles.RED_SPYMASTER);
        player1.setTeam(TeamColor.RED);
        Player player2 = new Player("player2", PlayerRoles.BLUE_OPERATIVE);
        player2.setTeam(TeamColor.BLUE);
        Player player3 = new Player("player3", PlayerRoles.RED_OPERATIVE);
        player3.setTeam(TeamColor.RED);

        Player[] players = new Player[] { hostPlayer, player1, player2, player3 };
        testLobby = new Lobby(
            hostUsername,
            players,
            GameType.TEXT,
            SupportedLanguages.ENGLISH);
        testLobbyId = testLobby.getLobbyID();

        InMemoryStore.putLobby(testLobbyId, testLobby);
    }

    @AfterEach
    public void tearDown() {

        InMemoryStore.clear();
    }

    // CreateLobby tests

    @Test
    public void createLobby_validUsername_storesAndReturnsNewLobby() {

        InMemoryStore.clear();
        String hostUsername = "newHost";

        Lobby createdLobby = lobbyService.createLobby(hostUsername);

        assertNotNull(createdLobby);
        String createdLobbyId = createdLobby.getLobbyID();
        assertNotNull(createdLobbyId);
        assertEquals(hostUsername, createdLobby.getHost());
        assertEquals(GameType.TEXT, createdLobby.getGameType());
        assertEquals(SupportedLanguages.ENGLISH, createdLobby.getLanguage());
        assertEquals(4, createdLobby.getPlayers().length);

        Player[] players = createdLobby.getPlayers();
        assertNotNull(players[0]);
        assertEquals(hostUsername, players[0].getPlayerName());
        assertEquals(PlayerRoles.BLUE_SPYMASTER, players[0].getRole().orElse(null));
        assertNull(players[1]);
        assertNull(players[2]);
        assertNull(players[3]);


        Optional<Lobby> storedLobbyOpt = InMemoryStore.getLobby(createdLobbyId);
        assertTrue(storedLobbyOpt.isPresent(), "Uh oh! Mesa no finden lobby in da store!");
        assertEquals(createdLobby, storedLobbyOpt.get(), "Oopsie! Da stored lobby no looken like da created lobby!");

    }

    @Test
    public void createLobby_nullUsername_throwsNullPointerException() {

        String nullUsername = null;


        assertThrows(NullPointerException.class, () -> {
            lobbyService.createLobby(nullUsername);
        }, "Whoopsie! Yousa try make lobby wit no name? Dat throwen NullPointerException!");
    }

    @Test
    public void createLobby_emptyUsername_throwsIllegalArgumentException() {

        String emptyUsername = "";

        assertThrows(IllegalArgumentException.class, () -> {
            lobbyService.createLobby(emptyUsername);
        }, "empty username");
    }

    // UpdateLobby tests

    @Test
    public void updateLobby_changeGameTypeAndLanguage_success() {
        Lobby updateRequest = new Lobby(testLobby.getHost(), null, GameType.IMAGE, SupportedLanguages.GERMAN);
        String requestingUser = "testHost";


        Lobby updatedLobby = lobbyService.updateLobby(testLobbyId, updateRequest, requestingUser);

        assertNotNull(updatedLobby);
        assertEquals(testLobbyId, updatedLobby.getLobbyID());
        assertEquals(GameType.IMAGE, updatedLobby.getGameType());
        assertEquals(SupportedLanguages.GERMAN, updatedLobby.getLanguage());
        assertEquals(testLobby.getHost(), updatedLobby.getHost());
        assertArrayEquals(testLobby.getPlayers(), updatedLobby.getPlayers());


        Optional<Lobby> storedLobbyOpt = InMemoryStore.getLobby(testLobbyId);
        assertTrue(storedLobbyOpt.isPresent());
        assertEquals(GameType.IMAGE, storedLobbyOpt.get().getGameType());
        assertEquals(SupportedLanguages.GERMAN, storedLobbyOpt.get().getLanguage());


    }

    @Test
    public void updateLobby_changePlayerRoleAndTeam_success() {

        String playerToUpdate = "player2"; // Blue Operative
        Player updatedPlayerInfo = new Player(playerToUpdate, PlayerRoles.RED_OPERATIVE);
        updatedPlayerInfo.setTeam(TeamColor.RED);
        Player[] playerUpdates = new Player[] { null, null, updatedPlayerInfo, null };

        Lobby updateRequest = new Lobby(testLobby.getHost(), playerUpdates, null, null);
        String requestingUser = "testHost";


        Lobby updatedLobby = lobbyService.updateLobby(testLobbyId, updateRequest, requestingUser);


        assertNotNull(updatedLobby);
        Player[] finalPlayers = updatedLobby.getPlayers();
        Player updatedPlayer = null;
        for (Player p : finalPlayers) {
            if (p != null && p.getPlayerName().equals(playerToUpdate)) {
                updatedPlayer = p;
                break;
            }
        }
        assertNotNull(updatedPlayer, "Where'sa player2? Mesa no see'um after update!");
        assertEquals(PlayerRoles.RED_OPERATIVE, updatedPlayer.getRole().orElse(null));
        assertEquals(TeamColor.RED, updatedPlayer.getTeam().orElse(null));

        Optional<Lobby> storedLobbyOpt = InMemoryStore.getLobby(testLobbyId);
        assertTrue(storedLobbyOpt.isPresent());
        Player storedPlayer = null;
        for (Player p : storedLobbyOpt.get().getPlayers()) {
             if (p != null && p.getPlayerName().equals(playerToUpdate)) {
                storedPlayer = p;
                break;
            }
        }
        assertNotNull(storedPlayer, "Where'sa player2 in da store? Mesa looken everywhere!");
        assertEquals(PlayerRoles.RED_OPERATIVE, storedPlayer.getRole().orElse(null));
        assertEquals(TeamColor.RED, storedPlayer.getTeam().orElse(null));

    }

     @Test
    public void updateLobby_noChanges_returnsUnchangedLobby() {

        Lobby updateRequest = new Lobby(testLobby.getHost(), testLobby.getPlayers(), testLobby.getGameType(), testLobby.getLanguage());
        String requestingUser = "testHost";
        Lobby originalStoredLobby = InMemoryStore.getLobby(testLobbyId).orElseThrow();


        Lobby resultLobby = lobbyService.updateLobby(testLobbyId, updateRequest, requestingUser);


        assertNotNull(resultLobby);
        Optional<Lobby> storedLobbyOpt = InMemoryStore.getLobby(testLobbyId);
        assertTrue(storedLobbyOpt.isPresent());
        assertEquals(originalStoredLobby, storedLobbyOpt.get(), "Hey! Da lobby in da store should be same-same!");
        assertEquals(originalStoredLobby, resultLobby, "Returned lobby should be same-same, no changen!");

    }

    @Test
    public void updateLobby_lobbyNotFound_throwsNotFound() {
        String nonExistentLobbyId = "nonExistent";
        Lobby updateRequest = new Lobby("testHost", null, GameType.IMAGE, null);
        String requestingUser = "testHost";
        InMemoryStore.removeLobby(nonExistentLobbyId);



        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            lobbyService.updateLobby(nonExistentLobbyId, updateRequest, requestingUser);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void updateLobby_gameInProgress_throwsConflict() {
        testLobby.setCurrentGame(mock(Game.class));
        InMemoryStore.putLobby(testLobbyId, testLobby);

        Lobby updateRequest = new Lobby(testLobby.getHost(), null, GameType.IMAGE, null);
        String requestingUser = "testHost";


        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            lobbyService.updateLobby(testLobbyId, updateRequest, requestingUser);
        });
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());

    }

    // --- Tests for joinLobby ---

    @Test
    public void joinLobby_success() {
        Lobby joinableLobby = lobbyService.createLobby("joinHost");
        String joinableLobbyId = joinableLobby.getLobbyID();
        String joiningPlayerName = "joiningPlayer";

        Lobby updatedLobby = lobbyService.joinLobby(joinableLobbyId, joiningPlayerName);

        assertNotNull(updatedLobby);
        Player[] players = updatedLobby.getPlayers();
        assertTrue(Arrays.stream(players).anyMatch(p -> p != null && p.getPlayerName().equals(joiningPlayerName)));
        assertEquals(2, Arrays.stream(players).filter(p -> p != null).count());

        Optional<Lobby> storedLobbyOpt = InMemoryStore.getLobby(joinableLobbyId);
        assertTrue(storedLobbyOpt.isPresent());
        assertTrue(Arrays.stream(storedLobbyOpt.get().getPlayers()).anyMatch(p -> p != null && p.getPlayerName().equals(joiningPlayerName)));
    }

    @Test
    public void joinLobby_lobbyFull_throwsConflict() {
        String joiningPlayerName = "fifthPlayer";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            lobbyService.joinLobby(testLobbyId, joiningPlayerName);
        });
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }

    @Test
    public void joinLobby_lobbyNotFound_throwsNotFound() {
        String nonExistentLobbyId = "nonExistentJoin";
        String joiningPlayerName = "joiningPlayer";
        InMemoryStore.removeLobby(nonExistentLobbyId);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            lobbyService.joinLobby(nonExistentLobbyId, joiningPlayerName);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void joinLobby_playerAlreadyInLobby_throwsConflict() {
        String existingPlayerName = "player1";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            lobbyService.joinLobby(testLobbyId, existingPlayerName);
        });
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }

    // --- Tests for leaveLobby ---

    @Test
    public void leaveLobby_success() {
        String playerToLeave = "player2";

        Lobby updatedLobby = lobbyService.leaveLobby(testLobbyId, playerToLeave);

        assertNotNull(updatedLobby);
        Player[] players = updatedLobby.getPlayers();
        assertFalse(Arrays.stream(players).anyMatch(p -> p != null && p.getPlayerName().equals(playerToLeave)));
        assertEquals(3, Arrays.stream(players).filter(p -> p != null).count());

        Optional<Lobby> storedLobbyOpt = InMemoryStore.getLobby(testLobbyId);
        assertTrue(storedLobbyOpt.isPresent());
        assertFalse(Arrays.stream(storedLobbyOpt.get().getPlayers()).anyMatch(p -> p != null && p.getPlayerName().equals(playerToLeave)));
        assertNull(storedLobbyOpt.get().getPlayers()[2]);
    }

    
    @Test
    public void leaveLobby_playerNotInLobby_throwsNotFound() {
        String nonExistentPlayer = "nonExistentPlayer";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            lobbyService.leaveLobby(testLobbyId, nonExistentPlayer);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void leaveLobby_lobbyNotFound_throwsNotFound() {
        String nonExistentLobbyId = "nonExistent";
        String playerToLeave = "player1";
        InMemoryStore.removeLobby(nonExistentLobbyId);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            lobbyService.leaveLobby(nonExistentLobbyId, playerToLeave);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
}
