package handler;

import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import service.RegisterRequest;
import service.UserService;

public class RegisterHandler implements Handler {
    private final UserService userService;

    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }

    public void handle(@NotNull Context context) {
        var registerRequest = new Gson().fromJson(context.body(), RegisterRequest.class);

        var result = userService.register(registerRequest);
        context.status(200);
        context.json(new Gson().toJson(result));
    }
}
