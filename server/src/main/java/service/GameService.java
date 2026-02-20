package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import exception.ResponseException;
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
    public void joinGame() throws ResponseException {}
    public void listGames() throws ResponseException {}
    public void getGame() throws ResponseException {}
}
