package ch.uzh.ifi.hase.soprafs24.controller;

import org.springframework.stereotype.Controller;

import ch.uzh.ifi.hase.soprafs24.classes.Clue;
import ch.uzh.ifi.hase.soprafs24.classes.Game;
import ch.uzh.ifi.hase.soprafs24.classes.Guess;
import ch.uzh.ifi.hase.soprafs24.service.GameService;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @MessageMapping("/game/{gameId}")
    public void handleGameUpdate(@DestinationVariable String gameId, String update) {
        // outdated endpoint but keeping for now
        System.out.println("Received message from client for game " + gameId + ": " + update);
        

        String payload = "[" + getTimestamp() + "] " + update;
        messagingTemplate.convertAndSend("/topic/game/" + gameId, payload);
    }

    @MessageMapping("/game/{gameId}/clue")
    public void handleProvideClue(@DestinationVariable String gameId, Clue clue) {
        System.out.println("Received clue for game " + gameId);
        System.out.println("Clue: " + clue.getClueText() + ", "+ clue.getClueNumber() + " Username: " + clue.getUsername());

        // Call GameService
        
        Game payload = gameService.handleClue(clue);
        messagingTemplate.convertAndSend("/topic/game/" + gameId, payload);
    }

    @MessageMapping("/game/{gameId}/guess")
    public void handleProvideGuess(@DestinationVariable String gameId, Guess guess) {
        System.out.println("Received guess for game " + gameId);
        System.out.println("Guess: " + guess.getcardNumber() + " Username: " + guess.getUsername());

        // Call GameService
        
        String payload = "Guess recieved";
        messagingTemplate.convertAndSend("/topic/game/" + gameId, payload);
    }
    
    @MessageMapping("/game/{gameId}/skipGuess")
    public void handleSkipGuess(@DestinationVariable String gameId, String username) {
        System.out.println("Received skip for game " + gameId);
        System.out.println("username: " + username);

        // Call GameService
        
        String payload = "Received skip from: " + username;
        messagingTemplate.convertAndSend("/topic/game/" + gameId, payload);
    }

    private String getTimestamp() {
        return new SimpleDateFormat("MM/dd/yyyy h:mm:ss a").format(new Date());
    }
}
