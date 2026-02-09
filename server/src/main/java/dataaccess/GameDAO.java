package dataaccess;

import model.GameData;

public interface GameDAO {
    void createGame(GameData game);
    void getGame(int gameID);
    void listGames();
    void updateGame();
}
