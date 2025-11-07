package server;

import com.google.gson.Gson;
import dataaccess.*;
import exception.ResponseException;
import io.javalin.*;
import io.javalin.http.Context;
import model.AuthData;
import model.UserData;
import service.UserService;

import java.util.Map;

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
                .post("/user", this::registerUser)
                .post("/session", this::loginUser)
                .delete("/session", this::logoutUser)
                .exception(ResponseException.class, this::exceptionHandler);

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

    private void registerUser (Context ctx) throws ResponseException, DataAccessException {
        try {
            UserData user = new Gson().fromJson(ctx.body(), UserData.class);
            AuthData auth = userService.createUser(user);

            ctx.status(200).result(new Gson().toJson(auth));
        } catch (Exception e) {
            throw e;
        }
    }

    private void exceptionHandler(ResponseException ex, Context ctx) {
        ctx.status(ex.toHttpStatusCode());
        ctx.result(ex.toJson());
    }

    private void loginUser(Context ctx) throws DataAccessException, ResponseException {
        try {
            UserData user = new Gson().fromJson(ctx.body(), UserData.class);

            if (user.username().isEmpty() || user.password().isEmpty()) {
                ctx.status(400).result(new Gson().toJson(Map.of("message", "Error: bad request")));
                return;
            }
            AuthData auth = userService.login(user);

            ctx.status(200).result(new Gson().toJson(auth));
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    private void logoutUser(Context ctx) throws DataAccessException {
        String authToken = ctx.header("Authorization");

        userService.logout(authToken);

        ctx.status(200).result("");
    }
}
