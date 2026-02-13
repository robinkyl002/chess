package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    HashMap<Integer, GameData> games = new HashMap<>();
    private int nextId = 1;

    @Override
    public int createGame(String gameName) {
        var game = new GameData(nextId++, null, null,
                gameName, new ChessGame());
        games.put(game.gameID(), game);
        return game.gameID();
    }

    @Override
    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    @Override
    public void listGames() {

    }

    @Override
    public void updateGame(int gameID, String username, ChessGame.TeamColor color) {
        var currGame = getGame(gameID);
        var newGame = (color == ChessGame.TeamColor.WHITE) ?
                new GameData(gameID, username, currGame.blackUsername(), currGame.gameName(), currGame.game()) :
                new GameData(gameID, currGame.whiteUsername(), username, currGame.gameName(), currGame.game());
        games.put(gameID, newGame);
    }
}
