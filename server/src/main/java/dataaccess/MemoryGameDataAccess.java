package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.NewGameResult;

import java.util.HashMap;
import java.util.HashSet;

public class MemoryGameDataAccess implements GameDAO{
    private int nextId = 1;
    HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public NewGameResult createGame(String gameName) throws DataAccessException {
        GameData game = new GameData(nextId++, null, null, gameName, new ChessGame());
        games.put(game.gameID(), game);
        return new NewGameResult(game.gameID());
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return games.get(gameID);
    }

    @Override
    public HashSet<GameData> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public void clearGameData() {
        games.clear();
    }
}
