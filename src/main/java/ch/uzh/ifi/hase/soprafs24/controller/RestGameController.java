package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.classes.Game;
import ch.uzh.ifi.hase.soprafs24.classes.GameConfiguration;
import ch.uzh.ifi.hase.soprafs24.classes.Player;
import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GameConfigurationDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class RestGameController {

    private final GameService gameService;

    public RestGameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/game/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Game getGame(@PathVariable String gameId, String username) {
        
        // username check
        if (username == null || username.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }

        // Fetch the game
        Game retrievedGame = gameService.getGame(gameId, username);
        

        return retrievedGame;
    }

    @PostMapping("/game")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Game createGame(@RequestBody GameConfigurationDTO gameConfigurationDTO, String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }
        System.out.println("Handling createGame request");
        
        // converting rest object into internal representation of gameConfig
        GameConfiguration gameConfig = convertGameConfigurationDTOtoGameConfiguration(gameConfigurationDTO);

        Game createdGame = gameService.createGame(gameConfig);
        return createdGame;
    }

    // we use a custom conversion function because the normal DTO mapper is not equipped for this kind of objects
    private GameConfiguration convertGameConfigurationDTOtoGameConfiguration(GameConfigurationDTO dto) {
        GameConfiguration gameConfiguration = new GameConfiguration();
        
        List<String> playerNames = dto.getPlayerNames();
        
        Player[] players = new Player[] {   new Player(playerNames.get(0), PlayerRoles.BLUE_SPYMASTER), 
                                            new Player(playerNames.get(1), PlayerRoles.BLUE_OPERATIVE), 
                                            new Player(playerNames.get(2), PlayerRoles.RED_SPYMASTER), 
                                            new Player(playerNames.get(3), PlayerRoles.RED_OPERATIVE)};

        gameConfiguration.setID(UUID.randomUUID().toString().substring(0, Game.ID_LENGTH));
        gameConfiguration.setHost(dto.getHost());
        gameConfiguration.addPlayer(players[0]);
        gameConfiguration.addPlayer(players[1]);
        gameConfiguration.addPlayer(players[2]);
        gameConfiguration.addPlayer(players[3]);

        gameConfiguration.setPlayerRole(playerNames.get(0), PlayerRoles.BLUE_SPYMASTER);
        gameConfiguration.setPlayerRole(playerNames.get(1), PlayerRoles.BLUE_OPERATIVE);
        gameConfiguration.setPlayerRole(playerNames.get(2), PlayerRoles.RED_SPYMASTER);
        gameConfiguration.setPlayerRole(playerNames.get(3), PlayerRoles.RED_OPERATIVE);
        gameConfiguration.setType(dto.getGameType());
        gameConfiguration.setLanguage(dto.getLanguage());
        
        return gameConfiguration;
    }
    
}
