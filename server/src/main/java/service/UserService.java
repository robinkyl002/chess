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
    public LoginResult login(LoginRequest loginRequest) {return null;}
    public void logout(LogoutRequest logoutRequest) {}
}
