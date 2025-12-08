package server;

import com.google.gson.Gson;
import dataaccess.*;
import exception.ResponseException;
import io.javalin.*;
import io.javalin.http.Context;
import model.*;
import service.*;

public class Server {

    private final Javalin httpHandler;
    private final UserService userService;
    private final GameService gameService;

    public Server() {
        boolean sql = true;

        if (sql) {
            try {
                SQLInitializer.configureDatabase();
            }
            catch (ResponseException | DataAccessException ex) {
                throw new RuntimeException("Database could not be configured", ex);
            }
        }

        UserDAO userDataAccess = (sql) ? new SQLUserDataAccess() : new MemoryUserDataAccess();
        AuthDAO authDataAccess = (sql) ? new SQLAuthDataAccess() : new MemoryAuthDataAccess();
        GameDAO gameDataAccess = (sql) ? new SQLGameDataAccess() : new MemoryGameDataAccess();
        userService = new UserService(userDataAccess, authDataAccess);
        gameService = new GameService(gameDataAccess, authDataAccess);


        httpHandler = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", this::registerUser)
                .post("/session", this::loginUser)
                .post("/game", this::createGame)
                .put("/game", this::joinGame)
                .get("/game", this::listGames)
                .delete("/session", this::logoutUser)
                .delete("/db", this::clear)
                .exception(ResponseException.class, this::exceptionHandler);

    }

    public int run(int desiredPort) {
        httpHandler.start(desiredPort);
        return httpHandler.port();
    }

    // public int port() { return httpHandler.port(); }

    public void stop() {
        httpHandler.stop();
    }

    private void registerUser (Context ctx) throws ResponseException, DataAccessException {
        UserData user = new Gson().fromJson(ctx.body(), UserData.class);

        if (user.username() == null || user.password() == null || user.email() == null) {
            throw new ResponseException(ResponseException.Code.BadRequestError, "Error: bad request");
        }
        else if (user.username().isEmpty() || user.password().isEmpty() || user.email().isEmpty()) {
            throw new ResponseException(ResponseException.Code.BadRequestError, "Error: bad request");
        }
        AuthData auth = userService.createUser(user.username(), user.password(), user.email());

        ctx.status(200).result(new Gson().toJson(auth));
    }

    private void exceptionHandler(ResponseException ex, Context ctx) {
        ctx.status(ex.toHttpStatusCode());
        ctx.result(ex.toJson());
    }

    private void loginUser(Context ctx) throws DataAccessException, ResponseException {
        UserData user = new Gson().fromJson(ctx.body(), UserData.class);

        if (user.username() == null || user.password() == null) {
            throw new ResponseException(ResponseException.Code.BadRequestError, "Error: bad request");
        }
        else if (user.username().isEmpty() || user.password().isEmpty()) {
            throw new ResponseException(ResponseException.Code.BadRequestError, "Error: bad request");
        }
        AuthData auth = userService.login(user);

        ctx.status(200).result(new Gson().toJson(auth));
    }

    private void logoutUser(Context ctx) throws DataAccessException, ResponseException{
        String authToken = ctx.header("Authorization");

        if (userService.validAuth(authToken)) {
            userService.logout(authToken);
            ctx.status(200).result("");
        }
        else {
            throw new ResponseException(ResponseException.Code.UnauthorizedError, "Error: Unauthorized");
        }
    }

    private void createGame(Context ctx) throws DataAccessException, ResponseException {
        String authToken = ctx.header("Authorization");

        if (userService.validAuth(authToken)) {
            NewGameRequest gameRequest = new Gson().fromJson(ctx.body(), NewGameRequest.class);

            NewGameResult newGame = gameService.newGame(gameRequest.gameName());
            ctx.status(200).result(new Gson().toJson(newGame));
        }
        else {
            throw new ResponseException(ResponseException.Code.UnauthorizedError, "Error: Unauthorized");
        }
    }

    private void listGames(Context ctx) throws DataAccessException, ResponseException {
        String authToken = ctx.header("Authorization");

        if (userService.validAuth(authToken)) {

            ListGamesResult games = gameService.listGames();

            ctx.status(200).result(new Gson().toJson(games));
        }
        else {
            throw new ResponseException(ResponseException.Code.UnauthorizedError, "Error: Unauthorized");
        }

    }

    private void joinGame(Context ctx) throws DataAccessException, ResponseException {
        String authToken = ctx.header("Authorization");

        if (userService.validAuth(authToken)) {
            JoinGameRequest joinReq = new Gson().fromJson(ctx.body(), JoinGameRequest.class);
            gameService.joinGame(joinReq, authToken);

            ctx.status(200).result("");
        }
        else {
            throw new ResponseException(ResponseException.Code.UnauthorizedError, "Error: Unauthorized");
        }
    }

    private void clear(Context ctx) throws ResponseException {
        userService.clearUserData();
        userService.clearAuthData();
        gameService.clearGameData();
    }
}
