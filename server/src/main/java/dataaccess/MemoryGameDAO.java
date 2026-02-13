package dataaccess;

import model.GameData;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    HashMap<Integer, GameData> games = new HashMap<>();
    // private int nextId = 1;

    @Override
    public void createGame(GameData game) {

    }

    @Override
    public void getGame(int gameID) {
        games.get(gameID);
    }

    @Override
    public void listGames() {

    }

    @Override
    public void updateGame() {

    }
}
