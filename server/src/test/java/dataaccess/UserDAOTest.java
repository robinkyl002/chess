package dataaccess;

import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import passoff.model.TestUser;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    private UserDAO getDataAccess(Class<? extends UserDAO> userDAOClass) {
        try {
            UserDAO db;
            if (userDAOClass.equals(MemoryUserDAO.class)) {
                db = new MemoryUserDAO();
            } else {
                db = new SQLUserDAO();
            }
            db.deleteUsers();
            return db;
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize UserDAO for class");
        }
    }
    private static TestUser existingUser;

    @BeforeEach
    void setup() {
        existingUser = new TestUser("username", "password", "email");
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryUserDAO.class, SQLUserDAO.class})
    @DisplayName("Successfully created a user")
    void createUserSuccessful(Class<?extends UserDAO> dbClass) {
        UserDAO userDAO = getDataAccess(dbClass);
        assertDoesNotThrow(() -> {userDAO.createUser(new UserData(existingUser.getUsername(),
                existingUser.getPassword(), existingUser.getEmail()));});}

    @ParameterizedTest
    @ValueSource(classes = {MemoryUserDAO.class, SQLUserDAO.class})
    @DisplayName("User already exists")
    void createUserFailed(Class<?extends UserDAO> dbClass) throws DataAccessException {
        UserDAO userDAO = getDataAccess(dbClass);

        userDAO.createUser(new UserData(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail()));
        assertThrows(DataAccessException.class, () -> {
            userDAO.createUser(new UserData(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail()));
        });
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryUserDAO.class, SQLUserDAO.class})
    @DisplayName("Successfully deleted all users")
    void deleteUsers(Class<? extends UserDAO> dbClass) {
        UserDAO userDAO = getDataAccess(dbClass);

        assertDoesNotThrow(userDAO::deleteUsers);
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryUserDAO.class, SQLUserDAO.class})
    @DisplayName("Successfully retrieved user")
    void getUserSuccessful(Class<? extends UserDAO> dbClass) throws DataAccessException {
        UserDAO userDAO = getDataAccess(dbClass);
        var userObject = new UserData(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());
        userDAO.createUser(userObject);

        var user = userDAO.getUser(existingUser.getUsername());

        assertNotNull(user);
        assertEquals(userObject.username(), user.username());
        assertEquals(userObject.email(), user.email());
        assertNotNull(user.password());
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryUserDAO.class, SQLUserDAO.class})
    @DisplayName("User Does not exist")
    void userDoesNotExist (Class<? extends UserDAO> dbClass) throws DataAccessException {
        UserDAO userDAO = getDataAccess(dbClass);

        var user = userDAO.getUser(existingUser.getUsername());
        assertNull(user);
    }
}