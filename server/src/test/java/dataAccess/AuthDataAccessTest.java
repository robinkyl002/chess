package dataAccess;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDataAccess;
import dataaccess.SQLAuthDataAccess;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import passoff.model.TestUser;

import static org.junit.jupiter.api.Assertions.*;

class AuthDataAccessTest {
    private static TestUser existingUser;

    @BeforeEach
    void prep() {
        existingUser = new TestUser("username", "password", "email");
    }

    private AuthDAO getDataAccess(Class<? extends AuthDAO> databaseClass) throws DataAccessException {
        AuthDAO db;
        if (databaseClass.equals(SQLAuthDataAccess.class)) {
            db = new SQLAuthDataAccess();
        } else {
            db = new MemoryAuthDataAccess();
        }
        db.clearAuthData();
        return db;
    }

    @ParameterizedTest
    @DisplayName("Create Auth Successful")
    @ValueSource(classes = {SQLAuthDataAccess.class, MemoryAuthDataAccess.class})
    void createAuth(Class<? extends AuthDAO> databaseClass) throws DataAccessException {
        var authDAO = getDataAccess(databaseClass);

        AuthData auth = authDAO.createAuth(existingUser.getUsername());

        assertNotNull(auth);
    }

    @ParameterizedTest
    @DisplayName("Create Auth Failed")
    @ValueSource(classes = {SQLAuthDataAccess.class, MemoryAuthDataAccess.class})
    void failedToCreateAuth(Class<? extends AuthDAO> databaseClass) throws DataAccessException {
        var authDAO = getDataAccess(databaseClass);

        assertThrows(DataAccessException.class, ()-> authDAO.createAuth(null));
    }

    @ParameterizedTest
    @DisplayName("Retrieved Auth")
    @ValueSource(classes = {SQLAuthDataAccess.class, MemoryAuthDataAccess.class})
    void getAuth(Class<? extends AuthDAO> databaseClass) throws DataAccessException {
        var authDAO = getDataAccess(databaseClass);

        var loggedInAuth = authDAO.createAuth(existingUser.getUsername());

        var retrievedAuth = authDAO.getAuth(loggedInAuth.authToken());
        assertNotNull(retrievedAuth);
    }

    @ParameterizedTest
    @DisplayName("Auth does not exist")
    @ValueSource(classes = {SQLAuthDataAccess.class, MemoryAuthDataAccess.class})
    void noAuth(Class<? extends AuthDAO> databaseClass) throws DataAccessException {
        var authDAO = getDataAccess(databaseClass);

        AuthData auth = authDAO.getAuth("abcd");
        assertNull(auth);
    }

    @ParameterizedTest
    @DisplayName("Delete a Single Auth")
    @ValueSource(classes = {SQLAuthDataAccess.class, MemoryAuthDataAccess.class})
    void deleteAuth(Class<? extends AuthDAO> databaseClass) throws DataAccessException {
        var authDAO = getDataAccess(databaseClass);

        var createdAuth = authDAO.createAuth(existingUser.getUsername());
        assertNotNull(createdAuth);

        authDAO.deleteAuth(createdAuth.authToken());

        var authAfterDelete = authDAO.getAuth(createdAuth.authToken());
        assertNull(authAfterDelete);
    }

    @ParameterizedTest
    @DisplayName("Auth does not exist")
    @ValueSource(classes = {SQLAuthDataAccess.class, MemoryAuthDataAccess.class})
    void authDoesNotExist(Class<? extends AuthDAO> databaseClass) throws DataAccessException {
        var authDAO = getDataAccess(databaseClass);

        assertDoesNotThrow(() -> authDAO.deleteAuth(null));
    }

    @ParameterizedTest
    @DisplayName("Clear Auth Data")
    @ValueSource(classes = {SQLAuthDataAccess.class, MemoryAuthDataAccess.class})
    void clearAuthData(Class<? extends AuthDAO> databaseClass) throws DataAccessException {
        var authDAO = getDataAccess(databaseClass);

        var auth = authDAO.createAuth(existingUser.getUsername());
        authDAO.clearAuthData();

        assertNull(authDAO.getAuth(auth.authToken()));
    }
}