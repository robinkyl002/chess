package service;

import chess.ChessGame;
import dataaccess.*;
import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    private final GameDAO gameDAO = new MemoryGameDataAccess();
    private final AuthDAO authDAO = new MemoryAuthDataAccess();
    private final UserDAO userDAO = new MemoryUserDataAccess();
    private final GameService gameService = new GameService(gameDAO, authDAO);
    private final UserService userService = new UserService(userDAO, authDAO);

    @BeforeEach
    void setup() throws ResponseException {
        gameService.clearGameData();
        userService.clearAuthData();
        userService.clearUserData();
    }

    @Test
    @DisplayName("Successfully created a game")
    void gameSuccessfullyCreated() throws ResponseException {
        NewGameResult newGame = gameService.newGame("New Game");

        assertEquals(1, newGame.gameID());
    }

    @Test
    @DisplayName("Empty gameName")
    void emptyGameName() {
        assertThrows(ResponseException.class, () -> {
           gameService.newGame("");
        });
    }

    @Test
    @DisplayName("Successfully joined game")
    void joinGameSuccessful() throws ResponseException, DataAccessException {
        AuthData auth = userService.createUser("name", "password", "email.email");
        gameService.newGame("Test game");

        assertDoesNotThrow(() -> gameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, 1), auth.authToken()));

    }

    @Test
    @DisplayName("Player Color taken")
    void playerColorTaken() throws ResponseException, DataAccessException {
        AuthData auth = userService.createUser("name", "password", "email.email");
        gameService.newGame("Test game");
        gameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, 1), auth.authToken());

        assertThrows(ResponseException.class, () ->
                gameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, 1), auth.authToken()));
    }

    @Test
    @DisplayName("Successfully retrieved list of games")
    void listOfGamesReturned() throws ResponseException {
        gameService.newGame("FirstGame");
        gameService.newGame("SecondGame");

        assertNotNull(gameService.listGames());
    }

    @Test
    @DisplayName("No games in List")
    void gameListIsEmpty() {
        assertTrue(gameService.listGames().games().isEmpty());
    }
}