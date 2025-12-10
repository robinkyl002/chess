package service;

import dataaccess.*;
import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.*;
import passoff.model.TestUser;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private final boolean sql = true;
    private final UserDAO userDAO = (sql) ? new SQLUserDataAccess() : new MemoryUserDataAccess();
    private final AuthDAO authDAO = (sql) ? new SQLAuthDataAccess() : new MemoryAuthDataAccess();
    private final UserService userService = new UserService(userDAO, authDAO);
    private static TestUser existingUser;

    @BeforeEach
    public void prep() throws ResponseException {
        userService.clearAuthData();
        userService.clearUserData();

        existingUser = new TestUser("ExistingUser", "existingUserPassword", "eu@mail.com");
    }

    @Test
    @DisplayName("Register Success")
    void createUserSuccessful() throws ResponseException {
        AuthData auth = userService.createUser(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());

        assertNotNull(auth);
        assertEquals(auth.username(), existingUser.getUsername());
        assertNotNull(auth.authToken());

    }

    @Test
    @DisplayName("Re-Register User")
    void userAlreadyExists () throws ResponseException {
        userService.createUser(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());

        assertThrows(ResponseException.class, () -> {
            userService.createUser(existingUser.getUsername(),
                    existingUser.getPassword(), existingUser.getEmail());
        });
    }

    @Test
    @DisplayName("Successful Login")
    void login() throws ResponseException, DataAccessException {
        AuthData registerAuth = userService.createUser(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());

        userService.logout(registerAuth.authToken());

        UserData newUser = new UserData(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());

        AuthData loginAuth = userService.login(newUser);

        assertNotNull(loginAuth);
        assertEquals(loginAuth.username(), newUser.username(), "Username of the AuthData does not match the username used to login");
    }

    @Test
    @DisplayName("Wrong Password")
    void loginWrongPassword() throws ResponseException {
        AuthData registerAuth = userService.createUser(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());

        userService.logout(registerAuth.authToken());

        UserData newUser = new UserData(existingUser.getUsername(), "WrongPassword", existingUser.getEmail());

        assertThrows(ResponseException.class, () -> {
            userService.login(newUser);
        });
    }

    @Test
    @DisplayName("Successful Logout")
    void logout() throws ResponseException {
        AuthData auth = userService.createUser(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());

        assertDoesNotThrow(() -> userService.logout(auth.authToken()));
    }

    @Test
    @DisplayName("Unauthorized logout")
    void logoutErrorNoAuth() throws ResponseException {
        AuthData auth = userService.createUser(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());

        userService.logout(auth.authToken());
        assertThrows(ResponseException.class, () -> userService.logout(auth.authToken()));
    }
}