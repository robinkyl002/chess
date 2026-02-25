package service;

import dataaccess.*;
import exception.ResponseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import passoff.model.TestUser;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    private final UserDAO userDAO = new MemoryUserDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();
    private final GameDAO gameDAO = new MemoryGameDAO();
    private final UserService userService = new UserService(userDAO, authDAO);
    private final GameService gameService = new GameService(authDAO, gameDAO);
    private static TestUser existingUser;

    @BeforeEach
    void setup () throws ResponseException {
        gameService.clearAllGames();

        existingUser = new TestUser("ExistingUser", "existingPassword", "eu@mail.com");
    }

    @Test
    @Order(1)
    @DisplayName("Create Game Successful")
    void createGameSuccessful() throws ResponseException {

        var createGameResult = gameService.createGame(new CreateGameRequest("newGame"));

        assertNotNull(createGameResult);
        assertEquals(1, createGameResult.gameID());
    }

    @Test
    @Order(2)
    @DisplayName("Create Game - Empty Game Name")
    void createGameEmptyGameName() throws ResponseException {

        assertThrows(ResponseException.class, () -> gameService.createGame(new CreateGameRequest("")), "Error: bad request");
    }

    @Test
    @Order(3)
    @DisplayName("Join Game Successful")
    void joinGameSuccessful() {
    }

    @Test
    @Order(4)
    @DisplayName("Join Game - Game ID Empty")
    void joinGame() {
    }

    @Test
    @Order(5)
    @DisplayName("List Games Successful")
    void listGamesSuccessful() {
    }

    @Test
    @Order(6)
    @DisplayName("List Games - Unauthorized")
    void listGamesUnauthorized() {
    }
}