package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

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
        return List.of();
    }

    @Override
    public void updateGame(int gameID, String username, ChessGame.TeamColor color) throws DataAccessException {

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
        return new GameData(gameID, whiteUsername, blackUsername, gameName,
                new Gson().fromJson(chessGameJson, ChessGame.class));
    }
}
