package dataaccess;

import model.UserData;

public interface UserDAO {
    void createUser(UserData user);
    void deleteUsers();
    UserData getUser(String username);
}
