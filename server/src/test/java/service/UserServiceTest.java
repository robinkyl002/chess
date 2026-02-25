package service;

import dataaccess.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private final UserDAO userDAO = new MemoryUserDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();
    private final UserService userService = new UserService(userDAO, authDAO);
    void setup () {

    }

    @Test
    @Order(1)
    @DisplayName("Register Successful")
    void registerSuccessful() {
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