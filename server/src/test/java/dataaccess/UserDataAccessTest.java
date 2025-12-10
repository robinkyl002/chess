package dataaccess;

import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import passoff.model.TestUser;

import static org.junit.jupiter.api.Assertions.*;

class UserDataAccessTest {

    private UserDAO getDataAccess(Class<? extends UserDAO> databaseClass) throws DataAccessException {
        UserDAO db;
        if (databaseClass.equals(SQLUserDataAccess.class)) {
            db = new SQLUserDataAccess();
        } else {
            db = new MemoryUserDataAccess();
        }
        db.clearUserData();
        return db;
    }
    private static TestUser existingUser;

    @BeforeEach
    void prep() {

        existingUser = new TestUser("username", "password", "email");
    }

    @ParameterizedTest
    @DisplayName("Successful Registration")
    @ValueSource(classes = {SQLUserDataAccess.class, MemoryUserDataAccess.class})
    void createUserSuccessful(Class<? extends UserDAO> databaseClass) throws DataAccessException {
        var userDAO = getDataAccess(databaseClass);

        assertDoesNotThrow(() -> userDAO.createUser(new UserData(existingUser.getUsername(),
                existingUser.getPassword(), existingUser.getEmail())));
    }

    @ParameterizedTest
    @DisplayName("Username is null")
    @ValueSource(classes = {SQLUserDataAccess.class, MemoryUserDataAccess.class})
    void usernameIsNull (Class<? extends UserDAO> databaseClass) throws DataAccessException {
        var userDAO = getDataAccess(databaseClass);

        assertThrows(DataAccessException.class, () -> userDAO.createUser(new UserData(null,
                existingUser.getPassword(), existingUser.getEmail())));
    }

    @ParameterizedTest
    @DisplayName("Retrieved user successfully")
    @ValueSource(classes = {SQLUserDataAccess.class, MemoryUserDataAccess.class})
    void getUserSuccessful(Class<? extends UserDAO> databaseClass) throws DataAccessException {
        var userDAO = getDataAccess(databaseClass);

        userDAO.createUser(new UserData(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail()));

        assertDoesNotThrow(() -> userDAO.getUser(existingUser.getUsername()));
        assertEquals(existingUser.getUsername(), userDAO.getUser(existingUser.getUsername()).username());
    }

    @ParameterizedTest
    @DisplayName("No user exists")
    @ValueSource(classes = {SQLUserDataAccess.class, MemoryUserDataAccess.class})
    void userDoesNotExist(Class<? extends UserDAO> databaseClass) throws DataAccessException {
        var userDAO = getDataAccess(databaseClass);

        var user = userDAO.getUser(existingUser.getUsername());
        assertNull(user);
    }

    @ParameterizedTest
    @DisplayName("User Data deleted")
    @ValueSource(classes = {SQLUserDataAccess.class, MemoryUserDataAccess.class})
    void clearUserData(Class<? extends UserDAO> databaseClass) throws DataAccessException {
        var userDAO = getDataAccess(databaseClass);

        userDAO.createUser(new UserData(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail()));
        userDAO.createUser(new UserData("newUser", "diffPassword", "special.email"));

        userDAO.clearUserData();

        var firstUser = userDAO.getUser("username");
        var secondUser = userDAO.getUser("newUser");
        assertNull(firstUser);
        assertNull(secondUser);
    }
}