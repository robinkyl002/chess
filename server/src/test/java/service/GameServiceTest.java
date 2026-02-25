package service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    @Test
    @Order(1)
    @DisplayName("Create Game Successful")
    void createGameSuccessful() {
    }

    @Test
    @Order(2)
    @DisplayName("Create Game - Empty Game Name")
    void createGameEmptyGameName() {
    }

    @Test
    @Order(3)
    @DisplayName("Join Game Successful")
    void joinGameSuccessful() {
    }

    @Test
    @Order(4)
    @DisplayName("Join Game - Game ID Empty")
    void joinGame() {
    }

    @Test
    @Order(5)
    @DisplayName("List Games Successful")
    void listGamesSuccessful() {
    }

    @Test
    @Order(6)
    @DisplayName("List Games - Unauthorized")
    void listGamesUnauthorized() {
    }
}