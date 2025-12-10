package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDataAccess implements AuthDAO{
    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        try {
            var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
            AuthData newAuth = new AuthData(generateToken(), username);
            SQLInitializer.executeUpdate(statement, newAuth.authToken(), newAuth.username());
            return newAuth;
        } catch (Exception e) {
            throw new DataAccessException("Error: " + e.getMessage(), e);
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM auth WHERE authToken=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException("Error: Could not find auth in database", ex);
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        try {
            var statement = "DELETE FROM auth WHERE authToken = ?";
            SQLInitializer.executeUpdate(statement, authToken);
        } catch (Exception ex) {
            throw new DataAccessException("Error: could not logout user", ex);
        }
    }

    @Override
    public void clearAuthData() throws DataAccessException {
        var statement = "TRUNCATE auth";
        SQLInitializer.executeUpdate(statement);
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        var authToken = rs.getString("authToken");
        var username = rs.getString("username");

        return new AuthData(authToken, username);
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
