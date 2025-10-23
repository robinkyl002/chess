package dataaccess;

import model.UserData;

public interface UserDAO {
    void createUser(UserData data) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void clearUserData();
}
