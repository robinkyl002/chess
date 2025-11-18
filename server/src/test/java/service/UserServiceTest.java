package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryUserDataAccess;
import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.*;
import passoff.model.TestUser;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private final UserService userService = new UserService(new MemoryUserDataAccess(), new MemoryAuthDataAccess());
    private static TestUser existingUser;

    @BeforeEach
    public void prep() throws ResponseException {
        userService.clearAuthData();
        userService.clearUserData();

        existingUser = new TestUser("ExistingUser", "existingUserPassword", "eu@mail.com");
    }

    @Test
    @DisplayName("Register Success")
    void createUserSuccessful() throws ResponseException, DataAccessException {
        AuthData auth = userService.createUser(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());

        assertNotNull(auth);
        assertEquals(auth.username(), existingUser.getUsername());
        assertNotNull(auth.authToken());

    }

    @Test
    @DisplayName("Re-Register User")
    void userAlreadyExists () throws ResponseException, DataAccessException {
        userService.createUser(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());

        assertThrows(ResponseException.class, () -> {
            userService.createUser(existingUser.getUsername(),
                    existingUser.getPassword(), existingUser.getEmail());
        });
    }

    @Test
    @DisplayName("Successful Login")
    void login() throws ResponseException, DataAccessException {
        userService.createUser(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());
    }

    @Test
    @DisplayName("Wrong Password")
    void loginWrongPassword() {

    }

    @Test
    @DisplayName("Successful Logout")
    void logout() throws ResponseException, DataAccessException {
        AuthData auth = userService.createUser(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());

        assertDoesNotThrow(() -> userService.logout(auth.authToken()));
    }

    @Test
    @DisplayName("Unauthorized logout")
    void logoutErrorNoAuth() throws ResponseException, DataAccessException {
        AuthData auth = userService.createUser(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());

        userService.logout(auth.authToken());
        assertThrows(ResponseException.class, () -> userService.logout(auth.authToken()));
    }
}