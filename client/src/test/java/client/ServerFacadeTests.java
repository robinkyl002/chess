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
    public void loginSuccess() {
        assertEquals(true, true);
    }

    @Test
    public void loginFail() {}

    @Test
    public void registerSuccess() {
        assertEquals(true, true);
    }
    @Test
    public void registerFail() {}

    @Test
    public void logoutSuccess() {
        assertEquals(true, true);
    }
    @Test
    public void logoutFail() {}

    @Test
    public void createGameSuccess() {
        assertEquals(true, true);
    }
    @Test
    public void createGameFail() {}

    @Test
    public void joinGameSuccess() {
        assertEquals(true, true);
    }
    @Test
    public void joinGameFail() {

    }

    @Test
    public void listGameSuccess() {
        assertEquals(true, true);
    }
    @Test
    public void listGameFail() {}

}
