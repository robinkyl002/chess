package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import passoff.model.TestUser;

import static org.junit.jupiter.api.Assertions.*;

class AuthDAOTest {
    private AuthDAO getDataAccess(Class<? extends AuthDAO> authDAOClass) {
        try {
            AuthDAO db;
            if (authDAOClass.equals(MemoryAuthDAO.class)) {
                db = new MemoryAuthDAO();
            } else {
                db = new SQLAuthDAO();
            }
            db.clearAuthData();
            return db;
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize AuthDAO for tests");
        }
    }
    private static TestUser existingUser;

    @BeforeEach
    void setup() {
        existingUser = new TestUser("username", "password", "email");
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryAuthDAO.class, SQLAuthDAO.class})
    @DisplayName("Create Auth Successful")
    void createAuthSuccessful(Class<? extends AuthDAO> authDAOClass) {
        AuthDAO authDAO = getDataAccess(authDAOClass);

        assertDoesNotThrow(() -> authDAO.createAuth(existingUser.getUsername()));
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryAuthDAO.class, SQLAuthDAO.class})
    @DisplayName("Get Auth Successful")
    void getAuthSuccessful(Class<? extends AuthDAO> authDAOClass) throws DataAccessException {
        AuthDAO authDAO = getDataAccess(authDAOClass);
        var authData = authDAO.createAuth(existingUser.getUsername());

        AuthData returnValue = authDAO.getAuth(authData.authToken());

        assertNotNull(returnValue);
        assertEquals(existingUser.getUsername(), returnValue.username());
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryAuthDAO.class, SQLAuthDAO.class})
    @DisplayName("Delete Auth Successful")
    void deleteAuth(Class<? extends AuthDAO> authDAOClass) throws DataAccessException {
        AuthDAO authDAO = getDataAccess(authDAOClass);
        var authData = authDAO.createAuth(existingUser.getUsername());

        assertDoesNotThrow(()->authDAO.deleteAuth(authData.authToken()));
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryAuthDAO.class, SQLAuthDAO.class})
    @DisplayName("Clear Auth Successful")
    void clearAuthData(Class<? extends AuthDAO> authDAOClass) {
        AuthDAO authDAO = getDataAccess(authDAOClass);
        assertDoesNotThrow(authDAO::clearAuthData);
    }
}