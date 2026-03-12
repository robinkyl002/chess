package dataaccess;

import exception.ResponseException;
import model.AuthData;

import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() throws ResponseException {
        try {
            DatabaseManager.configureDatabase();
        } catch (DataAccessException e) {
            throw new ResponseException(ResponseException.Code.ServerError,
                    ResponseException.errorMessageFromCode(ResponseException.Code.ServerError));
        }
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        var statement = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
        String authToken = generateToken();

        DatabaseManager.executeUpdate(statement, username, authToken);

        return new AuthData(authToken, username);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public void clearAuthData() throws DataAccessException {

    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
