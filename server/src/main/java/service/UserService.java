package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {
    private final UserDAO userDataAccess;
    private final AuthDAO authDataAccess;

    public UserService(UserDAO userDataAccess, AuthDAO authDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public AuthData createUser(String username, String password, String email) throws ResponseException {
        try {
            UserData existingUser = userDataAccess.getUser(username);
            if (existingUser != null) {
                throw new ResponseException(ResponseException.Code.AlreadyTakenError, "Error: username already taken");
            }
            userDataAccess.createUser(new UserData(username, password, email));

            return authDataAccess.createAuth(username);
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    public AuthData login(UserData loginRequest) throws DataAccessException, ResponseException {
        try {
            UserData existingUser = userDataAccess.getUser(loginRequest.username());

            if (existingUser == null || !BCrypt.checkpw(loginRequest.password(), existingUser.password())) {
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

    public void logout(String authToken) throws ResponseException {
        try {
            AuthData auth = authDataAccess.getAuth(authToken);

            if (auth == null) {
                throw new ResponseException(ResponseException.Code.UnauthorizedError, "Error: Unauthorized");
            }

            authDataAccess.deleteAuth(authToken);
        }
        catch (DataAccessException ex) {
                throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    public boolean validAuth(String authToken) throws DataAccessException {
        AuthData auth = authDataAccess.getAuth(authToken);

        return auth != null;
    }

    public void clearUserData() throws ResponseException {
        try {
            userDataAccess.clearUserData();
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    public void clearAuthData() throws ResponseException {
        try {
            authDataAccess.clearAuthData();
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }
}
