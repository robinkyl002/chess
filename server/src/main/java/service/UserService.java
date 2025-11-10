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

        return authDataAccess.createAuth(newUser.username());
    }

    public AuthData login(UserData loginRequest) throws DataAccessException, ResponseException {
        try {
            UserData existingUser = userDataAccess.getUser(loginRequest.username());
            if (existingUser == null || !loginRequest.password().equals(existingUser.password())) {
                throw new ResponseException(ResponseException.Code.UnauthorizedError, "Error: Unauthorized");
            } else {
                return authDataAccess.createAuth(existingUser.username());
            }
        }
        catch (Exception ex) {
            if (ex instanceof ResponseException) {
                throw ex;
            } else {
                throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
            }
        }
    }

    public void logout(String authToken) throws DataAccessException, ResponseException {
        try {
            AuthData auth = authDataAccess.getAuth(authToken);

            if (auth == null) {
                throw new ResponseException(ResponseException.Code.UnauthorizedError, "Error: Unauthorized");
            }

            authDataAccess.deleteAuth(authToken);
        }
        catch (Exception ex) {
            if (ex instanceof ResponseException) {
                throw ex;
            } else {
                throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
            }
        }
    }
}
