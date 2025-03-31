package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.classes.Team;
import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.IllegalArgumentException;


public class TeamClassTest {

    @Test
    public void teamOneParameterConstructor_Succeds() {

        Team testTeam = new Team(TeamColor.BLUE);

        assertEquals(testTeam.getColor(), TeamColor.BLUE);
        assertEquals(testTeam.getSpymaster().isPresent(), false);
        assertEquals(testTeam.getOperative().isPresent(), false);
    }

    @Test
    public void teamCopyConstructorCreatesDeepCopy() {
        
        Team testTeam = new Team(TeamColor.BLUE);
        testTeam.setOperative("Bob");
        testTeam.setSpymaster("John");
        Team testCopy = new Team(testTeam);
        
        // Assert fields are correctly copied.
        assertEquals(testTeam.getColor(), testCopy.getColor());
        assertEquals(testTeam.getSpymaster().get(), testCopy.getSpymaster().get());
        assertEquals(testTeam.getOperative().get(), testCopy.getOperative().get());

        // Modify copy -> No modification of original should occur.
        testCopy.setOperative("Hugo");
        testCopy.setSpymaster("Fred");

        assertNotSame(testTeam.getSpymaster().get(), testCopy.getSpymaster().get());
        assertNotSame(testTeam.getOperative().get(), testCopy.getOperative().get());
    }
}
