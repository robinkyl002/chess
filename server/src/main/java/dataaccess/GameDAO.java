package dataaccess;

import model.GameData;
import model.NewGameResult;

import java.util.Collection;

public interface GameDAO {
    NewGameResult createGame(String data) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    Collection<GameData> listGames();
    void updateGame(GameData updatedGame) throws DataAccessException;
    void clearGameData() throws DataAccessException;
}
