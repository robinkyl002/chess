package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

public class UserService {
    private final UserDAO userDataAccess;
    private final AuthDAO authDataAccess;

    public UserService(UserDAO userDataAccess, AuthDAO authDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public AuthData createUser(UserData newUser) throws DataAccessException {
        UserData existingUser = userDataAccess.getUser(newUser.username());
        if (existingUser != null) {
            throw new DataAccessException("");

        }
        userDataAccess.createUser(newUser);

        AuthData auth = authDataAccess.createAuth(newUser.username());

        return auth;
    }

    public AuthData login(UserData loginRequest) throws DataAccessException {
        UserData existingUser = userDataAccess.getUser(loginRequest.username());
        if (existingUser == null) {
            throw new DataAccessException("");
        }
        else if(!loginRequest.password().equals(existingUser.password())) {
            throw new DataAccessException("Unauthorized");
        }
        else {
            return authDataAccess.createAuth(existingUser.username());
        }
    }
}
