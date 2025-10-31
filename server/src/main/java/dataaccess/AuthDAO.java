package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData createAuth(String username) throws DataAccessException;
    AuthData getAuth(String username) throws DataAccessException;
    void deleteAuth(String username) throws DataAccessException;
    void clearAuthData();
}
