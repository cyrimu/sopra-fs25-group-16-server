package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.classes.Game;
import ch.uzh.ifi.hase.soprafs24.classes.GameConfiguration;
import ch.uzh.ifi.hase.soprafs24.classes.Player;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GameConfigurationDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GameDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlayerDTO;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(RestGameController.class)
public class RestGameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GameService gameService;

    private Game testGame;
    private Player[] testPlayersForGame;
    private GameConfiguration testGameConfigurationInput;
    private GameConfigurationDTO testGameConfigurationDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        Player playerA = new Player("A");
        Player playerB = new Player("B");
        Player playerC = new Player("C");
        Player playerD = new Player("D");
        Player[] configPlayers = new Player[] { playerA, playerB, playerC, playerD };

        PlayerDTO[] playerDTOs = new PlayerDTO[] {
                new PlayerDTO("A"),
                new PlayerDTO("B"),
                new PlayerDTO("C"),
                new PlayerDTO("D")
        };
        testGameConfigurationDTO = new GameConfigurationDTO();
        testGameConfigurationDTO.setHost("A");
        testGameConfigurationDTO.setPlayers(playerDTOs);
        testGameConfigurationDTO.setGameType(GameType.TEXT);
        testGameConfigurationDTO.setLanguage(SupportedLanguages.ENGLISH);

        testGameConfigurationInput = new GameConfiguration();
        testGameConfigurationInput.setHost("A");
        testGameConfigurationInput.addPlayer(playerA);
        testGameConfigurationInput.addPlayer(playerB);
        testGameConfigurationInput.addPlayer(playerC);
        testGameConfigurationInput.addPlayer(playerD);
        testGameConfigurationInput.setPlayerRole("A", PlayerRoles.BLUE_SPYMASTER);
        testGameConfigurationInput.setPlayerRole("B", PlayerRoles.BLUE_OPERATIVE);
        testGameConfigurationInput.setPlayerRole("C", PlayerRoles.RED_SPYMASTER);
        testGameConfigurationInput.setPlayerRole("D", PlayerRoles.RED_OPERATIVE);
        testGameConfigurationInput.setType(GameType.TEXT);
        testGameConfigurationInput.setLanguage(SupportedLanguages.ENGLISH);

        testGame = new Game(testGameConfigurationInput);
        testPlayersForGame = testGame.getPlayers();
    }

    @Test
    public void getGame_validInput_gameReturned() throws Exception {
        String gameId = testGame.getGameID();
        String username = "A";

        given(gameService.retrieveGame(gameId, username)).willReturn(testGame);

        MockHttpServletRequestBuilder getRequest = get("/game/{gameId}", gameId)
                .param("username", username)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameID", is(gameId)))
                .andExpect(jsonPath("$.host", is(testGame.getHost())))
                .andExpect(jsonPath("$.gameType", is(GameType.TEXT.toString())))
                .andExpect(jsonPath("$.language", is(SupportedLanguages.ENGLISH.toString())));
    }

    @Test
    public void getGame_noUsername_throwsBadRequest() throws Exception {
        String gameId = testGame.getGameID();

        MockHttpServletRequestBuilder getRequest = get("/game/{gameId}", gameId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isBadRequest());
    }

     @Test
    public void getGame_emptyUsername_throwsBadRequest() throws Exception {
        String gameId = testGame.getGameID();

        MockHttpServletRequestBuilder getRequest = get("/game/{gameId}", gameId)
                .param("username", " ")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getGame_gameNotFound_throwsNotFound() throws Exception {
        String gameId = "nonExistentId";
        String username = "A";
        given(gameService.retrieveGame(gameId, username))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));

        MockHttpServletRequestBuilder getRequest = get("/game/{gameId}", gameId)
                .param("username", username)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void createGame_validInput_gameCreated() throws Exception {
        String usernameParam = "A";

        given(gameService.createGame(any(GameConfiguration.class))).willReturn(testGame);

        MockHttpServletRequestBuilder postRequest = post("/game")
                .param("username", usernameParam)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(testGameConfigurationDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.gameID", is(testGame.getGameID())))
                .andExpect(jsonPath("$.host", is(testGame.getHost())))
                .andExpect(jsonPath("$.gameType", is(testGame.getGameType().toString())))
                .andExpect(jsonPath("$.language", is(testGame.getLanguage().toString())));

        ArgumentCaptor<GameConfiguration> captor = ArgumentCaptor.forClass(GameConfiguration.class);
        verify(gameService).createGame(captor.capture());
        GameConfiguration capturedConfig = captor.getValue();


        assertEquals(testGameConfigurationDTO.getHost(), capturedConfig.getHost());
        assertEquals(testGameConfigurationDTO.getPlayers().length, capturedConfig.getPlayers().length);
        // Check player names within the captured config
        assertEquals("A", capturedConfig.getPlayers()[0].getPlayerName());
        assertEquals("B", capturedConfig.getPlayers()[1].getPlayerName());
        assertEquals("C", capturedConfig.getPlayers()[2].getPlayerName());
        assertEquals("D", capturedConfig.getPlayers()[3].getPlayerName());
        // Check assigned roles within the captured config using getRolebyName
        assertEquals(PlayerRoles.BLUE_SPYMASTER, capturedConfig.getRolebyName("A").get());
        assertEquals(PlayerRoles.BLUE_OPERATIVE, capturedConfig.getRolebyName("B").get());
        assertEquals(PlayerRoles.RED_SPYMASTER, capturedConfig.getRolebyName("C").get());
        assertEquals(PlayerRoles.RED_OPERATIVE, capturedConfig.getRolebyName("D").get());
        assertEquals(testGameConfigurationDTO.getGameType(), capturedConfig.getType());
        assertEquals(testGameConfigurationDTO.getLanguage(), capturedConfig.getLanguage());
    }

    @Test
    public void createGame_noUsername_throwsBadRequest() throws Exception {
        MockHttpServletRequestBuilder postRequest = post("/game")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(testGameConfigurationDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createGame_emptyUsername_throwsBadRequest() throws Exception {
        MockHttpServletRequestBuilder postRequest = post("/game")
                .param("username", "  ")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(testGameConfigurationDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isBadRequest());
    }

    private String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert object to JSON string", e);
        }
    }
}