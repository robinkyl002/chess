package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    private final HashMap<String, UserData> users = new HashMap<>();

    @Override
    public void createUser(UserData user) throws DataAccessException{
        try {
            users.put(user.username(), user);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteUsers() throws DataAccessException {
        try {
            if (!users.isEmpty()) {
                users.clear();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try {
            return users.get(username);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
