package dataaccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import passoff.model.TestUser;

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
    void createGameSuccessful(Class<? extends GameDAO> gameDAOClass) {
        GameDAO gameDAO = getDataAccess(gameDAOClass);
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryGameDAO.class, SQLGameDAO.class})
    @DisplayName("Retrieve Game Successful")
    void getGame(Class<? extends GameDAO> gameDAOClass) {
        GameDAO gameDAO = getDataAccess(gameDAOClass);
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryGameDAO.class, SQLGameDAO.class})
    @DisplayName("List Games Successful")
    void listGames(Class<? extends GameDAO> gameDAOClass) {
        GameDAO gameDAO = getDataAccess(gameDAOClass);
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryGameDAO.class, SQLGameDAO.class})
    @DisplayName("Join Game Successful")
    void joinGame(Class<? extends GameDAO> gameDAOClass) {
        GameDAO gameDAO = getDataAccess(gameDAOClass);
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryGameDAO.class, SQLGameDAO.class})
    @DisplayName("Clear Games Successful")
    void clearGameDataSuccessful(Class<? extends GameDAO> gameDAOClass) {
        GameDAO gameDAO = getDataAccess(gameDAOClass);

    }
}