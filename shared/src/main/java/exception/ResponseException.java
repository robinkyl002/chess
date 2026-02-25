package exception;

import com.google.gson.Gson;

//import java.util.HashMap;
import java.util.Map;

public class ResponseException extends Exception {

    public enum Code {
        BadRequestError,
        UnauthorizedError,
        AlreadyTakenError,
        ServerError
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

    public static String errorMessageFromCode(Code enteredCode) {
        return switch(enteredCode) {
            case ServerError -> "Error: ";
            case BadRequestError -> "Error: bad request";
            case UnauthorizedError -> "Error: unauthorized";
            case AlreadyTakenError -> "Error: already taken";
        };
    }
}
