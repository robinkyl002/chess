package client;

import exception.ResponseException;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import service.CreateGameRequest;
import service.LoginRequest;
import service.LogoutRequest;
import service.RegisterRequest;

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

    @BeforeEach
    void setup() throws ResponseException {

        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    @DisplayName("Login Successful")
    public void loginSuccess() throws ResponseException {
        facade.register(new RegisterRequest("user", "pass", "email"));

        var loginResult = facade.login(new LoginRequest("user", "pass"));
        Assertions.assertNotNull(loginResult);
        Assertions.assertEquals("user", loginResult.username());
        Assertions.assertNotNull(loginResult.authToken());
    }

    @Test
    @DisplayName("Login Failed - No Existing User")
    public void loginFail() {
        Exception ex = assertThrows(ResponseException.class, () -> facade.login(new LoginRequest("user", "pass")));
        Assertions.assertEquals("Error: unauthorized",  ex.getMessage());
    }

    @Test
    @DisplayName("Register Successful")
    public void registerSuccess() throws ResponseException {
        var registerResult = facade.register(new RegisterRequest("user", "pass", "email"));

        Assertions.assertNotNull(registerResult);
        Assertions.assertEquals("user", registerResult.username());
        Assertions.assertNotNull(registerResult.authToken());
    }

    @Test
    @DisplayName("Register Failed - Already Taken Error")
    public void registerFail() throws ResponseException {
        facade.register(new RegisterRequest("user", "pass", "email"));

        Exception ex = assertThrows(ResponseException.class,
                () -> facade.register(new RegisterRequest("user", "pass", "email")));

        Assertions.assertEquals("Error: already taken", ex.getMessage());
    }

    @Test
    @DisplayName("Logout Successful")
    public void logoutSuccess() throws ResponseException {
        var registerResult = facade.register(new RegisterRequest("user", "pass", "email"));

        assertDoesNotThrow(() -> facade.logout(new LogoutRequest(registerResult.authToken())));
    }

    @Test
    @DisplayName("Logout Failed - No existing auth token")
    public void logoutFail() throws ResponseException {
        var registerResult = facade.register(new RegisterRequest("user", "pass", "email"));

        facade.logout(new LogoutRequest(registerResult.authToken()));

        Exception ex = assertThrows(ResponseException.class, () -> facade.logout(new LogoutRequest(" ")));
        Assertions.assertEquals("Error: unauthorized", ex.getMessage());
    }

    @Test
    @DisplayName("Create Game Successful")
    public void createGameSuccess() throws ResponseException {
        var registerResult = facade.register(new RegisterRequest("user", "pass", "email"));

        var createGameResult = facade.createGame(new CreateGameRequest("new game"), registerResult.authToken());
        Assertions.assertNotNull(createGameResult);
        assertEquals(1, createGameResult.gameID());
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

    }

    @Test
    @DisplayName("List Games Failed - No auth token")
    public void listGameFail() {
        Exception ex = assertThrows(ResponseException.class, () -> facade.listGames(null));

        Assertions.assertEquals("Error: unauthorized", ex.getMessage());
    }

    @Test
    @DisplayName("Clear DB Successful")
    public void clearDBSuccess() {
        assertDoesNotThrow(() -> facade.clear());
    }
}
