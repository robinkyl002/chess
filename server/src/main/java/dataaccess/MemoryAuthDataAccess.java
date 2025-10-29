package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDataAccess implements AuthDAO {
    HashMap<String, AuthData> authTokens = new HashMap<>();

    @Override
    public void createAuth(AuthData data) throws DataAccessException {
        authTokens.put(data.authToken(), data);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return authTokens.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        authTokens.remove(authToken);
    }

    @Override
    public void clearAuthData() {
        authTokens.clear();
    }
}
