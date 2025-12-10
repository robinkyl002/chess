package dataAccess;

import chess.ChessGame;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDataAccess;
import dataaccess.SQLGameDataAccess;
import dataaccess.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class GameDataAccessTest {

    private GameDAO getDataAccess(Class<? extends GameDAO> databaseClass) throws DataAccessException {
        GameDAO db;
        if (databaseClass.equals(SQLGameDataAccess.class)) {
            db = new SQLGameDataAccess();
        } else {
            db = new MemoryGameDataAccess();
        }
        db.clearGameData();
        return db;
    }

    @ParameterizedTest
    @DisplayName("Create Game Successful")
    @ValueSource(classes = {SQLGameDataAccess.class, MemoryGameDataAccess.class})
    void createGame(Class<? extends GameDAO> databaseClass) throws DataAccessException {
        var gameDAO = getDataAccess(databaseClass);

        var gameID = gameDAO.createGame("newGame");
        assertNotNull(gameID);
    }

    @ParameterizedTest
    @DisplayName("Null gameName")
    @ValueSource(classes = {SQLGameDataAccess.class, MemoryGameDataAccess.class})
    void nullGameName (Class<? extends GameDAO> databaseClass) throws DataAccessException {
        var gameDAO = getDataAccess(databaseClass);

        assertThrows(DataAccessException.class, () -> gameDAO.createGame(null));
    }

    @ParameterizedTest
    @DisplayName("Game Successfully retrieved")
    @ValueSource(classes = {SQLGameDataAccess.class, MemoryGameDataAccess.class})
    void getGame(Class<? extends GameDAO> databaseClass) throws DataAccessException {
        var gameDAO = getDataAccess(databaseClass);

        var gameResult = gameDAO.createGame("newGame");
        var game = gameDAO.getGame(gameResult.gameID());
        assertNotNull(game);
    }

    @ParameterizedTest
    @DisplayName("Game not created")
    @ValueSource(classes = {SQLGameDataAccess.class, MemoryGameDataAccess.class})
    void gameNotCreated(Class<? extends GameDAO> databaseClass) throws DataAccessException {
        var gameDAO = getDataAccess(databaseClass);

        var game = gameDAO.getGame(2);
        assertNull(game);
    }

    @ParameterizedTest
    @DisplayName("Multiple games returned")
    @ValueSource(classes = {SQLGameDataAccess.class, MemoryGameDataAccess.class})
    void listGames(Class<? extends GameDAO> databaseClass) throws DataAccessException {
        var gameDAO = getDataAccess(databaseClass);

        gameDAO.createGame("first game");
        gameDAO.createGame("second game");
        gameDAO.createGame("third game");

        Collection<GameData> games = gameDAO.listGames();

        assertEquals(3, games.size());
    }

    @ParameterizedTest
    @DisplayName("Empty List")
    @ValueSource(classes = {SQLGameDataAccess.class, MemoryGameDataAccess.class})
    void emptyList(Class<? extends GameDAO> databaseClass) throws DataAccessException {
        var gameDAO = getDataAccess(databaseClass);

        Collection<GameData> games = gameDAO.listGames();
        assertEquals(0, games.size());
    }

    @ParameterizedTest
    @DisplayName("Updated username")
    @ValueSource(classes = {SQLGameDataAccess.class, MemoryGameDataAccess.class})
    void updateGame(Class<? extends GameDAO> databaseClass) throws DataAccessException {
        var gameDAO = getDataAccess(databaseClass);
        gameDAO.createGame("newGame");
        GameData oldGame = gameDAO.getGame(1);
        gameDAO.updateGame(new GameData(1, "whiteUsername",
                oldGame.blackUsername(), oldGame.gameName(), oldGame.game()));
        GameData updatedGame = gameDAO.getGame(1);
        assertEquals("whiteUsername", updatedGame.whiteUsername());
    }

    @ParameterizedTest
    @DisplayName("Game not created")
    @ValueSource(classes = {SQLGameDataAccess.class, MemoryGameDataAccess.class})
    void cannotUpdateGame(Class<? extends GameDAO> databaseClass) throws DataAccessException {
        var gameDAO = getDataAccess(databaseClass);

        assertThrows(DataAccessException.class, () -> gameDAO.updateGame(new GameData(2, "white",
                "black", "gameName", new ChessGame())));
    }

    @ParameterizedTest
    @DisplayName("Clear Games")
    @ValueSource(classes = {SQLGameDataAccess.class, MemoryGameDataAccess.class})
    void clearGameData(Class<? extends GameDAO> databaseClass) throws DataAccessException {
        var gameDAO = getDataAccess(databaseClass);

        gameDAO.createGame("newGame");
        Collection<GameData> games = gameDAO.listGames();
        assertEquals(1, games.size());

        gameDAO.clearGameData();

        Collection<GameData> gamesAfterDelete = gameDAO.listGames();
        assertEquals(0, gamesAfterDelete.size());
    }
}