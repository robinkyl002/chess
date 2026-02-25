package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    int createGame(String gameName) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
    void updateGame(int gameID, String username, ChessGame.TeamColor color) throws DataAccessException;
    void clearGameData() throws DataAccessException;
}
