package ch.uzh.ifi.hase.soprafs24.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs24.classes.Clue;
import ch.uzh.ifi.hase.soprafs24.classes.Game;

@Service
@Transactional
public class GameService {
    // this is the class that is the Game worker.
    // program the game logic here.
    // this is where the controller endpoints and internal game representation meets
    public String handleClue(Clue clue) {
        // handleClue logic
        System.out.println("Handling clue");

        // this should return a game object
        // Game newGame = new Game();
        return "Future game object";
    }

}
