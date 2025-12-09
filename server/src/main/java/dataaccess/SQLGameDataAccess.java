package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SQLGameDataAccess implements GameDAO{
    private int nextID = 1;

    public NewGameResult createGame(String gameName) throws DataAccessException {
        try {
            var statement = "INSERT INTO game (id, whiteUsername, blackUsername, gameName, chessGame) VALUES (?, ?, ?, ?)";
            var chessGame = new Gson().toJson(new ChessGame());
            SQLInitializer.executeUpdate(statement,  null, null, gameName, chessGame);
            return new NewGameResult(nextID++);

        } catch (Exception ex) {
            throw new DataAccessException("Could not create new game", ex);
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, whiteUsername, blackUsername, gameName, chessGame FROM game WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException("Could not find game in database", ex);
        }
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        var games = new ArrayList<GameData>();
        try (var preparedStatement = DatabaseManager.getConnection().prepareStatement("SELECT id, whiteUsername, blackUsername, gameName FROM game")) {
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    var currGame = readGame(rs);
                    games.add(currGame);
                }
            }
            return games;
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateGame(GameData updatedGame) throws DataAccessException {
        try (var prepareStatement = DatabaseManager.getConnection().prepareStatement("")) {

        } catch (SQLException e) {

        }
    }

    @Override
    public void clearGameData() throws DataAccessException {
        nextID = 1;
        var statement = "TRUNCATE game";
        SQLInitializer.executeUpdate(statement);
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        var id = rs.getInt("id");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        var json = rs.getString("chessGame");
        var chessGame = new Gson().fromJson(json, ChessGame.class);

        return new GameData(id, whiteUsername, blackUsername, gameName, chessGame);
    }

}
