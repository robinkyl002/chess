package dataaccess;

import model.UserData;

public interface UserDAO {
    void createUser(UserData user) throws DataAccessException;
    void deleteUsers() throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
}
