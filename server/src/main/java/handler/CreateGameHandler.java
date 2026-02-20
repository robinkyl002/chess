package handler;

import exception.ResponseException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import service.GameService;
import service.UserService;

public class CreateGameHandler implements Handler {
    private final UserService userService;
    private final GameService gameService;

    public CreateGameHandler(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    public void handle(@NotNull Context context) throws ResponseException {

    }
}
