package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDataAccess implements AuthDAO {
    HashMap<String, AuthData> authTokens = new HashMap<>();

    public AuthData createAuth(String username) throws DataAccessException {
        try {
            if (username == null) {
                throw new DataAccessException("Error: username cannot be null");
            }
            AuthData newAuth = new AuthData(generateToken(), username);
            authTokens.put(newAuth.authToken(), newAuth);
            return newAuth;
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try {
            return authTokens.get(authToken);
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        try {
            authTokens.remove(authToken);
        }
        catch (Exception ex) {
            throw new DataAccessException("Error: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void clearAuthData() throws DataAccessException {
        try {
            if (!authTokens.isEmpty()) {
                authTokens.clear();
            }
        } catch (Exception e) {
            throw new DataAccessException("Error: " + e.getMessage(), e);
        }
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
