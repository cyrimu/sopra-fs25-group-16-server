package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.classes.Lobby;
import ch.uzh.ifi.hase.soprafs24.classes.Player;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlayerDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyUpdateDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@WebMvcTest(RestLobbyController.class)
public class RestLobbyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LobbyService lobbyService;

    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    private Lobby testLobby;
    private Player[] testPlayers;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        Player hostPlayer = new Player("Alice");
        hostPlayer.setRole(PlayerRoles.BLUE_SPYMASTER);
        hostPlayer.setTeam(TeamColor.BLUE);
        testPlayers = new Player[4];
        testPlayers[0] = hostPlayer;
        testLobby = new Lobby(
                "Alice",
                testPlayers,
                GameType.TEXT,
                SupportedLanguages.ENGLISH
        );
    }

    // GET TESTS

    @Test
    public void getLobby_validInput_lobbyReturned() throws Exception {
        // given
        String lobbyId = testLobby.getLobbyID();
        String username = "Alice";

        given(lobbyService.getLobby(lobbyId, username)).willReturn(testLobby);

        // when
        MockHttpServletRequestBuilder getRequest = get("/lobby/{lobbyId}", lobbyId)
                .param("username", username)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lobbyID", is(lobbyId)))
                .andExpect(jsonPath("$.host", is(testLobby.getHost())))
                .andExpect(jsonPath("$.gameType", is(GameType.TEXT.toString())))
                .andExpect(jsonPath("$.language", is(SupportedLanguages.ENGLISH.toString())))
                .andExpect(jsonPath("$.players", hasSize(4)))
                .andExpect(jsonPath("$.players[0].playerName", is("Alice")))
                .andExpect(jsonPath("$.players[0].role", is(PlayerRoles.BLUE_SPYMASTER.toString())))
                .andExpect(jsonPath("$.players[0].team", is(TeamColor.BLUE.toString())))
                .andExpect(jsonPath("$.players[1]", is(nullValue())))
                .andExpect(jsonPath("$.players[2]", is(nullValue())))
                .andExpect(jsonPath("$.players[3]", is(nullValue())));
    }

    @Test
    public void getLobby_noUsername_throwsBadRequest() throws Exception {
        // given
        String lobbyId = testLobby.getLobbyID();

        // when
        MockHttpServletRequestBuilder getRequest = get("/lobby/{lobbyId}", lobbyId)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getLobby_emptyUsername_throwsBadRequest() throws Exception {
        // given
        String lobbyId = testLobby.getLobbyID();

        // when
        MockHttpServletRequestBuilder getRequest = get("/lobby/{lobbyId}", lobbyId)
                .param("username", "  ")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getLobby_lobbyNotFound_throwsNotFound() throws Exception {
        // given
        String lobbyId = "nonExistentId";
        String username = "Alice";
        given(lobbyService.getLobby(lobbyId, username))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found"));

        // when
        MockHttpServletRequestBuilder getRequest = get("/lobby/{lobbyId}", lobbyId)
                .param("username", username)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());
    }


    // POST TESTS

    @Test
    public void createLobby_validInput_lobbyCreated() throws Exception {
        // given
        String username = "Alice";
        given(lobbyService.createLobby(username)).willReturn(testLobby);

        // when
        MockHttpServletRequestBuilder postRequest = post("/lobby")
                .param("username", username)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lobbyID", is(testLobby.getLobbyID())))
                .andExpect(jsonPath("$.host", is(username)))
                .andExpect(jsonPath("$.gameType", is(GameType.TEXT.toString())))
                .andExpect(jsonPath("$.language", is(SupportedLanguages.ENGLISH.toString())))
                .andExpect(jsonPath("$.players", hasSize(4)))
                .andExpect(jsonPath("$.players[0].playerName", is(username)))
                .andExpect(jsonPath("$.players[0].role", is(PlayerRoles.BLUE_SPYMASTER.toString())))
                .andExpect(jsonPath("$.players[0].team", is(TeamColor.BLUE.toString())))
                .andExpect(jsonPath("$.players[1]", is(nullValue())))
                .andExpect(jsonPath("$.players[2]", is(nullValue())))
                .andExpect(jsonPath("$.players[3]", is(nullValue())));

        verify(lobbyService).createLobby(username);
    }

    @Test
    public void createLobby_noUsername_throwsBadRequest() throws Exception {
        // when
        MockHttpServletRequestBuilder postRequest = post("/lobby")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createLobby_emptyUsername_throwsBadRequest() throws Exception {
        // when
        MockHttpServletRequestBuilder postRequest = post("/lobby")
                .param("username", "   ")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isBadRequest());
    }

    // PUT TESTS

    @Test
    public void updateLobby_validInput_lobbyUpdated() throws Exception {
        // given
        String lobbyId = testLobby.getLobbyID();
        String username = "Alice";

        LobbyUpdateDTO dto = new LobbyUpdateDTO();
        dto.setLobbyID(lobbyId);
        dto.setHost(username);
        dto.setPlayers(new PlayerDTO[] { new PlayerDTO(username), null, null, null });
        dto.setGameType(GameType.TEXT);
        dto.setLanguage(SupportedLanguages.ENGLISH);

        given(lobbyService.updateLobby(eq(lobbyId), any(Lobby.class), eq(username)))
            .willReturn(testLobby);

        // when
        MockHttpServletRequestBuilder putRequest = put("/lobby/{lobbyId}", lobbyId)
            .param("username", username)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto));

        // then
        mockMvc.perform(putRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.lobbyID", is(lobbyId)))
            .andExpect(jsonPath("$.host", is(username)))
            .andExpect(jsonPath("$.gameType", is(GameType.TEXT.toString())))
            .andExpect(jsonPath("$.language", is(SupportedLanguages.ENGLISH.toString())))
            .andExpect(jsonPath("$.players", hasSize(4)))
            .andExpect(jsonPath("$.players[0].playerName", is(username)));

        verify(lobbyService).updateLobby(eq(lobbyId), any(Lobby.class), eq(username));
    }

    @Test
    public void updateLobby_noUsername_throwsBadRequest() throws Exception {
        // given
        String lobbyId = testLobby.getLobbyID();
        LobbyUpdateDTO dto = new LobbyUpdateDTO();
        dto.setLobbyID(lobbyId);

        // when
        MockHttpServletRequestBuilder putRequest = put("/lobby/{lobbyId}", lobbyId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto));

        // then
        mockMvc.perform(putRequest)
            .andExpect(status().isBadRequest());
    }

    @Test
    public void updateLobby_emptyUsername_throwsBadRequest() throws Exception {
        // given
        String lobbyId = testLobby.getLobbyID();
        LobbyUpdateDTO dto = new LobbyUpdateDTO();
        dto.setLobbyID(lobbyId);

        // when
        MockHttpServletRequestBuilder putRequest = put("/lobby/{lobbyId}", lobbyId)
            .param("username", "   ")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto));

        // then
        mockMvc.perform(putRequest)
            .andExpect(status().isBadRequest());
    }

    @Test
    public void updateLobby_idMismatch_throwsBadRequest() throws Exception {
        // given
        String lobbyId = testLobby.getLobbyID();
        LobbyUpdateDTO dto = new LobbyUpdateDTO();
        dto.setLobbyID("noneExistentId");
        dto.setHost("Alice");

        // when
        MockHttpServletRequestBuilder putRequest = put("/lobby/{lobbyId}", lobbyId)
            .param("username", "Alice")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto));

        // then
        mockMvc.perform(putRequest)
            .andExpect(status().isBadRequest());
    }

    @Test
    public void updateLobby_notHost_throwsForbidden() throws Exception {
        // given
        String lobbyId = testLobby.getLobbyID();
        LobbyUpdateDTO dto = new LobbyUpdateDTO();
        dto.setLobbyID(lobbyId);
        dto.setHost("Alice");

        // when
        MockHttpServletRequestBuilder putRequest = put("/lobby/{lobbyId}", lobbyId)
            .param("username", "Bob")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto));

        // then
        mockMvc.perform(putRequest)
            .andExpect(status().isForbidden());
    }

    // JOIN TESTS

    @Test
    public void joinLobby_validInput_lobbyJoined() throws Exception {
        // given
        String lobbyId = testLobby.getLobbyID();
        String username = "Bob";
        Player joinPlayer = new Player(username);
        Player[] joinedPlayers = new Player[4];
        joinedPlayers[0] = testPlayers[0];
        joinedPlayers[1] = joinPlayer;
        Lobby joinedLobby = new Lobby(testLobby.getHost(), joinedPlayers, testLobby.getGameType(), testLobby.getLanguage());
        given(lobbyService.joinLobby(lobbyId, username)).willReturn(joinedLobby);

        // when
        MockHttpServletRequestBuilder joinRequest = post("/lobby/{lobbyId}/join", lobbyId)
            .param("username", username)
            .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(joinRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.players[1].playerName", is(username)));

        verify(lobbyService).joinLobby(eq(lobbyId), eq(username));
    }

    @Test
    public void joinLobby_noUsername_throwsBadRequest() throws Exception {
        // given
        String lobbyId = testLobby.getLobbyID();

        // when
        MockHttpServletRequestBuilder joinRequest = post("/lobby/{lobbyId}/join", lobbyId)
            .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(joinRequest)
            .andExpect(status().isBadRequest());
    }

    // LEAVE TESTS

    @Test
    public void leaveLobby_validInput_lobbyLeft() throws Exception {
        // given
        String lobbyId = testLobby.getLobbyID();
        String username = "Alice";
        Player[] remainingPlayers = new Player[4]; // all null
        Lobby leftLobby = new Lobby(testLobby.getHost(), remainingPlayers, testLobby.getGameType(), testLobby.getLanguage());
        
        // simulating leaving the lobby
        given(lobbyService.leaveLobby(lobbyId, username)).willReturn(leftLobby);

        // when
        MockHttpServletRequestBuilder leaveRequest = post("/lobby/{lobbyId}/leave", lobbyId)
            .param("username", username)
            .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(leaveRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.players[0]", is(nullValue())));

        verify(lobbyService).leaveLobby(eq(lobbyId), eq(username));
    }

    @Test
    public void leaveLobby_noUsername_throwsBadRequest() throws Exception {
        // given
        String lobbyId = testLobby.getLobbyID();

        // when
        MockHttpServletRequestBuilder leaveRequest = post("/lobby/{lobbyId}/leave", lobbyId)
            .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(leaveRequest)
            .andExpect(status().isBadRequest());
    }
}