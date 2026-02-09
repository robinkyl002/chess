package dataaccess;

import model.UserData;

public interface UserDAO {
    void createUser(UserData user);
    void deleteUsers();
    void getUser(String username);
}
