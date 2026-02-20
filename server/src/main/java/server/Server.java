package server;

import dataaccess.*;
import exception.ResponseException;
import handler.*;
import io.javalin.*;
import io.javalin.http.Context;
import service.UserService;

public class Server {

    private final Javalin javalin;
    private final UserService userService;

    public Server() {
        final UserDAO userDAO = new MemoryUserDAO();
        final AuthDAO authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        createHandlers();

        // Register your endpoints and exception handlers here.

    }

    private void createHandlers() {
        javalin.post("/user", new RegisterHandler(userService))
                .post("/session", new LoginHandler(userService))
                .delete("/session", new LogoutHandler(userService))
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
