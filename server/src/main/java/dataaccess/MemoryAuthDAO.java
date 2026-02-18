package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    private final HashMap<String, AuthData> authTokens = new HashMap<>();

    @Override
    public AuthData createAuth(String username) {
        String newToken = generateToken();
        AuthData authData = new AuthData(newToken, username);
        authTokens.put(newToken, authData);
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
