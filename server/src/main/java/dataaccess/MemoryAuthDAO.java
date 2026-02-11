package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{
    private HashMap<String, AuthData> authTokens = new HashMap<>();

    @Override
    public void createAuth(AuthData authData) {
        authTokens.put(authData.authToken(), authData);
    }

    @Override
    public void deleteAuth() {
        authTokens.clear();
    }

    @Override
    public void getAuth(String authToken) {
        authTokens.get(authToken);
    }
}
