package client;

import exception.ResponseException;
import server.ServerFacade;
import service.*;

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
                case "login" -> login(params);
                case "create" -> createGame(params);
                case "join" -> null;
                case "observe" -> null;
                case "list" -> null;
                case "logout" -> logout();
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
            return String.format("Logged in as %s", result.username());
        }
        throw new ResponseException(ResponseException.Code.BadRequestError, "Expected: <USERNAME> <PASSWORD>");
    }

    public String logout() throws ResponseException {
        assertSignedIn();
        server.logout(new LogoutRequest(authToken));
        authToken = null;
        return String.format("%s logged out.", username);
    }

    public String createGame(String ...params) throws ResponseException {
        assertSignedIn();
        if (params.length > 0) {
            String gameName = String.join(" ", params);
            var createGameRequest = new CreateGameRequest(gameName);
            var result = server.createGame(createGameRequest, authToken);
            return String.format("You created a game called %s. The id is %d", gameName, result.gameID());
        }
        throw new ResponseException(ResponseException.Code.BadRequestError, "Expected: <NAME>");
    }

    public String listGames() throws ResponseException {
        assertSignedIn();
        var gameListResult = server.listGames(authToken);

        return "";
    }

    public String help() {
        if (authToken == null || authToken.isEmpty()) {
            return """
                    - register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    - login <USERNAME> <PASSWORD> - to play chess
                    - quit - playing chess
                    - help - with possible commands
                    """;
        }
        return """
                - create <NAME> - to start a new game
                - join <ID> [WHITE|BLACK]- a game
                - observe <ID> - a game
                - list - to see all existing games
                - logout - when you are done
                - quit - playing chess
                - help - with possible commands
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException(ResponseException.Code.UnauthorizedError, "You must sign in");
        }
    }
}
