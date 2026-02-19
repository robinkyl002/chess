package handler;

import com.google.gson.Gson;
import exception.ResponseException;
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

    public void handle(@NotNull Context context) throws ResponseException {
        var registerRequest = new Gson().fromJson(context.body(), RegisterRequest.class);

        if (registerRequest.username() == null || registerRequest.password() == null
                || registerRequest.email() == null || registerRequest.username().isEmpty()
                || registerRequest.password().isEmpty() || registerRequest.email().isEmpty()) {
            throw new ResponseException(ResponseException.Code.BadRequestError, "Error: bad request");
        }
        var result = userService.register(registerRequest);
        context.status(200);
        context.json(new Gson().toJson(result));
    }
}
