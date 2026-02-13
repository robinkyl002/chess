package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData createAuth(AuthData authData);
    AuthData getAuth(String authToken);
    void deleteAuth(String authToken);
    void clearAuthData();
}
