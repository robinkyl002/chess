package server;

import dataaccess.*;
import handler.RegisterHandler;
import io.javalin.*;
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
        createHandlers(javalin, userService);

        // Register your endpoints and exception handlers here.

    }

//    public Server(UserService userService) {
//        javalin = Javalin.create(config -> config.staticFiles.add("web"));
//        createHandlers(javalin, userService);
//    }

    private void createHandlers(Javalin javalin, UserService userService) {
        javalin.post("/user", new RegisterHandler(userService));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
