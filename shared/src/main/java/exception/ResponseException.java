package exception;

import com.google.gson.Gson;

// import java.util.HashMap;
import java.util.Map;

public class ResponseException extends Exception {

    public enum Code {
        UnauthorizedError,
        AlreadyTakenError,
        BadRequestError,
        ServerError,
    }

    final private Code code;

    public ResponseException(Code code, String message) {
        super(message);
        this.code = code;
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage()));
    }

    public int toHttpStatusCode() {
        return switch (code) {
            case ServerError -> 500;
            case BadRequestError -> 400;
            case UnauthorizedError -> 401;
            case AlreadyTakenError -> 403;
        };
    }
}

