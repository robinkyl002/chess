package handler;

import com.google.gson.Gson;
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
        var logoutRequest = new Gson().fromJson(context.header("Authorization"), LogoutRequest.class);

        userService.logout(logoutRequest);

        context.status(200);
        context.json("");
    }
}
