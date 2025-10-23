package dataaccess;

import model.GameData;

import java.util.HashSet;

public interface GameDAO {
    void createGame(GameData data) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    HashSet<GameData> listGames() throws DataAccessException;
    void clearGameData();
}
