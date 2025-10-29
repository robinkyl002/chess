package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDataAccess implements UserDAO{
    HashMap<String, UserData> users = new HashMap<>();

    @Override
    public void createUser(UserData newUser) throws DataAccessException {
        users.put(newUser.username(), newUser);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return users.get(username);
    }

    @Override
    public void clearUserData() {
        users.clear();
    }
}
