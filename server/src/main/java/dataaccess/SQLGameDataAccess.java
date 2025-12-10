package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDataAccess implements GameDAO{
    public NewGameResult createGame(String gameName) throws DataAccessException {
        try {
            var statement = "INSERT INTO game (whiteUsername, blackUsername, gameName, chessGame) VALUES (?,?,?,?)";
            var newGame = new ChessGame();
            var chessGame = new Gson().toJson(newGame);
            int id = SQLInitializer.executeUpdate(statement,  null, null, gameName, chessGame);
            return new NewGameResult(id);

        } catch (Exception ex) {
            throw new DataAccessException("Error: Could not create new game", ex);
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
            throw new DataAccessException("Error: Could not find game in database", ex);
        }
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        var games = new ArrayList<GameData>();
        try (var preparedStatement = DatabaseManager.getConnection().prepareStatement("SELECT id, whiteUsername, blackUsername, gameName, chessGame FROM game")) {
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    var currGame = readGame(rs);
                    games.add(currGame);
                }
            }
            return games;
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException("Error: Could not list games", e);
        }
    }

    @Override
    public void updateGame(GameData updatedGame) throws DataAccessException {
        try (var ps = DatabaseManager.getConnection().prepareStatement("UPDATE game SET whiteUsername=?, blackUsername=? WHERE id=?")) {
            ps.setString(1, updatedGame.whiteUsername());
            ps.setString(2, updatedGame.blackUsername());
            ps.setInt(3, updatedGame.gameID());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage(), e);
        }
    }

    @Override
    public void clearGameData() throws DataAccessException {
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
