package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDataAccess implements AuthDAO {
    HashMap<String, AuthData> authTokens = new HashMap<>();

    public AuthData createAuth(String username) throws DataAccessException {
        //String token = generateToken();
        AuthData newAuth = new AuthData(generateToken(), username);
        authTokens.put(newAuth.authToken(), newAuth);
        return newAuth;


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

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
