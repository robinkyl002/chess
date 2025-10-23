package dataaccess;

import model.AuthData;

public class MemoryAuthDataAccess implements AuthDAO {

    @Override
    public void createAuth(AuthData data) throws DataAccessException {

    }

    @Override
    public AuthData getAuth(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String username) throws DataAccessException {

    }

    @Override
    public void clearAuthData() {

    }
}
