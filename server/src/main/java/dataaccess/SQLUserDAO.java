package dataaccess;

import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class SQLUserDAO implements UserDAO {
    @Override
    public void createUser(UserData user) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteUsers() throws DataAccessException {
        try {

        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }
}
