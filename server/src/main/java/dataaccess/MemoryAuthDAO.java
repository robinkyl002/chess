package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    private HashMap<String, AuthData> authTokens = new HashMap<>();

    @Override
    public AuthData createAuth(AuthData authData) {
        authTokens.put(authData.authToken(), authData);
        return authData;
    }

    @Override
    public void deleteAuth(String authToken) {
        authTokens.remove(authToken);
    }

    @Override
    public AuthData getAuth(String authToken) {
        return authTokens.get(authToken);
    }

    @Override
    public void clearAuthData() {
        if (!authTokens.isEmpty()) {
            authTokens.clear();
        }
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
