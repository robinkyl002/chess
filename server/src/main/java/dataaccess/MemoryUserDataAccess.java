package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDataAccess implements UserDAO{
    HashMap<String, UserData> users = new HashMap<>();

    @Override
    public void createUser(UserData newUser) throws DataAccessException {
        if (newUser.username() == null) {
            throw new DataAccessException("Error: Username cannot be null");
        }
        users.put(newUser.username(), newUser);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return users.get(username);
    }

    @Override
    public void clearUserData() {
        if (!users.isEmpty()) {
            users.clear();
        }
    }
}
