package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.classes.Clue;
import ch.uzh.ifi.hase.soprafs24.classes.Game;
import ch.uzh.ifi.hase.soprafs24.classes.GameConfiguration;
import ch.uzh.ifi.hase.soprafs24.classes.Guess;
import ch.uzh.ifi.hase.soprafs24.classes.Player;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GameConfigurationDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GameDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlayerDTO;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private GameService gameService;

    private GameController gameController;

    private Game testGame;
    private GameConfiguration testGameConfigurationInput;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        gameController = new GameController(messagingTemplate, gameService);

        Player playerA = new Player("A");
        playerA.setRole(PlayerRoles.BLUE_SPYMASTER);
        Player playerB = new Player("B");
        playerB.setRole(PlayerRoles.BLUE_OPERATIVE);
        Player playerC = new Player("C");
        playerC.setRole(PlayerRoles.RED_SPYMASTER);
        Player playerD = new Player("D");
        playerD.setRole(PlayerRoles.RED_OPERATIVE);


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
    }

@Test
void handleProvideClue_sendsGameDTO() {
    Clue clue = new Clue("testHint", 2, "A");
    clue.setUsername("A");

    when(gameService.handleClue(eq(testGame.getGameID()), any(Clue.class)))
            .thenReturn(testGame);
    
    gameController.handleProvideClue(testGame.getGameID(), clue);


    ArgumentCaptor<GameDTO> gameDtoCaptor = ArgumentCaptor.forClass(GameDTO.class);
    verify(messagingTemplate, times(1)).convertAndSend(eq("/topic/game/" + testGame.getGameID()), gameDtoCaptor.capture());

    GameDTO sentGameDto = gameDtoCaptor.getValue();
    assertNotNull(sentGameDto);
    assertEquals(testGame.getGameID(), sentGameDto.getGameID());
    }

@Test
void handleProvideGuess_sendsGameDTO() {
    Guess guess = new Guess(5, "B");


    when(gameService.handleGuess(eq(testGame.getGameID()), any(Guess.class)))
            .thenReturn(testGame);

    gameController.handleProvideGuess(testGame.getGameID(), guess);

    ArgumentCaptor<GameDTO> gameDtoCaptor = ArgumentCaptor.forClass(GameDTO.class);
    verify(messagingTemplate, times(1)).convertAndSend(eq("/topic/game/" + testGame.getGameID()), gameDtoCaptor.capture());

    GameDTO sentGameDto = gameDtoCaptor.getValue();
    assertNotNull(sentGameDto);
    assertEquals(testGame.getGameID(), sentGameDto.getGameID());
    }

@Test
void handleSkipGuess_sendsGameDTO() {
    String username = "B";

    when(gameService.handleSkip(eq(testGame.getGameID()), eq(username)))
            .thenReturn(testGame);

    gameController.handleSkipGuess(testGame.getGameID(), username);

    ArgumentCaptor<GameDTO> gameDtoCaptor = ArgumentCaptor.forClass(GameDTO.class);
    verify(messagingTemplate, times(1)).convertAndSend(eq("/topic/game/" + testGame.getGameID()), gameDtoCaptor.capture());

    GameDTO sentGameDto = gameDtoCaptor.getValue();
    assertNotNull(sentGameDto);
    assertEquals(testGame.getGameID(), sentGameDto.getGameID());
    }


@Test
void handleSaveGame_sendsSaveMessage() {
    gameController.handleSaveGame(testGame.getGameID());

    ArgumentCaptor<Map<String, Object>> messageCaptor = ArgumentCaptor.forClass(Map.class);
    verify(messagingTemplate, times(1)).convertAndSend(eq("/topic/game/" + testGame.getGameID()), messageCaptor.capture());

    Map<String, Object> message = messageCaptor.getValue();
    assertNotNull(message);
    assertEquals("save", message.get("type"));
    }

    
}
