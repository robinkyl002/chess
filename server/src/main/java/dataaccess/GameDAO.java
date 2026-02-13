package dataaccess;

import chess.ChessGame;
import model.GameData;

public interface GameDAO {
    int createGame(String gameName);
    GameData getGame(int gameID);
    void listGames();
    void updateGame(int gameID, String username, ChessGame.TeamColor color);
    void clearGameData();
}
