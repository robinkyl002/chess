package dataaccess;

import model.GameData;
import model.NewGameResult;

import java.util.HashSet;

public interface GameDAO {
    NewGameResult createGame(String data) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    HashSet<GameData> listGames() throws DataAccessException;
    void updateGame(GameData updatedGame) throws DataAccessException;
    void clearGameData() throws DataAccessException;
}
