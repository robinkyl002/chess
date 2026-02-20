package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import exception.ResponseException;
import model.UserData;
import static exception.ResponseException.Code.*;
import static exception.ResponseException.errorMessageFromCode;

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
                throw new ResponseException(AlreadyTakenError, errorMessageFromCode(AlreadyTakenError));
            } else {
                userDAO.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
                var authData = authDAO.createAuth(registerRequest.username());
                return new RegisterResult(registerRequest.username(), authData.authToken());
            }
        } catch (DataAccessException ex) {
            throw new ResponseException(ServerError, errorMessageFromCode(ServerError) + ex.getMessage());
        }
    }
    public LoginResult login(LoginRequest loginRequest) throws ResponseException {
        try {
            var existingUser = userDAO.getUser(loginRequest.username());
            if (existingUser == null) {
                throw new ResponseException(UnauthorizedError, errorMessageFromCode(UnauthorizedError));
            }

            if (!existingUser.password().equals(loginRequest.password())) {
                throw new ResponseException(UnauthorizedError, errorMessageFromCode(UnauthorizedError));
            }
            var authData = authDAO.createAuth(loginRequest.username());
            return new LoginResult(authData.username(), authData.authToken());
        } catch (DataAccessException ex) {
            throw new ResponseException(ServerError, errorMessageFromCode(ServerError) + ex.getMessage());
        }
    }

    public void logout(LogoutRequest logoutRequest) throws ResponseException {
        try {
            authDAO.deleteAuth(logoutRequest.authToken());
        } catch (DataAccessException ex) {
            throw new ResponseException(ServerError, errorMessageFromCode(ServerError) + ex.getMessage());
        }
    }

    public boolean validAuth(String authToken) throws ResponseException {
        try {
            boolean valid = false;
            if (authToken == null || authToken.isEmpty()) {
                return valid;
            }
            var existingAuth = authDAO.getAuth(authToken);

            return existingAuth != null;
        } catch (DataAccessException ex) {
            throw new ResponseException(ServerError, errorMessageFromCode(ServerError) + ex.getMessage());
        }
    }
}
