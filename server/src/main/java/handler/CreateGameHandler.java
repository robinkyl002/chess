package handler;

import com.google.gson.Gson;
import exception.ResponseException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import service.*;

import static exception.ResponseException.errorMessageFromCode;
import static exception.ResponseException.Code.*;

public class CreateGameHandler implements Handler {
    private final UserService userService;
    private final GameService gameService;

    public CreateGameHandler(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    public void handle(@NotNull Context context) throws ResponseException {
        String authToken = context.header("Authorization");

        if (!userService.validAuth(authToken)) {
            throw new ResponseException(UnauthorizedError, errorMessageFromCode(UnauthorizedError));
        }
        var createGameRequest = new Gson().fromJson(context.body(), CreateGameRequest.class);

        var result = gameService.createGame(createGameRequest);

        context.status(200).json(new Gson().toJson(result));

    }
}
