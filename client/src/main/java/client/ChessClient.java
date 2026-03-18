package client;

import exception.ResponseException;
import server.ServerFacade;
import service.LoginRequest;
import service.RegisterRequest;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient {
    private String username = null;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private String authToken;

    public ChessClient(String url) {
        server = new ServerFacade(url);
        authToken = "";
    }

    public void run() {
        System.out.println(" Welcome to Chess. Sign in to start.");
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
//                System.out.println("working on it");
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> null;
                case "list" -> null;
                case "logout" -> null;
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String ...params) throws ResponseException {
        if (params.length >= 3) {
            var result = server.register(new RegisterRequest(params[0], params[1], params[2]));
            authToken = result.authToken();
            username = result.username();
            return String.format("Logged in as %s", result.username());
        }
        throw new ResponseException(ResponseException.Code.BadRequestError, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String login(String ...params) throws ResponseException {
        if (params.length >= 2) {
            var result = server.login(new LoginRequest(params[0], params[1]));
            authToken = result.authToken();
            username = String.format(" - " + result.username());
        }
        return null;
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    - login <USERNAME> <PASSWORD> - to play chess
                    - quit - playing chess
                    - help - with possible commands
                    """;
        }
        return """
                - create <NAME> - to start a new game
                - join <ID> - to join a game
                - list - to see all existing games
                - logout
                - quit - playing chess
                - help - with possible commands
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(ResponseException.Code.UnauthorizedError, "You must sign in");
        }
    }

}
