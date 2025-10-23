package dataaccess;

import model.UserData;

public class MemoryUserDataAccess implements UserDAO{
    @Override
    public void createUser(UserData data) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clearUserData() {

    }
}
