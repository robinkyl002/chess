package handler;

import exception.ResponseException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import service.GameService;
import service.UserService;

public class ClearDataHandler implements Handler {
    private final UserService userService;
    private final GameService gameService;

    public ClearDataHandler(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    public void handle(@NotNull Context context) throws ResponseException {
        userService.clearUserData();
        userService.clearAuthData();
        gameService.clearAllGames();
    }
}
