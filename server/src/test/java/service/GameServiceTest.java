package service;

import chess.ChessGame;
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
    void createGameEmptyGameName() {
        assertThrows(ResponseException.class, () -> gameService.createGame(new CreateGameRequest("")), "Error: bad request");
    }

    @Test
    @Order(3)
    @DisplayName("Join Game Successful")
    void joinGameSuccessful() throws ResponseException {
        var registerResult = userService.register(new RegisterRequest(existingUser.getUsername(),
                existingUser.getPassword(), existingUser.getEmail()));

        gameService.createGame(new CreateGameRequest("New Game"));

        assertDoesNotThrow(() -> gameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 1), registerResult.authToken()));
    }

    @Test
    @Order(4)
    @DisplayName("Join Game - Game ID Empty")
    void joinGame() throws ResponseException {
        var registerResult = userService.register(new RegisterRequest(existingUser.getUsername(),
                existingUser.getPassword(), existingUser.getEmail()));

        gameService.createGame(new CreateGameRequest("New Game"));

        assertThrows(ResponseException.class,
                () -> gameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 1), registerResult.authToken()));
    }

    @Test
    @Order(5)
    @DisplayName("List Games Successful")
    void listGamesSuccessful() throws ResponseException {
        var gameList = gameService.listGames();

        assertNotNull(gameList);
    }

    @Test
    @Order(6)
    @DisplayName("List Games - Server Error")
    void listGamesServerError() {

    }

    @Test
    @Order(7)
    @DisplayName("Clear Database Successful")
    void clearDB() throws ResponseException {
        assertDoesNotThrow(userService::clearUserData);
        assertDoesNotThrow(userService::clearAuthData);
        assertDoesNotThrow(gameService::clearAllGames);

        userService.register(new RegisterRequest(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail()));
        gameService.createGame(new CreateGameRequest("newGame"));
        assertDoesNotThrow(userService::clearUserData);
        assertDoesNotThrow(userService::clearAuthData);
        assertDoesNotThrow(gameService::clearAllGames);
    }
}