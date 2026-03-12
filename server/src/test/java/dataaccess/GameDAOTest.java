package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import passoff.model.TestUser;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class GameDAOTest {
    private GameDAO getDataAccess(Class<? extends GameDAO> gameDAOClass) {
        try {
            GameDAO db;
            if (gameDAOClass.equals(MemoryGameDAO.class)) {
                db = new MemoryGameDAO();
            } else {
                db = new SQLGameDAO();
            }
            db.clearGameData();
            return db;
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize GameDAO for tests");
        }
    }
    private static TestUser existingUser;

    @BeforeEach
    void setup() {
        existingUser = new TestUser("username", "password", "email");
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryGameDAO.class, SQLGameDAO.class})
    @DisplayName("Create Game Successful")
    void createGameSuccessful(Class<? extends GameDAO> gameDAOClass) throws DataAccessException {
        GameDAO gameDAO = getDataAccess(gameDAOClass);

        int id = gameDAO.createGame("New Game");
        assertEquals(1, id);
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryGameDAO.class, SQLGameDAO.class})
    @DisplayName("Cannot Create Game - Name Is Empty")
    void createGameFailedGameNameEmpty(Class<? extends GameDAO> gameDAOClass) throws DataAccessException {
        GameDAO gameDAO = getDataAccess(gameDAOClass);

        assertThrows(DataAccessException.class, () -> gameDAO.createGame(null));
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryGameDAO.class, SQLGameDAO.class})
    @DisplayName("Retrieve Game Successful")
    void getGameSuccessful(Class<? extends GameDAO> gameDAOClass) throws DataAccessException {
        GameDAO gameDAO = getDataAccess(gameDAOClass);

        int id = gameDAO.createGame("test");

        GameData game = gameDAO.getGame(id);

        assertNotNull(game);
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryGameDAO.class, SQLGameDAO.class})
    @DisplayName("Get Game - Does not exist")
    void retrieveGameThatDoesNotExist(Class<? extends GameDAO> gameDAOClass) throws DataAccessException {
        GameDAO gameDAO = getDataAccess(gameDAOClass);

        GameData game = gameDAO.getGame(1);
        assertNull(game);
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryGameDAO.class, SQLGameDAO.class})
    @DisplayName("List Games Successful")
    void listGames(Class<? extends GameDAO> gameDAOClass) throws DataAccessException {
        GameDAO gameDAO = getDataAccess(gameDAOClass);
        gameDAO.createGame("test");

        Collection<GameData> gamesList = gameDAO.listGames();

        assertNotEquals(0, gamesList.size());
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryGameDAO.class, SQLGameDAO.class})
    @DisplayName("List of Games is Empty")
    void listGamesEmpty(Class<? extends GameDAO> gameDAOClass) throws DataAccessException {
        GameDAO gameDAO = getDataAccess(gameDAOClass);

        Collection<GameData> gamesList = gameDAO.listGames();

        assertEquals(0, gamesList.size());
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryGameDAO.class, SQLGameDAO.class})
    @DisplayName("Join Game Successful")
    void joinGame(Class<? extends GameDAO> gameDAOClass) throws DataAccessException {
        GameDAO gameDAO = getDataAccess(gameDAOClass);
        int id = gameDAO.createGame("test");

        assertDoesNotThrow(() -> gameDAO.joinGame(id, existingUser.getUsername(), ChessGame.TeamColor.WHITE));
        GameData updatedGame = gameDAO.getGame(id);
        assertEquals(existingUser.getUsername(), updatedGame.whiteUsername());
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryGameDAO.class, SQLGameDAO.class})
    @DisplayName("Game not created")
    void gameDoesNotExist (Class<? extends GameDAO> gameDAOClass) throws DataAccessException {
        GameDAO gameDAO = getDataAccess(gameDAOClass);

        assertThrows(DataAccessException.class, () -> gameDAO.joinGame(1, existingUser.getUsername(), ChessGame.TeamColor.WHITE));
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryGameDAO.class, SQLGameDAO.class})
    @DisplayName("Clear Games Successful")
    void clearGameDataSuccessful(Class<? extends GameDAO> gameDAOClass) {
        GameDAO gameDAO = getDataAccess(gameDAOClass);

        assertDoesNotThrow(gameDAO::clearGameData);

    }
}