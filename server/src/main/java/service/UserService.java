package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import io.javalin.http.UnauthorizedResponse;
import model.UserData;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }
    public RegisterResult register(RegisterRequest registerRequest) throws UnauthorizedResponse {
        var existingUser = userDAO.getUser(registerRequest.username());
        if (existingUser != null) {
            // TODO: implement error to be thrown if existingUser is not null
            return null;
        }
        else {
            userDAO.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
            var authData = authDAO.createAuth(registerRequest.username());
            return new RegisterResult(registerRequest.username(), authData.authToken());
        }
    }
    public LoginResult login(LoginRequest loginRequest) {return null;}
    public void logout(LogoutRequest logoutRequest) {}
}
