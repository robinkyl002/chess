package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData createAuth(AuthData authData);
    void deleteAuth();
    AuthData getAuth(String authToken);
}
