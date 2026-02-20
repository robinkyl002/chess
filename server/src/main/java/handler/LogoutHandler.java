package handler;

import exception.ResponseException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import service.LogoutRequest;
import service.UserService;

public class LogoutHandler implements Handler {
    private final UserService userService;

    public LogoutHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(@NotNull Context context) throws ResponseException {
        String authToken = context.header("Authorization");
        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException(ResponseException.Code.UnauthorizedError, "Error: unauthorized");
        }

        userService.logout(new LogoutRequest(authToken));

        context.status(200).json("");
    }
}
