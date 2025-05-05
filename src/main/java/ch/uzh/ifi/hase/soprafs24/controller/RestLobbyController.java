package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.classes.Lobby;
import ch.uzh.ifi.hase.soprafs24.classes.Player;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyUpdateDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlayerDTO;
import ch.uzh.ifi.hase.soprafs24.service.ImageService;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;


@RestController
public class RestLobbyController {

    private final LobbyService lobbyService;
    private final SimpMessagingTemplate messagingTemplate;
    private ImageService imageService; // dont touch this


    public RestLobbyController(SimpMessagingTemplate messagingTemplate, LobbyService lobbyService) {
        this.messagingTemplate = messagingTemplate;
        this.lobbyService = lobbyService;
        this.imageService = new ImageService();
    }

    @PostMapping("/lobby")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Lobby createLobby(@RequestParam String username) {
        // username check
        if (username == null || username.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }

        // Create a new lobby with the username as host
        Lobby createdLobby = lobbyService.createLobby(username);
        
        return createdLobby;
    }
    
    @GetMapping("/lobby/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Lobby getLobby(@PathVariable String lobbyId, String username) {
        
        // username check
        if (username == null || username.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }

        // Fetch the Lobby
        Lobby retrievedLobby = lobbyService.getLobby(lobbyId, username);
        
        return retrievedLobby;
    }

    @PutMapping("/lobby/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Lobby updateLobby(@PathVariable String lobbyId, @RequestBody LobbyUpdateDTO lobbyDTO, @RequestParam String username) {
        System.out.println("Doing updateLobby for: " + lobbyId);
        
        // username check
        if (username == null || username.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }

        // Ensure the path ID matches the lobby ID in the DTO
        if (!lobbyId.equals(lobbyDTO.getLobbyID())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "The lobby ID in the path must match the ID in the request body");
        }
        
        // Check if user is authorized to update (must be host)
        if (!username.equals(lobbyDTO.getHost())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, 
                "Only the host can update the lobby");
        }

        Lobby lobby = convertLobbyUpdateDTOToLobby(lobbyDTO);
        
        // Update lobby using the lobby service
        Lobby updatedLobby = lobbyService.updateLobby(lobbyId, lobby, username);

        messagingTemplate.convertAndSend("/topic/lobby/" + lobbyId, new LobbyDTO(updatedLobby));

        return updatedLobby;
    }

    @PostMapping("/lobby/{lobbyId}/join")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Lobby joinLobby(@PathVariable String lobbyId, @RequestParam String username) {
        // Username check
        if (username == null || username.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }

        System.out.println("User " + username + " is joining lobby: " + lobbyId);

        // Call service method to handle joining logic
        Lobby updatedLobby = lobbyService.joinLobby(lobbyId, username);

        messagingTemplate.convertAndSend("/topic/lobby/" + lobbyId, new LobbyDTO(updatedLobby));

        return updatedLobby;
    }

    @PostMapping("/lobby/{lobbyId}/leave")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Lobby leaveLobby(@PathVariable String lobbyId, @RequestParam String username) {
        // Username check
        if (username == null || username.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }

        System.out.println("User " + username + " is leaving lobby: " + lobbyId);

        Lobby updatedLobby = lobbyService.leaveLobby(lobbyId, username);

        messagingTemplate.convertAndSend("/topic/lobby/" + lobbyId, new LobbyDTO(updatedLobby));

        return updatedLobby;
    }

    @DeleteMapping("/lobby/{lobbyId}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLobby(@PathVariable String lobbyId, @RequestParam String username) {
        // Username check
        if (username == null || username.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }

        System.out.println("User " + username + " is deleting the lobby: " + lobbyId);

        lobbyService.deleteLobby(lobbyId, username);

        messagingTemplate.convertAndSend("/topic/lobby/" + lobbyId, Map.of("type", "delete"));
    }

    @PostMapping("/image/generate")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String generateImage() {
        String imageData = imageService.generateBase64();
        return imageData;
    }

    // For now we use this to construct a new lobby onject which will be used as an update injection for the lobby
    private Lobby convertLobbyUpdateDTOToLobby(LobbyUpdateDTO dto) {
        PlayerDTO[] playerDTOs = dto.getPlayers();
        Player[] players = new Player[playerDTOs.length];
        for (int i = 0; i < playerDTOs.length; i++) {
            PlayerDTO currentDTO = playerDTOs[i];
            if (currentDTO != null) {
                players[i] = new Player(currentDTO.getPlayerName());
                if (currentDTO.getRole() != null) {
                    players[i].setRole(currentDTO.getRole());
                }
                if (currentDTO.getTeam() != null) {
                    players[i].setTeam(currentDTO.getTeam());
                }
            } else {
                players[i] = null;
            }
        }

        return new Lobby(
            dto.getHost(),
            players,
            dto.getType(),
            dto.getLanguage()
        );
    }
    
}
