package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import exception.ResponseException;
import model.UserData;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }
    public RegisterResult register(RegisterRequest registerRequest) throws ResponseException {
        try {
            var existingUser = userDAO.getUser(registerRequest.username());
            if (existingUser != null) {
                throw new ResponseException(ResponseException.Code.AlreadyTakenError, "Error: already taken");
            } else {
                userDAO.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
                var authData = authDAO.createAuth(registerRequest.username());
                return new RegisterResult(registerRequest.username(), authData.authToken());
            }
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }
    public LoginResult login(LoginRequest loginRequest) throws ResponseException {
        try {
            var existingUser = userDAO.getUser(loginRequest.username());
            if (existingUser == null) {
                throw new ResponseException(ResponseException.Code.UnauthorizedError, "Error: unauthorized");
            }

            if (!existingUser.password().equals(loginRequest.password())) {
                throw new ResponseException(ResponseException.Code.UnauthorizedError, "Error: unauthorized");
            }
            var authData = authDAO.createAuth(loginRequest.username());
            return new LoginResult(authData.username(), authData.authToken());
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    public void logout(LogoutRequest logoutRequest) throws ResponseException {
        try {
            var existingAuth = authDAO.getAuth(logoutRequest.authToken());

            if (existingAuth == null) {
                throw new ResponseException(ResponseException.Code.UnauthorizedError, "Error: unauthorized");
            }

            authDAO.deleteAuth(logoutRequest.authToken());
        } catch (DataAccessException ex) {
            throw new ResponseException(ResponseException.Code.UnauthorizedError, ex.getMessage());
        }
    }
}
