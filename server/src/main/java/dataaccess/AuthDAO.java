package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData authData);
    void deleteAuth();
    void getAuth(String authToken);
}
