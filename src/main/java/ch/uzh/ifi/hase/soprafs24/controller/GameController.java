package ch.uzh.ifi.hase.soprafs24.controller;

import org.springframework.stereotype.Controller;

import ch.uzh.ifi.hase.soprafs24.classes.Clue;
import ch.uzh.ifi.hase.soprafs24.classes.Game;
import ch.uzh.ifi.hase.soprafs24.classes.Guess;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GameDTO;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;


@Controller
public class GameController {

    private final SimpMessagingTemplate messagingTemplate;
    private final GameService gameService;

    public GameController(SimpMessagingTemplate messagingTemplate, GameService gameService) {
        this.messagingTemplate = messagingTemplate;
        this.gameService = gameService;
    }

    @MessageMapping("/game/{gameId}/clue")
    public void handleProvideClue(@DestinationVariable String gameId, Clue clue) {
        System.out.println("Received clue for game " + gameId);
        System.out.println("Clue: " + clue.getClueText() + ", "+ clue.getClueNumber() + " Username: " + clue.getUsername());

        // Call GameService
        Game modifiedGame = gameService.handleClue(gameId, clue);
        
        messagingTemplate.convertAndSend("/topic/game/" + gameId, new GameDTO(modifiedGame));
    }

    @MessageMapping("/game/{gameId}/guess")
    public void handleProvideGuess(@DestinationVariable String gameId, Guess guess) {
        System.out.println("Received guess for game " + gameId);
        System.out.println("Guess: " + guess.getCardNumber() + " Username: " + guess.getUsername());

        // Call GameService
        Game modifiedGame = gameService.handleGuess(gameId, guess);
        
        messagingTemplate.convertAndSend("/topic/game/" + gameId, new GameDTO(modifiedGame));
    }
    
    @MessageMapping("/game/{gameId}/skipGuess")
    public void handleSkipGuess(@DestinationVariable String gameId, String username) {
        System.out.println("Received skip for game " + gameId);
        System.out.println("username: " + username);

        // Call GameService
        Game modifiedGame = gameService.handleSkip(gameId, username);

        messagingTemplate.convertAndSend("/topic/game/" + gameId, new GameDTO(modifiedGame));
    }

    @MessageMapping("/game/{gameId}/save")
    public void handleSaveGame(@DestinationVariable String gameId) {
        System.out.println("Received save game " + gameId);

        Map<String, Object> message = new HashMap<>();
        message.put("type", "save");
        messagingTemplate.convertAndSend("/topic/game/" + gameId, message);
    }

}
