package server;

import dataaccess.*;
import exception.ResponseException;
import handler.LoginHandler;
import handler.RegisterHandler;
import io.javalin.*;
import io.javalin.http.Context;
import service.UserService;

public class Server {

    private final Javalin javalin;
    private final UserService userService;
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public Server() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        createHandlers();

        // Register your endpoints and exception handlers here.

    }

//    public Server(UserService userService) {
//        javalin = Javalin.create(config -> config.staticFiles.add("web"));
//        createHandlers(javalin, userService);
//    }

    private void createHandlers() {
        javalin.post("/user", new RegisterHandler(userService))
                .post("/session", new LoginHandler(userService))
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
