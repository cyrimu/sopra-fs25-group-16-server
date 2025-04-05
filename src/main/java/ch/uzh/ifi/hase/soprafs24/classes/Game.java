package ch.uzh.ifi.hase.soprafs24.classes;

import ch.uzh.ifi.hase.soprafs24.classes.Player;
import ch.uzh.ifi.hase.soprafs24.classes.Team;
import ch.uzh.ifi.hase.soprafs24.classes.Board;

import ch.uzh.ifi.hase.soprafs24.constant.GameType;
import ch.uzh.ifi.hase.soprafs24.constant.SupportedLanguages;
import ch.uzh.ifi.hase.soprafs24.constant.PlayerRoles;
import ch.uzh.ifi.hase.soprafs24.constant.TeamColor;

import java.util.UUID;
import java.util.ArrayList;
import java.util.Optional;
import java.util.NoSuchElementException;
import java.lang.IllegalArgumentException;
import java.lang.NullPointerException;
import java.lang.RuntimeException;


public class Game {
    public static final int ID_LENGTH = 8;
    public static final int MAX_NUM_PLAYERS = 4;
    public static final int MIN_NUM_PLAYERS = 4;

    final String mGameID;
    final String mHost;
    Player[] mPlayers;
    GameType mType;
    SupportedLanguages mLanguage;
    Team mBlueTeam;
    Team mRedTeam;
    Board mBoard;
    PlayerRoles mTurn;
    TeamColor mFirstTeam;
    int mRemainingGuesses;
    TeamColor mWinner;
    ArrayList<String> mLog;

    public Game(GameConfiguration gameConfiguration) {
        this(UUID.randomUUID().toString().substring(0,ID_LENGTH), gameConfiguration.getHost(), gameConfiguration.getPlayers(), gameConfiguration.getType(), gameConfiguration.getLanguage());
    }

    public Game(String gameID, GameConfiguration gameConfiguration) {
        this(gameID, gameConfiguration.getHost(), gameConfiguration.getPlayers(), gameConfiguration.getType(), gameConfiguration.getLanguage());
    }

    public Game(String host, Player[] players, GameType type, SupportedLanguages language) {
        this(UUID.randomUUID().toString().substring(0,ID_LENGTH), host, players, type, language);
    }

    public Game(String gameID, String host, Player[] players, GameType type, SupportedLanguages language) throws IllegalArgumentException, NullPointerException {
        String errorMessage = null;
        boolean validInput = true;
        if (gameID == null) {validInput = false; errorMessage = "Class Game; Game Constructor: GameID cannot be null";}
        else if (host == null || host.equals("")) {validInput = false; errorMessage = "Class Game; Game Constructor: Host parameter cannot be null or empty";}
        else if (type == null) {validInput = false; errorMessage = "Class Game; Game Constructor: GameType parameter cannot be null";}
        else if (language == null) {validInput = false; errorMessage = "Class Game; Game Constructor: Language parameter cannot be null";}
        if (!validInput) {throw new NullPointerException(errorMessage);}

        if (gameID.length() != ID_LENGTH) {throw new IllegalArgumentException(String.format("Class Game; Game Constructor: GameID needs to be of correct length: %2d", ID_LENGTH));}


        this.mGameID = gameID;
        // Helper Function in order to ensure correct order of Initializing Teams and Players;
        this.initializeTeamsAndPlayers(players);
        
        // Needs to occur after Players intialization -> Check if host in Players
        boolean hostFound = false;
        for (Player player : this.mPlayers) {
            if (player.getPlayerName().equals(host)) {hostFound = true;}
        }
        if (hostFound) {this.mHost = host;}
        else {throw new IllegalArgumentException("Class Game; Game Constructor: Host must be a Player");}

        // Reason for private setter to reset Game state on finishing of Game -> Avoid verification duplication
        this.setGameType(type);
        this.setLanguage(language);
        this.setFirstTeam(TeamColor.BLUE);

        this.mTurn = (this.getFirstTeam() == TeamColor.BLUE) ? PlayerRoles.BLUE_SPYMASTER : PlayerRoles.RED_SPYMASTER;
        this.mBoard = new Board(type, this.mFirstTeam, this.mLanguage);
        this.mRemainingGuesses = 0;
        this.mWinner = null;
        this.mLog = new ArrayList<String>();
    }

    public Optional<PlayerRoles> getRolebyName (String playerName) {
        Optional<PlayerRoles> opt = Optional.empty();
        for (Player player : mPlayers) {
            if (playerName.equals(player.getPlayerName())) {
                if (player.getRole().isPresent()){
                    opt = Optional.of(player.getRole().get());
                    break;
                }
                else {
                    // There should be a 0% chance of this ever happening!
                    throw new RuntimeException("Class Game; getRolebyName: Internal Player assignment was somehow modified after Game initialization");
                }
            }
        }
        return opt;
    }

    public Optional<String> getNamebyRole (PlayerRoles role) {
        Optional<String> opt = Optional.empty();
        for (Player player : mPlayers) {
            if (player.getRole().isPresent()) {
                if (role == player.getRole().get()) {
                    opt = Optional.of(player.getPlayerName());
                    break;
                }
            }
            else {
                // There should be a 0% chance of this ever happening!
                throw new RuntimeException("Class Game; getNamebyRole: Internal Player assignment was somehow modified after Game initialization");
            }
        }
        return opt;
    }

    // private void logTurn(PlayerRoles role, String word) throws IllegalArgumentException {
    //     String playerName = getNamebyRole(role).get();
    //     String roleMessage = "";
    //     if ((role == PlayerRoles.BLUE_OPERATIVE || role == PlayerRoles.RED_OPERATIVE)) {
    //         roleMessage = " guessed the following word: ";
    //     }
    //     else {
    //         roleMessage = " provided the following Hint: ";
    //     }
    //     String logMessage = "%s" + roleMessage + "%s";
    //     mLog.add(String.format(logMessage, playerName, word));
    // }

    public String[] getLog() {
        String[] log = mLog.toArray(new String[0]);
        return log;
    }

    public String getGameID() {
        return mGameID;
    }

    public String getHost() {
        return mHost;
    }

    public Player[] getPlayers() {
        return new Player[] {new Player(mPlayers[0]), new Player(mPlayers[1]), new Player(mPlayers[2]), new Player(mPlayers[3])};
    }

    public Card[] getCards() {
        return mBoard.getCards();
    }

    public GameType getGameType() {
        return mType;
    }

    public SupportedLanguages getLanguage() {
        return mLanguage;
    }

    public TeamColor getFirstTeam() {
        return mFirstTeam;
    }

    public PlayerRoles getTurn() {
        return mTurn;
    }

    public int getRemainingGuesses() {
        return mRemainingGuesses;
    }

    public Optional<TeamColor> getWinner() {
        return (mWinner == null) ? Optional.empty() : Optional.of(mWinner);
    }

    private void initializeTeamsAndPlayers(Player[] players) throws IllegalArgumentException, NullPointerException {
        // ORDER IS IMPORTANT: REASON WHY HELPER FUNCTION EXISTS!
        this.createTeams(players);
        this.fillPlayerArray(players);
        if (!(playerRolesAndTeamAssignmentMatch(players, mBlueTeam, mRedTeam))) {
            throw new IllegalArgumentException("Class Game; initializeTeamsAndPlayers: Team Roles and Player Roles have to be the same");
        }
        this.assertAllRolesOccupied();
    }

    private void fillPlayerArray(Player[] players) throws IllegalArgumentException, NullPointerException {
        String errorMessage = null;
        boolean validInput = true;
        if (players == null) {throw new NullPointerException("Class Game; fillPlayerArray: List of Players cannot be null");}
        else if (players.length > MAX_NUM_PLAYERS) {
            validInput = false;
            errorMessage = String.format("Class Game; fillPlayerArray: Only Maximally %2d Players can play", MAX_NUM_PLAYERS);
        }
        else if (players.length < MIN_NUM_PLAYERS) {
            validInput = false;
            errorMessage = String.format("Class Game; fillPlayerArray: A Minimum of %2d Players is needed", MIN_NUM_PLAYERS);
        }
        else if (!allPlayernamesUnique(players)) {
            validInput = false;
            errorMessage = "Class Game; fillPlayerArray: All playerNames must be unique";
        }
        if (!validInput) {throw new IllegalArgumentException(errorMessage);}

        ArrayList<Player> playersToBeStored = new ArrayList<Player>(MAX_NUM_PLAYERS);
        for (Player player: players) {
            playersToBeStored.add(new Player(player));
        }
        this.mPlayers = playersToBeStored.toArray(new Player[0]);
    }
    
    private void createTeams(Player[] players) throws IllegalArgumentException, NullPointerException {
        if (players == null) {throw new NullPointerException("Class Game; createTeams: List of Players cannot be null");}
        this.mBlueTeam = new Team(TeamColor.BLUE);
        this.mRedTeam = new Team(TeamColor.RED);
        String errorMessage = "Class Game; createTeams: To each role only one Player may be assigned";
        for (Player player : players){
            if (!player.getRole().isPresent()) {throw new IllegalArgumentException("Class Game; createTeams: All Players need an assigned Role");}
            PlayerRoles role = player.getRole().get();
            switch (role) {
                case BLUE_SPYMASTER:
                    // Change if Multiple Players can have same Role
                    if (mBlueTeam.getSpymaster().isPresent()) {throw new IllegalArgumentException(errorMessage);}
                    mBlueTeam.setSpymaster(player.getPlayerName());
                    break;
                case BLUE_OPERATIVE:
                    // Change if Multiple Players can have same Role
                    if (mBlueTeam.getOperative().isPresent())  {throw new IllegalArgumentException(errorMessage);}
                    mBlueTeam.setOperative(player.getPlayerName());
                    break;
                case RED_SPYMASTER:
                    // Change if Multiple Players can have same Role
                    if (mRedTeam.getSpymaster().isPresent()) {throw new IllegalArgumentException(errorMessage);}
                    mRedTeam.setSpymaster(player.getPlayerName());
                    break;
                case RED_OPERATIVE:
                    // Change if Multiple Players can have same Role
                    if (mRedTeam.getOperative().isPresent()) {throw new IllegalArgumentException(errorMessage);}
                    mRedTeam.setOperative(player.getPlayerName());
                    break;
            }
        }
    }

    private void assertAllRolesOccupied() throws IllegalArgumentException {
        for (PlayerRoles role : PlayerRoles.values()) {
            if (!(this.getNamebyRole(role).isPresent())) {
                throw new IllegalArgumentException("Class Game; assertAllRolesOccupied: Atleast one Player needs to be assigned per role");
            }
        }
    }

    private void setGameType(GameType type) throws IllegalArgumentException {
        if (type == null) {throw new IllegalArgumentException("Class Game; setGameType: Parameter cannot be null");}
        this.mType = type;
    }

    private void setLanguage(SupportedLanguages language) throws IllegalArgumentException {
        if (language == null) {throw new IllegalArgumentException("Class Game; setLanguage: Parameter cannot be null");}
        this.mLanguage = language;
    }

    private void setFirstTeam(TeamColor team) throws IllegalArgumentException {
        if (team == null) {throw new IllegalArgumentException("Class Game; setFirstTeam: Parameter cannot be null");}
        this.mFirstTeam = team;
    }

    public static boolean allPlayernamesUnique(Player[] players) throws NullPointerException {
        if (players == null) {throw new NullPointerException("Class Game; allPlayernamesUnique: List of Players cannot be null");}
        for (int i = 0; i < players.length; i++) {
            for (int j = i+1; j < players.length; j++) {
                if (players[i].getPlayerName().equals(players[j].getPlayerName())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean playerRolesAndTeamAssignmentMatch(Player[] players, Team blueTeam, Team redTeam) throws NullPointerException {
        if (players == null) {throw new NullPointerException("Class Game; playerRolesAndTeamAssignmentMatch: List of Players cannot be null");}
        else if (blueTeam == null || redTeam == null) {throw new NullPointerException("Class Game; playerRolesAndTeamAssignmentMatch: Team Parameter cannot be null");}

        int expectedNullCount = 0;
        if (!(blueTeam.getSpymaster().isPresent())) {expectedNullCount++;}
        if (!(blueTeam.getOperative().isPresent())) {expectedNullCount++;}
        if (!(redTeam.getSpymaster().isPresent())) {expectedNullCount++;}
        if (!(redTeam.getSpymaster().isPresent())) {expectedNullCount++;}

        int actualNullCount = 0;
        for (Player player : players) {
            PlayerRoles role = (player.getRole().isPresent()) ? player.getRole().get() : null;
            String playerName = player.getPlayerName();
            if (role != null) {
                switch (role) {
                    case BLUE_SPYMASTER:
                        if (!(blueTeam.getSpymaster().isPresent())) {return false;}
                        else if (!(blueTeam.getSpymaster().get().equals(playerName))) {return false;}
                        break;
                    case BLUE_OPERATIVE:
                        if (!(blueTeam.getOperative().isPresent())) {return false;}
                        else if (!(blueTeam.getOperative().get().equals(playerName))) {return false;}
                        break;
                    case RED_SPYMASTER:
                        if (!(redTeam.getSpymaster().isPresent())) {return false;}
                        else if (!(redTeam.getSpymaster().get().equals(playerName))) {return false;}
                        break;
                    case RED_OPERATIVE:
                        if (!(redTeam.getOperative().isPresent())) {return false;}
                        else if (!(redTeam.getOperative().get().equals(playerName))) {return false;}
                        break;
                }
            }
            else {actualNullCount++;}
        }
        return (expectedNullCount == actualNullCount);
    }
}
