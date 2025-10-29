package service;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;

public class UserService {
    private final UserDAO userDataAccess;

    public UserService(UserDAO userDataAccess) {
        this.userDataAccess = userDataAccess;
    }

    public void createUser(UserData newUser) throws DataAccessException {
        UserData existingUser = userDataAccess.getUser(newUser.username());
        if (existingUser == null) {
            throw new DataAccessException("");

        }
        userDataAccess.createUser(newUser);
    }
}
