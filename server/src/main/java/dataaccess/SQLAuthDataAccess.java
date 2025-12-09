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
            throw new DataAccessException("Could not find user in database", ex);
        }
        return null;
    }

    @Override
    public void deleteAuth(String username) throws DataAccessException {

    }

    @Override
    public void clearAuthData() throws DataAccessException {

    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        var authToken = rs.getString("authToken");
        var username = rs.getString("username");

        return new AuthData(authToken, username);
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
