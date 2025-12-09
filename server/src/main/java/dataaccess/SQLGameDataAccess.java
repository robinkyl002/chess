package dataaccess;

import model.GameData;
import model.NewGameResult;

import java.util.Collection;
import java.util.List;

public class SQLGameDataAccess implements GameDAO{
    @Override
    public NewGameResult createGame(String data) {
        return null;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        return List.of();
    }

    @Override
    public void updateGame(GameData updatedGame) throws DataAccessException {

    }

    @Override
    public void clearGameData() throws DataAccessException {

    }

}
