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
        GameData game = games.get(gameID);
        if (game == null) {
            throw new DataAccessException("Game not found");
        }
        return game;
    }

    @Override
    public HashSet<GameData> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(GameData updatedGame) throws DataAccessException {
        GameData existingGame = games.get(updatedGame.gameID());
        if (existingGame == null) {
            throw new DataAccessException("Existing game could not be found");
        }

        games.remove(updatedGame.gameID());
        games.put(updatedGame.gameID(), updatedGame);
    }

    @Override
    public void clearGameData() {
        if (!games.isEmpty()) {
            games.clear();
        }
    }
}
