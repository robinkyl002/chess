package service;

import dataaccess.*;
import exception.ResponseException;
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
    void registerAlreadyTaken() {
    }

    @Test
    @Order(3)
    @DisplayName("Login Successful")
    void loginSuccessful() {
    }

    @Test
    @Order(4)
    @DisplayName("Unauthorized Login")
    void loginUnauthorized() {
    }

    @Test
    @Order(5)
    @DisplayName("Logout Successful")
    void logoutSuccessful() {
    }

    @Test
    @Order(6)
    @DisplayName("Unauthorized Logout")
    void logoutUnauthorized() {
    }
}