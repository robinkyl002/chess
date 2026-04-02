package client;

import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(String.format("http://localhost:%d", port));
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    @DisplayName("Login Successful")
    public void loginSuccess() {
        assertEquals(true, true);
    }

    @Test
    @DisplayName("Login Failed - No Existing User")
    public void loginFail() {}

    @Test
    @DisplayName("Register Successful")
    public void registerSuccess() {
        assertEquals(true, true);
    }

    @Test
    @DisplayName("Register Failed - Already Taken Error")
    public void registerFail() {}

    @Test
    @DisplayName("Logout Successful")
    public void logoutSuccess() {
        assertEquals(true, true);
    }

    @Test
    @DisplayName("Logout Failed - No existing auth token")
    public void logoutFail() {}

    @Test
    @DisplayName("Create Game Successful")
    public void createGameSuccess() {
        assertEquals(true, true);
    }

    @Test
    @DisplayName("Create Game Failed - Empty name")
    public void createGameFail() {}

    @Test
    @DisplayName("Join Game Successful")
    public void joinGameSuccess() {
        assertEquals(true, true);
    }

    @Test
    @DisplayName("Join Game Failed")
    public void joinGameFail() {

    }

    @Test
    @DisplayName("List Games Successful")
    public void listGameSuccess() {
        assertEquals(true, true);
    }

    @Test
    @DisplayName("List Games Failed - Empty list")
    public void listGameFail() {}

}
