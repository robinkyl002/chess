package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import exception.ResponseException;
import model.NewGameResult;

public class GameService {
    private final GameDAO gameDataAccess;
    private final AuthDAO authDataAccess;

    public GameService(GameDAO gameDataAccess, AuthDAO authDataAccess) {
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public NewGameResult newGame(String gameName) throws DataAccessException, ResponseException {
        if (gameName == null || gameName.isEmpty()) {
            throw new ResponseException(ResponseException.Code.BadRequestError, "Error: bad request");
        }
        return gameDataAccess.createGame(gameName);
    }
}
