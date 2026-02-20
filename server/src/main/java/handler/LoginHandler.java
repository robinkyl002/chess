package handler;

import com.google.gson.Gson;
import exception.ResponseException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import service.LoginRequest;
import service.LoginResult;
import service.UserService;

import static exception.ResponseException.Code.BadRequestError;
import static exception.ResponseException.errorMessageFromCode;

public class LoginHandler implements Handler {
    private final UserService userService;

    public LoginHandler(UserService userService) {
        this.userService = userService;
    }
    @Override
    public void handle(@NotNull Context context) throws ResponseException {
        var loginRequest = new Gson().fromJson(context.body(), LoginRequest.class);

        if (loginRequest.username() == null || loginRequest.password() == null
                || loginRequest.username().isEmpty() || loginRequest.password().isEmpty()) {
            throw new ResponseException(BadRequestError, errorMessageFromCode(BadRequestError));
        }
        LoginResult result = userService.login(loginRequest);
        context.status(200);
        context.json(new Gson().toJson(result));
    }
}
