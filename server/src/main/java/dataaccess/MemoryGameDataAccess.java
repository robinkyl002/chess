package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.NewGameResult;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDataAccess implements GameDAO{
    private int nextId = 1;
    HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public NewGameResult createGame(String gameName) {
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
    public Collection<GameData> listGames() {
        return games.values();
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
