package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() throws ResponseException{
        try {
            DatabaseManager.configureDatabase();
        } catch (DataAccessException e) {
            throw new ResponseException(ResponseException.Code.ServerError,
                    ResponseException.errorMessageFromCode(ResponseException.Code.ServerError));
        }
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO game (gameName, blackUsername, whiteUsername, chessGame) VALUES (?, ?, ?, ?)";
        String chessGame = new Gson().toJson(new ChessGame());
        return  DatabaseManager.executeUpdate(statement, gameName, null, null, chessGame);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM game WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to retrieve game from database: %s", e.getMessage()), e);
        }
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        var result = new HashSet<GameData>();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM game";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to retrieve game from database: %s", e.getMessage()), e);
        }
        return result;
    }

    @Override
    public void joinGame(int gameID, String username, ChessGame.TeamColor color) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = (color == ChessGame.TeamColor.WHITE) ?
                    "UPDATE game SET whiteUsername=? WHERE id=?"
                    : "UPDATE game SET blackUsername=? WHERE id=?";
            try(PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                ps.setInt(2, gameID);
                ps.executeUpdate();

                int updates = ps.getUpdateCount();
                if (updates == 0) {
                    throw new DataAccessException("Could not update game with new player");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not update user");
        }
    }

    @Override
    public void updateGame(int gameID, GameData updatedGame, boolean completed) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "UPDATE game SET chessGame=?, completed=? WHERE id=?";
            try(PreparedStatement ps = conn.prepareStatement(statement)) {
                String chessGame = new Gson().toJson(updatedGame.game());
                ps.setString(1, chessGame);
                ps.setBoolean(2, completed);
                ps.setInt(3, gameID);
                ps.executeUpdate();

                int updates = ps.getUpdateCount();
                if (updates == 0) {
                    throw new DataAccessException("Could not update the game");
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to update game from database: %s", e.getMessage()), e);
        }
    }

    @Override
    public void removePlayer(int gameID, ChessGame.TeamColor playerColor) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = (playerColor == ChessGame.TeamColor.WHITE) ?
                    "UPDATE game SET whiteUsername=? WHERE id=?"
                    : "UPDATE game SET blackUsername=? WHERE id=?";
            try(PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, null);
                ps.setInt(2, gameID);
                ps.executeUpdate();

                int updates = ps.getUpdateCount();
                if (updates == 0) {
                    throw new DataAccessException("Could not update game with new player");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not update user");
        }
    }

    @Override
    public void clearGameData() throws DataAccessException {
        var statement = "TRUNCATE game";
        DatabaseManager.executeUpdate(statement);
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        int gameID = rs.getInt("id");
        String whiteUsername = rs.getString("whiteUsername");
        String blackUsername = rs.getString("blackUsername");
        String gameName = rs.getString("gameName");
        String chessGameJson = rs.getString("chessGame");
        boolean completed = rs.getBoolean("completed");
        return new GameData(gameID, whiteUsername, blackUsername, gameName,
                new Gson().fromJson(chessGameJson, ChessGame.class),  completed);
    }
}
