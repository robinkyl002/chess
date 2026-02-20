package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    HashMap<Integer, GameData> games = new HashMap<>();
    private int nextId = 1;

    @Override
    public int createGame(String gameName) throws DataAccessException {
        try {
            var game = new GameData(nextId++, null, null,
                    gameName, new ChessGame());
            games.put(game.gameID(), game);
            return game.gameID();
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try {
            return games.get(gameID);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void listGames() throws DataAccessException {
        try {

        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    @Override
    public void updateGame(int gameID, String username, ChessGame.TeamColor color) throws DataAccessException {
        try {
            GameData currGame = getGame(gameID);
            var newGame = (color == ChessGame.TeamColor.WHITE) ?
                    new GameData(gameID, username, currGame.blackUsername(), currGame.gameName(), currGame.game()) :
                    new GameData(gameID, currGame.whiteUsername(), username, currGame.gameName(), currGame.game());
            games.put(gameID, newGame);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clearGameData() throws DataAccessException {
        try {
            if (!games.isEmpty()) {
                games.clear();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
