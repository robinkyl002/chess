package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import exception.ResponseException;
import model.AuthData;
import model.UserData;

public class UserService {
    private final UserDAO userDataAccess;
    private final AuthDAO authDataAccess;

    public UserService(UserDAO userDataAccess, AuthDAO authDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public AuthData createUser(UserData newUser) throws DataAccessException, ResponseException {

        UserData existingUser = userDataAccess.getUser(newUser.username());
        if (existingUser != null) {
            throw new ResponseException(ResponseException.Code.AlreadyTakenError, "Error: username already taken");
        }
        userDataAccess.createUser(newUser);

        AuthData auth = authDataAccess.createAuth(newUser.username());

        return auth;
    }

    public AuthData login(UserData loginRequest) throws DataAccessException, ResponseException {
        try {
            UserData existingUser = userDataAccess.getUser(loginRequest.username());
            if (existingUser == null) {
                throw new ResponseException(ResponseException.Code.BadRequestError, "Error: bad request");

            } else if (!loginRequest.password().equals(existingUser.password())) {
                throw new ResponseException(ResponseException.Code.UnauthorizedError, "Error: Unauthorized");
            } else {
                return authDataAccess.createAuth(existingUser.username());
            }
        }
        catch (Exception ex) {
                throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());

            // throw new DataAccessException(ex.getMessage());
        }
    }

    public void logout(String authToken) throws DataAccessException {
        AuthData auth = authDataAccess.getAuth(authToken);

        if (auth == null) {
            throw new DataAccessException("Error: Unauthorized");
        }

        authDataAccess.deleteAuth(authToken);
    }
}
