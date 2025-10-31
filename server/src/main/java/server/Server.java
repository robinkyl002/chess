package server;

import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.*;
import io.javalin.http.Context;
import model.AuthData;
import model.UserData;
import service.UserService;

public class Server {

    private final Javalin httpHandler;
    private final UserService userService;
    private final UserDAO userDataAccess;
    private final AuthDAO authDataAccess;

    public Server() {
        userDataAccess = new MemoryUserDataAccess();
        authDataAccess = new MemoryAuthDataAccess();
        userService = new UserService(userDataAccess, authDataAccess);


        httpHandler = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", this::registerUser);

        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        httpHandler.start(desiredPort);
        return httpHandler.port();
    }

    public int port() { return httpHandler.port(); }

    public void stop() {
        httpHandler.stop();
    }

    private void registerUser (Context ctx) throws DataAccessException {
        UserData user = new Gson().fromJson(ctx.body(), UserData.class);
        AuthData auth = userService.createUser(user);

        ctx.json(new Gson().toJson(auth));
        // return auth;
    }
}
