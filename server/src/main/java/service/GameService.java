package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import exception.ResponseException;
import model.AuthData;

import static exception.ResponseException.Code.*;
import static exception.ResponseException.errorMessageFromCode;

public class GameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws ResponseException {
        try {
            if (createGameRequest.gameName() == null || createGameRequest.gameName().isEmpty()) {
                throw new ResponseException(BadRequestError, errorMessageFromCode(BadRequestError));
            }

            int gameID = gameDAO.createGame(createGameRequest.gameName());
            return new CreateGameResult(gameID);
        } catch (DataAccessException ex) {
            throw new ResponseException(ServerError, errorMessageFromCode(ServerError) + ex.getMessage());
        }
    }
    public void joinGame(JoinGameRequest joinGameRequest, String authToken) throws ResponseException {
        try {
            if (joinGameRequest.color() == null || joinGameRequest.gameID() == null) {
                throw new ResponseException(BadRequestError, errorMessageFromCode(BadRequestError));
            }

            AuthData auth = authDAO.getAuth(authToken);

            gameDAO.updateGame(joinGameRequest.gameID(), auth.username(), joinGameRequest.color());
        } catch (DataAccessException ex) {
            throw new ResponseException(ServerError, errorMessageFromCode(ServerError) + ex.getMessage());
        }
    }
    public void listGames() throws ResponseException {}
    public void getGame() throws ResponseException {}

    public void clearAllGames() throws ResponseException {
        try {
            gameDAO.clearGameData();
        } catch (DataAccessException e) {
            throw new ResponseException(ServerError, errorMessageFromCode(ServerError) + e.getMessage());
        }
    }
}
