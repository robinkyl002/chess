package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{
    private HashMap<String, AuthData> authTokens = new HashMap<>();

    @Override
    public AuthData createAuth(AuthData authData) {
        authTokens.put(authData.authToken(), authData);
        return authData;
    }

    @Override
    public void deleteAuth() {
        if (!authTokens.isEmpty()) {
            authTokens.clear();
        }
    }

    @Override
    public AuthData getAuth(String authToken) {
        return authTokens.get(authToken);
    }
}
