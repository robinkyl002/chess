package handler;

import com.google.gson.Gson;
import exception.ResponseException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import service.*;

import static exception.ResponseException.Code.*;
import static exception.ResponseException.errorMessageFromCode;

public class ListGamesHandler implements Handler {
    private final UserService userService;
    private final GameService gameService;

    public ListGamesHandler(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    public void handle(@NotNull Context context) throws ResponseException {
        String authToken = context.header("Authorization");

        if (!userService.validAuth(authToken)) {
            throw new ResponseException(UnauthorizedError, errorMessageFromCode(UnauthorizedError));
        }

        ListGamesResult result = gameService.listGames();

        context.status(200).json(new Gson().toJson(result));
    }
}
