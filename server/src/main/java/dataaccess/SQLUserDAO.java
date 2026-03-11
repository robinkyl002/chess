package dataaccess;

import exception.ResponseException;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws ResponseException {
        try {
            DatabaseManager.configureDatabase();
        } catch (DataAccessException e) {
            throw new ResponseException(ResponseException.Code.ServerError,
                    ResponseException.errorMessageFromCode(ResponseException.Code.ServerError));
        }
    }
    @Override
    public void createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        int id = DatabaseManager.executeUpdate(statement, user.username(), hashedPassword, user.email());
    }

    @Override
    public void deleteUsers() throws DataAccessException {
        var statement = "TRUNCATE user";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM user WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {

                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to retrieve user from database: %s", e.getMessage()), e);
        }
        return null;
    }
}
