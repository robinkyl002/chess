package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDataAccess implements UserDAO{
    @Override
    public void createUser(UserData data) throws DataAccessException {
        try {
            var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
            String hashedPassword = BCrypt.hashpw(data.password(), BCrypt.gensalt());
            SQLInitializer.executeUpdate(statement, data.username(), hashedPassword, data.email());
        } catch (Exception ex) {
            throw new DataAccessException("Error: could not create new user", ex);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM user WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException("Error: Could not find user in database", ex);
        }
        return null;
    }

    @Override
    public void clearUserData() throws DataAccessException {
        var statement = "TRUNCATE user";
        SQLInitializer.executeUpdate(statement);
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var hashedPassword = rs.getString("password");
        var email = rs.getString("email");

        return new UserData(username, hashedPassword, email);
    }
}
