package server;

import dataaccess.*;
import exception.ResponseException;
import handler.*;
import handler.websocket.WebsocketHandler;
import io.javalin.*;
import io.javalin.http.Context;
import service.GameService;
import service.UserService;

public class Server {

    private final Javalin javalin;
    private final UserService userService;
    private final GameService gameService;
    private final WebsocketHandler websocketHandler;

    public Server() {
        boolean sql = true;

        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        if (sql) {
            try {
                userDAO = new SQLUserDAO();
                authDAO = new SQLAuthDAO();
                gameDAO = new SQLGameDAO();
            } catch (Exception e) {
                throw new RuntimeException("Could not connect to database" + e.getMessage());
            }
        }

        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(authDAO, gameDAO);
        this.websocketHandler = new WebsocketHandler();
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        createHandlers();

        // Register your endpoints and exception handlers here.

    }

    public Server(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
        this.websocketHandler = new WebsocketHandler();

        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        createHandlers();
    }

    private void createHandlers() {
        javalin.post("/user", new RegisterHandler(userService))
                .post("/session", new LoginHandler(userService))
                .delete("/session", new LogoutHandler(userService))
                .post("/game", new CreateGameHandler(userService, gameService))
                .put("/game", new JoinGameHandler(userService, gameService))
                .get("/game", new ListGamesHandler(userService, gameService))
                .delete("/db", new ClearDataHandler(userService, gameService))
                .ws("/ws", ws -> {
                    ws.onConnect(websocketHandler);
                    ws.onClose(websocketHandler);
                    ws.onMessage(websocketHandler);
                })
                .exception(ResponseException.class, this::exceptionHandler);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void exceptionHandler (ResponseException ex, Context ctx) {
        ctx.status(ex.toHttpStatusCode());
        ctx.json(ex.toJson());
    }
}
