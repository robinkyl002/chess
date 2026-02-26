package service;

import dataaccess.*;
import exception.ResponseException;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import passoff.model.TestUser;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private final UserDAO userDAO = new MemoryUserDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();
    private final UserService userService = new UserService(userDAO, authDAO);
    private static TestUser existingUser;

    @BeforeEach
    void setup () throws ResponseException {
        userService.clearUserData();
        userService.clearAuthData();

        existingUser = new TestUser("ExistingUser", "existingUserPassword", "eu@mail.com");
    }

    @Test
    @Order(1)
    @DisplayName("Register Successful")
    void registerSuccessful() throws ResponseException {
        var registerResult = userService.register(new RegisterRequest("newUser", "newUserPassword", "nu@mail.com"));

        assertNotNull(registerResult);
        assertEquals("newUser", registerResult.username());
        assertNotNull(registerResult.authToken());
    }

    @Test
    @Order(2)
    @DisplayName("Register Failed - Already Taken")
    void registerAlreadyTaken() throws ResponseException {
        userService.register(new RegisterRequest(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail()));
        assertThrows(ResponseException.class, () -> {
            userService.register(new RegisterRequest(existingUser.getUsername(),
                    existingUser.getPassword(), existingUser.getEmail()));
        }, "Error: already taken");
    }

    @Test
    @Order(3)
    @DisplayName("Login Successful")
    void loginSuccessful() throws ResponseException {
        userService.register(new RegisterRequest(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail()));

        var loginResult = userService.login(new LoginRequest(existingUser.getUsername(), existingUser.getPassword()));

        assertNotNull(loginResult);
        assertEquals(existingUser.getUsername(), loginResult.username());
        assertNotNull(loginResult.authToken());
    }

    @Test
    @Order(4)
    @DisplayName("Unauthorized Login")
    void loginUnauthorized() throws ResponseException {
        userService.register(new RegisterRequest(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail()));

        assertThrows(ResponseException.class, () -> {
            userService.login(new LoginRequest(existingUser.getUsername(), "wrongPassword"));
        }, "Error: unauthorized");
    }

    @Test
    @Order(5)
    @DisplayName("Logout Successful")
    void logoutSuccessful() throws ResponseException {
        var registerResult = userService.register(new RegisterRequest(existingUser.getUsername(),
                existingUser.getPassword(), existingUser.getEmail()));

        assertDoesNotThrow(() -> {userService.logout(new LogoutRequest(registerResult.authToken())); });
    }

    static class BrokenAuthDAO implements AuthDAO {

        @Override
        public AuthData createAuth(String username) throws DataAccessException {
            return null;
        }

        @Override
        public AuthData getAuth(String authToken) throws DataAccessException {
            return null;
        }

        @Override
        public void deleteAuth(String authToken) throws DataAccessException {
            throw new DataAccessException("Could not delete token");
        }

        @Override
        public void clearAuthData() throws DataAccessException {

        }
    }

    @Test
    @Order(6)
    @DisplayName("Logout - Server Error")
    void logoutDataAccessException() throws ResponseException {
        var brokenUserService = new UserService(userDAO, new BrokenAuthDAO());

        assertThrows(ResponseException.class, () -> {
            brokenUserService.logout(new LogoutRequest("authToken"));
        });
    }
}