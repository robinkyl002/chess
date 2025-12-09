package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import exception.ResponseException;
import model.*;
import java.util.Collection;
import java.util.List;

public class GameService {
    private final GameDAO gameDataAccess;
    private final AuthDAO authDataAccess;

    public GameService(GameDAO gameDataAccess, AuthDAO authDataAccess) {
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public NewGameResult newGame(String gameName) throws ResponseException {
        try {
            if (gameName == null || gameName.isEmpty()) {
                throw new ResponseException(ResponseException.Code.BadRequestError, "Error: bad request");
            }
            return gameDataAccess.createGame(gameName);
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    public void joinGame(JoinGameRequest joinRequest, String authToken) throws DataAccessException, ResponseException {
        if (joinRequest.playerColor() == null || joinRequest.gameID() == null) {
            throw new ResponseException(ResponseException.Code.BadRequestError, "Error: bad request");
        }
        GameData game = gameDataAccess.getGame(joinRequest.gameID());

        if ((joinRequest.playerColor() == ChessGame.TeamColor.BLACK && game.blackUsername() != null
                && !game.blackUsername().isEmpty()) ||
                (joinRequest.playerColor() == ChessGame.TeamColor.WHITE && game.whiteUsername() != null
                        && !game.blackUsername().isEmpty())) {
            throw new ResponseException(ResponseException.Code.AlreadyTakenError, "Error: already taken");
        }
        AuthData auth = authDataAccess.getAuth(authToken);

        if (joinRequest.playerColor() == ChessGame.TeamColor.BLACK) {
            GameData updatedGame = game.setBlackUsername(game, auth.username());
            gameDataAccess.updateGame(updatedGame);
        }
        else {
            GameData updatedGame = game.setWhiteUsername(game, auth.username());
            gameDataAccess.updateGame(updatedGame);
        }

    }

    public ListGamesResult listGames() {
        Collection<GameData> games = gameDataAccess.listGames();

        List<GameSummary> summaries = games.stream().map(g -> new GameSummary(
                g.gameID(), g.whiteUsername(),
                g.blackUsername(), g.gameName())).toList();

        return new ListGamesResult(summaries);
    }

    public void clearGameData() throws ResponseException {
        try {
            gameDataAccess.clearGameData();
        } catch (DataAccessException e) {
            throw new ResponseException(ResponseException.Code.ServerError, e.getMessage());
        }
    }
}
