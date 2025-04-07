package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.classes.Game;
import ch.uzh.ifi.hase.soprafs24.classes.GameConfiguration;
import ch.uzh.ifi.hase.soprafs24.classes.Player;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;
import ch.uzh.ifi.hase.soprafs24.service.GameService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

@WebMvcTest(RestGameController.class)
public class RestGameControllerTest {
    @InjectMocks
    private Player[] testPlayers = {    new Player("A", PlayerRoles.BLUE_SPYMASTER), 
                                        new Player("B", PlayerRoles.BLUE_OPERATIVE), 
                                        new Player("C", PlayerRoles.RED_SPYMASTER), 
                                        new Player("D", PlayerRoles.RED_OPERATIVE)};

    private GameConfiguration testConfiguration = new GameConfiguration();
    private Game testGame = new Game("A", testPlayers, GameType.TEXT, SupportedLanguages.ENGLISH);


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testPlayers = new Player[] {    new Player("A", PlayerRoles.BLUE_SPYMASTER), 
                                        new Player("B", PlayerRoles.BLUE_OPERATIVE), 
                                        new Player("C", PlayerRoles.RED_SPYMASTER), 
                                        new Player("D", PlayerRoles.RED_OPERATIVE)};

        testConfiguration.setID(UUID.randomUUID().toString().substring(0, Game.ID_LENGTH));
        testConfiguration.setHost("A");
        testConfiguration.addPlayer(testPlayers[0]);
        testConfiguration.addPlayer(testPlayers[1]);
        testConfiguration.addPlayer(testPlayers[2]);
        testConfiguration.addPlayer(testPlayers[3]);

        testConfiguration.setPlayerRole("A", PlayerRoles.BLUE_SPYMASTER);
        testConfiguration.setPlayerRole("B", PlayerRoles.BLUE_OPERATIVE);
        testConfiguration.setPlayerRole("C", PlayerRoles.RED_SPYMASTER);
        testConfiguration.setPlayerRole("D", PlayerRoles.RED_OPERATIVE);
        testConfiguration.setType(GameType.TEXT);
        testConfiguration.setLanguage(SupportedLanguages.ENGLISH);

        testGame = new Game(UUID.randomUUID().toString().substring(0,Game.ID_LENGTH),"A", testPlayers, GameType.TEXT, SupportedLanguages.ENGLISH);
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Test
    public void getGame_validInput_gameReturned() throws Exception {
        String gameId = "12345678";
        String username = "testUser";
        
        //Game mockGame = Mockito.mock(Game.class);
        Game testGame = new Game("A", testPlayers, GameType.TEXT, SupportedLanguages.ENGLISH);
        //Game gameSpy = Mockito.spy(testGame);

                
        //Mockito.when(gameSpy.getGameID()).thenReturn(gameId);
        //Mockito.when(gameSpy.getHost()).thenReturn("Alice");
        //Mockito.when(gameSpy.getGameType()).thenReturn(GameType.TEXT);
        //Mockito.when(gameSpy.getLanguage()).thenReturn(SupportedLanguages.ENGLISH);

        given(gameService.getGame(gameId, username)).willReturn(testGame);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/game/{gameId}", gameId)
            .param("username", username)
            .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
            .andExpect(status().isOk())
            // Test that the fields match our expected values
            .andExpect(jsonPath("$.host").value("A"))
            .andExpect(jsonPath("$.gameType").value("TEXT"))
            .andExpect(jsonPath("$.language").value("ENGLISH"));
    }
}