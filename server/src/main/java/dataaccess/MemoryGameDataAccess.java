package dataaccess;

import model.GameData;

import java.util.HashSet;

public class MemoryGameDataAccess implements GameDAO{
    @Override
    public void createGame(GameData data) throws DataAccessException {

    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public HashSet<GameData> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public void clearGameData() {

    }
}
