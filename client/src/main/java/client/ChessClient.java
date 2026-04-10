package client;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import server.ServerFacade;
import service.*;
import ui.ChessBoardRenderer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient implements NotificationHandler {
    private String username = null;
    private final ServerFacade server;
    private final WebsocketFacade ws;
    private String authToken;
    private final HashMap<Integer, Integer> gameIDs = new HashMap<>();

    public ChessClient(String url) throws ResponseException {
        server = new ServerFacade(url);
        ws = new WebsocketFacade(url, this);
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


    @Override
    public void notify(String notification) {
        System.out.println(notification);
        printPrompt();
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
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "list" -> listGames();
                case "logout" -> logout();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length >= 3) {
            var result = server.register(new RegisterRequest(params[0], params[1], params[2]));
            authToken = result.authToken();
            username = result.username();
            return String.format("Logged in as %s", result.username());
        }
        throw new ResponseException(ResponseException.Code.BadRequestError, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String login(String... params) throws ResponseException {
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

    public String createGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length > 0) {
            String gameName = String.join(" ", params);
            var createGameRequest = new CreateGameRequest(gameName);
            var result = server.createGame(createGameRequest, authToken);
            int nextKey = gameIDs.size() + 1;
            gameIDs.put(nextKey, result.gameID());
            return String.format("You created a game called %s. The id is %d", gameName, nextKey);
        }
        throw new ResponseException(ResponseException.Code.BadRequestError, "Expected: <NAME>");
    }

    public String listGames() throws ResponseException {
        assertSignedIn();
        var gameListResult = server.listGames(authToken);
        var result = new StringBuilder();
        if (!gameIDs.isEmpty()) {
            gameIDs.clear();
        }
        int count = 1;
        for (GameSummary game : gameListResult.games()) {
            result.append(String.format("%d. ", count))
                    .append(String.format("Game Name: %s, Black Team Player: %s, White Team Player: %s",
                            game.gameName(), game.blackUsername(), game.whiteUsername()))
                    .append("\n");
            gameIDs.put(count, game.gameID());
            count++;
        }
        return result.toString();
    }

    public String joinGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 2) {
            try {
                ChessGame.TeamColor color = ChessGame.TeamColor.valueOf(params[1].toUpperCase());
                int userGameID = Integer.parseInt(params[0]);
                if (userGameID < 0 || userGameID > gameIDs.size()) {
                    throw new ResponseException(ResponseException.Code.BadRequestError,
                            String.format("Game does not exist with the id %d", userGameID));
                }
                Integer serverGameID = gameIDs.get(userGameID);
                if (serverGameID == null) {
                    throw new ResponseException(ResponseException.Code.BadRequestError, "Game does not exist with the id " + userGameID);
                }
                var joinGameRequest = new JoinGameRequest(color, serverGameID);

                server.joinGame(joinGameRequest, authToken);

                ChessBoardRenderer.drawBoard(new GameData(userGameID, "", "", "game", new ChessGame()), color);
                return String.format("Successfully joined game with id %d as color %s\n", userGameID, color.name());
            } catch (IllegalArgumentException ex) {
                throw new ResponseException(ResponseException.Code.BadRequestError, "Expected: <ID> [WHITE|BLACK]");
            }
        }
        throw new ResponseException(ResponseException.Code.BadRequestError, "Expected: <ID> [WHITE|BLACK]");
    }

    public String observeGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 1) {
            try {
                int userGameID = Integer.parseInt(params[0]);
                if (userGameID < 0 || userGameID > gameIDs.size()) {
                    throw new ResponseException(ResponseException.Code.BadRequestError, "Game does not exist with the id " + userGameID);
                }

                Integer serverGameID = gameIDs.get(userGameID);
                if (serverGameID == null) {
                    throw new ResponseException(ResponseException.Code.BadRequestError, "Game does not exist with the id " + userGameID);
                }

                ChessBoardRenderer.drawBoard(new GameData(userGameID, "", "", "game", new ChessGame()),
                        ChessGame.TeamColor.WHITE);
                return String.format("Now observing game with id %d", userGameID);
            } catch (IllegalArgumentException e) {
                throw new ResponseException(ResponseException.Code.BadRequestError, "Expected: <ID>");
            }
        }
        throw new ResponseException(ResponseException.Code.BadRequestError, "Expected: <ID>");
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
            throw new ResponseException(ResponseException.Code.UnauthorizedError, "You must be signed in to perform this action.");
        }
    }
}
