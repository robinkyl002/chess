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
                .post("/game", this::createGame)
                .put("/game", this::joinGame)
                .get("/game", this::listGames)
                .delete("/session", this::logoutUser)
                .delete("/db", this::clear)
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
        UserData user = new Gson().fromJson(ctx.body(), UserData.class);
        if (user.username().isEmpty() || user.password().isEmpty() || user.email().isEmpty()) {
            throw new ResponseException(ResponseException.Code.BadRequestError, "Error: bad request");
        }
        AuthData auth = userService.createUser(user);

        ctx.status(200).result(new Gson().toJson(auth));
    }

    private void exceptionHandler(ResponseException ex, Context ctx) {
        ctx.status(ex.toHttpStatusCode());
        ctx.result(ex.toJson());
    }

    private void loginUser(Context ctx) throws DataAccessException, ResponseException {
        UserData user = new Gson().fromJson(ctx.body(), UserData.class);

        if (user.username().isEmpty() || user.password().isEmpty()) {
            throw new ResponseException(ResponseException.Code.BadRequestError, "Error: bad request");
        }
        AuthData auth = userService.login(user);

        ctx.status(200).result(new Gson().toJson(auth));
    }

    private void logoutUser(Context ctx) throws DataAccessException, ResponseException{
        String authToken = ctx.header("Authorization");
        userService.logout(authToken);
        ctx.status(200).result("");
    }

    private void createGame(Context ctx) throws DataAccessException, ResponseException {

    }

    private void listGames(Context ctx) throws DataAccessException, ResponseException {

    }

    private void joinGame(Context ctx) throws DataAccessException, ResponseException {

    }

    private void clear(Context ctx) throws DataAccessException, ResponseException {

    }
}
