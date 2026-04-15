package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import exception.ResponseException;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

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
            if (joinGameRequest.playerColor() == null || joinGameRequest.gameID() == null) {
                throw new ResponseException(BadRequestError, errorMessageFromCode(BadRequestError));
            }

            AuthData auth = authDAO.getAuth(authToken);

            GameData currGame = gameDAO.getGame(joinGameRequest.gameID());

            // Check to make sure the color is not already taken by someone else
            if ((joinGameRequest.playerColor() == ChessGame.TeamColor.WHITE && currGame.whiteUsername() != null
                    && !currGame.whiteUsername().equals(auth.username()) )
                    || (joinGameRequest.playerColor() == ChessGame.TeamColor.BLACK && currGame.blackUsername() != null
                    && !currGame.blackUsername().equals(auth.username()))) {
                    throw new ResponseException(AlreadyTakenError, errorMessageFromCode(AlreadyTakenError));
            }
            gameDAO.joinGame(joinGameRequest.gameID(), auth.username(), joinGameRequest.playerColor());
        } catch (DataAccessException ex) {
            throw new ResponseException(ServerError, errorMessageFromCode(ServerError) + ex.getMessage());
        }
    }

    public void updateGame(GameData gameData) throws ResponseException {
        try {
                gameDAO.updateGame(gameData.gameID(), gameData);
        } catch (DataAccessException e) {
            throw new ResponseException(ServerError, errorMessageFromCode(ServerError) + e.getMessage());
        }
    }

    public GameData retrieveGame(int gameID) throws ResponseException {
        try {
            return gameDAO.getGame(gameID);
        } catch (DataAccessException e) {
            throw new ResponseException(ServerError, errorMessageFromCode(ServerError) + e.getMessage());
        }
    }

    public ListGamesResult listGames() throws ResponseException {
        try {
            Collection<GameData> games = gameDAO.listGames();
            var summaries = new ArrayList<GameSummary>();
            for (var game: games) {
                summaries.add(new GameSummary(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
            }

            return new ListGamesResult(summaries);
        } catch (DataAccessException ex) {
            throw new ResponseException(ServerError, errorMessageFromCode(ServerError) + ex.getMessage());
        }
    }

    public void clearAllGames() throws ResponseException {
        try {
            gameDAO.clearGameData();
        } catch (DataAccessException e) {
            throw new ResponseException(ServerError, errorMessageFromCode(ServerError) + e.getMessage());
        }
    }
}
