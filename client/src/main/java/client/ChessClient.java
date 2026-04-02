package client;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ResponseException;
import model.GameData;
import server.ServerFacade;
import service.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient {
    private String username = null;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private String authToken;
    private HashMap<Integer, Integer> gameIDs = new HashMap<>();
    private final static int SQUARES_ON_SIDE = 8;
    private final static String[] HORIZONTAL_HEADERS = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private final static String[] VERTICAL_HEADERS = {"1", "2", "3", "4", "5", "6", "7", "8"};

    private enum SpaceColor {
        WHITE,
        BLACK,
    }

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
                case "join" -> joinGame(params);
                case "observe" -> null;
                case "list" -> listGames();
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

    public String joinGame(String ...params) throws ResponseException {
        assertSignedIn();
        if (params.length == 2) {
            ChessGame.TeamColor color = ChessGame.TeamColor.valueOf(params[1].toUpperCase());
            int userGameID = Integer.parseInt(params[0]);
            if (userGameID < 0 || userGameID > gameIDs.size()) {
                throw new ResponseException(ResponseException.Code.BadRequestError, String.format("Game does not exist with the id %d", userGameID));
            }
            Integer serverGameID =  gameIDs.get(userGameID);
            if (serverGameID == null) {
                throw new ResponseException(ResponseException.Code.BadRequestError, "Game does not exist with the id " + userGameID);
            }
            var joinGameRequest = new JoinGameRequest(color, serverGameID);

            server.joinGame(joinGameRequest, authToken);

            return drawBoard(new GameData(userGameID, "", "", "game", new ChessGame()), color);
        }
        throw new ResponseException(ResponseException.Code.BadRequestError, "Expected: <ID> [WHITE|BLACK]");
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

    public static String drawBoard (GameData gameData, ChessGame.TeamColor color) throws ResponseException {
        var gameBoard = gameData.game().getBoard();

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        int increment = (color == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int startRow = (color == ChessGame.TeamColor.WHITE) ? 0 : SQUARES_ON_SIDE - 1;
        int endRow = (color == ChessGame.TeamColor.WHITE) ? SQUARES_ON_SIDE : -1;
        drawHorizontalBorder(out, increment);
        int spaceNumber = 1;

        for (int i = startRow; i != endRow; i += increment) {
            setBorderColors(out);
            out.print(" " + VERTICAL_HEADERS[i] + " ");

            int startColumn = (color == ChessGame.TeamColor.WHITE) ? 0 : SQUARES_ON_SIDE - 1;
            int endColumn = (color == ChessGame.TeamColor.WHITE) ? SQUARES_ON_SIDE : -1;
            for (int j = startColumn; j != endColumn; j += increment) {
                var piece = gameBoard.getPiece(new ChessPosition(i + 1, j + 1));

                if (spaceNumber % 2 == 0) {
                    out.print(SET_BG_COLOR_BLACK);
                } else {
                    out.print(SET_BG_COLOR_WHITE);
                }
                if (piece == null) {
                    out.print(EMPTY);
                } else {
                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        out.print(SET_TEXT_COLOR_RED);
                    } else {
                        out.print(SET_TEXT_COLOR_BLUE);
                    }

                    out.print(getPieceSymbol(piece));
                }
                spaceNumber++;
            }
            spaceNumber++;
            setBorderColors(out);
            out.print(" " + VERTICAL_HEADERS[i] + " ");
            out.print(RESET);
            out.println();
        }
        drawHorizontalBorder(out, increment);
        return "";
    }

    private static void drawHorizontalBorder(PrintStream out, int increment) {
        setBorderColors(out);
        out.print(EMPTY);

        if (increment == 1) {
            for (int i = 0; i < SQUARES_ON_SIDE; i += increment) {
                out.print(" " + HORIZONTAL_HEADERS[i] + " ");
            }
        } else {
            for (int i = SQUARES_ON_SIDE - 1; i >= 0; i += increment) {
                out.print(" " + HORIZONTAL_HEADERS[i] + " ");
            }
        }
        out.print(EMPTY);
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        out.println();
    }

    private static void setBorderColors(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static String getPieceSymbol(ChessPiece piece) {
        return switch (piece.getPieceType()) {
            case KING -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KING : BLACK_KING;
            case QUEEN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_QUEEN : BLACK_QUEEN;
            case ROOK -> piece.getTeamColor()  == ChessGame.TeamColor.WHITE ? WHITE_ROOK : BLACK_ROOK;
            case KNIGHT -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KNIGHT : BLACK_KNIGHT;
            case BISHOP -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_BISHOP : BLACK_BISHOP;
            case PAWN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_PAWN : BLACK_PAWN;
        };
    }
}
