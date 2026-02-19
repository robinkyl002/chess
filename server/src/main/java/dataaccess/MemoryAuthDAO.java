package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    private final HashMap<String, AuthData> authTokens = new HashMap<>();

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        try {
            String newToken = generateToken();
            AuthData authData = new AuthData(newToken, username);
            authTokens.put(newToken, authData);
            return authData;
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException{
        try {
            authTokens.remove(authToken);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException{
        try {
            return authTokens.get(authToken);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clearAuthData() throws DataAccessException {
        try {
            if (!authTokens.isEmpty()) {
                authTokens.clear();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
