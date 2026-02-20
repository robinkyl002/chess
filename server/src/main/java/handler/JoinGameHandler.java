package handler;

import com.google.gson.Gson;
import exception.ResponseException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import service.GameService;
import service.JoinGameRequest;
import service.UserService;
import static exception.ResponseException.Code.*;
import static exception.ResponseException.errorMessageFromCode;

public class JoinGameHandler implements Handler {
    private final UserService userService;
    private final GameService gameService;

    public JoinGameHandler(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    public void handle(@NotNull Context context) throws ResponseException {
        String authToken = context.header("Authorization");
        if (!userService.validAuth(authToken)) {
            throw new ResponseException(UnauthorizedError, errorMessageFromCode(UnauthorizedError));
        }

        var request = new Gson().fromJson(context.body(), JoinGameRequest.class);
        gameService.joinGame(request, authToken);
        context.status(200).json("");
    }
}
