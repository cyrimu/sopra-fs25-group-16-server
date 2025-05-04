package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.classes.Game;
import ch.uzh.ifi.hase.soprafs24.classes.GameConfiguration;
import ch.uzh.ifi.hase.soprafs24.classes.Player;
import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GameConfigurationDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlayerDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GameDTO;
import ch.uzh.ifi.hase.soprafs24.service.GameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
public class RestGameController {

    private final GameService gameService;

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public RestGameController(SimpMessagingTemplate messagingTemplate, GameService gameService) {
        this.messagingTemplate = messagingTemplate;
        this.gameService = gameService;
    }

    @GetMapping("/game/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameDTO getGame(@PathVariable String gameId, String username) {
        
        // username check
        if (username == null || username.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }

        // Fetch the game
        Game retrievedGame = gameService.retrieveGame(gameId, username);
        GameDTO gameDTO = new GameDTO(retrievedGame);
        return gameDTO;
    }

    @PostMapping("/game/{lobbyId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameDTO createGame(@RequestBody GameConfigurationDTO gameConfigurationDTO, @PathVariable String lobbyId, @RequestParam String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }
        System.out.println("Handling createGame request");
        
        // converting rest object into internal representation of gameConfig
        GameConfiguration gameConfig = convertGameConfigurationDTOtoGameConfiguration(gameConfigurationDTO);

        Game createdGame = gameService.createGame(gameConfig);
        GameDTO gameDTO = new GameDTO(createdGame);

        // Send updated ready list to all clients in the lobby
        Map<String, Object> message = new HashMap<>();
        message.put("type", "game");
        message.put("gameId", gameDTO.getGameID());
        message.put("username", username);

        messagingTemplate.convertAndSend("/topic/lobby/" + lobbyId, message);

        return gameDTO;
    }

    // we use a custom conversion function because the normal DTO mapper is not equipped for this kind of objects
    private GameConfiguration convertGameConfigurationDTOtoGameConfiguration(GameConfigurationDTO dto) {
        GameConfiguration gameConfiguration = new GameConfiguration();

        PlayerDTO[] playerDTOs = dto.getPlayers();
        Player[] players = new Player[playerDTOs.length];

        for (int i = 0; i < playerDTOs.length; i++) {
            players[i] = new Player(playerDTOs[i].getPlayerName());
        }

        gameConfiguration.setID(UUID.randomUUID().toString().substring(0, Game.ID_LENGTH));
        gameConfiguration.setHost(dto.getHost());
        gameConfiguration.addPlayer(players[0]);
        gameConfiguration.addPlayer(players[1]);
        gameConfiguration.addPlayer(players[2]);
        gameConfiguration.addPlayer(players[3]);

        gameConfiguration.setPlayerRole(players[0].getPlayerName(), PlayerRoles.BLUE_SPYMASTER);
        gameConfiguration.setPlayerRole(players[1].getPlayerName(), PlayerRoles.BLUE_OPERATIVE);
        gameConfiguration.setPlayerRole(players[2].getPlayerName(), PlayerRoles.RED_SPYMASTER);
        gameConfiguration.setPlayerRole(players[3].getPlayerName(), PlayerRoles.RED_OPERATIVE);
        gameConfiguration.setType(dto.getGameType());
        gameConfiguration.setLanguage(dto.getLanguage());
        
        return gameConfiguration;
    }
    
}
