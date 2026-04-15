package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import exception.ResponseException;
import model.GameData;
import server.ServerFacade;
import service.*;
import ui.ChessBoardRenderer;
import websocket.messages.LoadGameMessage;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

import static exception.ResponseException.Code.*;
import static ui.EscapeSequences.*;

public class ChessClient implements NotificationHandler {
    private String username = null;
    private final ServerFacade server;
    private final WebsocketFacade ws;
    private String authToken;
    private State state;
    private ChessGame.TeamColor playerColor;
    private GameData currGameState;
    private final HashMap<Integer, Integer> gameIDs = new HashMap<>();

    public ChessClient(String url) throws ResponseException {
        server = new ServerFacade(url);
        ws = new WebsocketFacade(url, this);
        authToken = "";
        state = State.IN_LOBBY;
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

    @Override
    public void loadGameNotification(LoadGameMessage loadGameMessage) {
        ChessGame.TeamColor loadColor = (playerColor != null ? playerColor : ChessGame.TeamColor.WHITE);
        ChessBoardRenderer.drawBoard(loadGameMessage.getGame(), loadColor, null);
        currGameState = loadGameMessage.getGame();
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
                case "leave" -> leaveGame();
                case "redraw" -> redraw();
                case "resign" -> resign();
                case "highlight" -> highlight(params);
                case "move" -> null;
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
        throw new ResponseException(BadRequestError, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String login(String... params) throws ResponseException {
        if (params.length >= 2) {
            var result = server.login(new LoginRequest(params[0], params[1]));
            authToken = result.authToken();
            username = result.username();
            return String.format("Logged in as %s", result.username());
        }
        throw new ResponseException(BadRequestError, "Expected: <USERNAME> <PASSWORD>");
    }

    public String logout() throws ResponseException {
        assertSignedIn();
        server.logout(new LogoutRequest(authToken));
        authToken = null;
        state = State.IN_LOBBY;
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
        throw new ResponseException(BadRequestError, "Expected: <NAME>");
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
                    throw new ResponseException(BadRequestError,
                            String.format("Game does not exist with the id %d", userGameID));
                }
                Integer serverGameID = gameIDs.get(userGameID);
                if (serverGameID == null) {
                    throw new ResponseException(BadRequestError, "Game does not exist with the id " + userGameID);
                }
                var joinGameRequest = new JoinGameRequest(color, serverGameID);

                server.joinGame(joinGameRequest, authToken);
                playerColor = color;

                ws.connect(authToken, serverGameID, false, username);

                state = State.PLAYING;
                return String.format("Successfully joined game with id %d as color %s\n", userGameID, color.name());
            } catch (IllegalArgumentException ex) {
                throw new ResponseException(BadRequestError, "Expected: <ID> [WHITE|BLACK]");
            }
        }
        throw new ResponseException(BadRequestError, "Expected: <ID> [WHITE|BLACK]");
    }

    public String observeGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 1) {
            try {
                int userGameID = Integer.parseInt(params[0]);
                if (userGameID < 0 || userGameID > gameIDs.size()) {
                    throw new ResponseException(BadRequestError, "Game does not exist with the id " + userGameID);
                }

                Integer serverGameID = gameIDs.get(userGameID);
                if (serverGameID == null) {
                    throw new ResponseException(BadRequestError, "Game does not exist with the id " + userGameID);
                }

                ws.connect(authToken, serverGameID, true, username);
                state = State.OBSERVING;
                return String.format("Now observing game with id %d", userGameID);
            } catch (IllegalArgumentException e) {
                throw new ResponseException(BadRequestError, "Expected: <ID>");
            }
        }
        throw new ResponseException(BadRequestError, "Expected: <ID>");
    }

    public String leaveGame() throws ResponseException {
        assertSignedIn();
        currentlyPlayingOrObserving();

        ws.leaveGame(authToken, currGameState.gameID(), username);
        state = State.IN_LOBBY;

        return "";
    }

    public String redraw() throws ResponseException {
        assertSignedIn();
        currentlyPlayingOrObserving();

        if (currGameState == null) {
            throw new  ResponseException(BadRequestError, "Could not load current game state. Please try joining the game again");
        }
        ChessBoardRenderer.drawBoard(currGameState, playerColor, null);

        return "";
    }

    public String resign() throws ResponseException {
        assertSignedIn();
        currentlyPlaying();


        return "";
    }

    public String highlight(String ... params) throws ResponseException {
        assertSignedIn();
        currentlyPlayingOrObserving();
        if (params.length == 1 && params[0].length() == 2) {
            var position = getChessPosition(params);
            var selectedPiece = currGameState.game().getBoard().getPiece(position);
            if (selectedPiece == null) {
                throw new ResponseException(BadRequestError,
                        "This position does not have a piece. Please select a valid position with a chess piece on it");
            }

            var validMoves = currGameState.game().validMoves(position);

            ChessBoardRenderer.drawBoard(currGameState, playerColor, validMoves);

            if (validMoves.isEmpty()) {
                return "There are no possible moves for this piece";
            }
            return "";
        }
        throw new ResponseException(BadRequestError, "Expected: <POSITION>. Position should be in range a1-h8.");
    }

    private ChessPosition getChessPosition(String ... params) throws ResponseException {
        var input = params[0];
        int row = Integer.parseInt(input.substring(1));
        var colCharacter = input.charAt(0);

        int col = colCharacter - 'a' + 1;
        if (row < 1 || row > 8) {
            throw new ResponseException(BadRequestError, "Invalid row " + row);
        }
        if (col < 1 || col > 8) {
            throw new ResponseException(BadRequestError, "Invalid column " + colCharacter);
        }

        return new ChessPosition(row, col);
    }

    public String help() {
        if (state == State.IN_LOBBY) {
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
        } else if (state == State.PLAYING) {
            return """
                 - redraw - to redraw the chess board
                 - highlight <PIECE POSITION> - possible moves for a specified piece
                 - move <CURRENT POSITION> <NEW POSITION> - to make a move
                 - leave - to return to the lobby
                 - resign - to concede defeat to your opponent
                 - help - with possible commands
                 """;
        } else {
            return """
                 - redraw - to redraw the chess board
                 - highlight <PIECE POSITION> - possible moves for a specified piece
                 - leave - to return to the lobby
                 - help - with possible commands
                 """;
        }

    }

    private void currentlyPlaying() throws ResponseException {
        if (state != State.PLAYING) {
            throw new ResponseException(UnauthorizedError, "You must be playing to use this command");
        }
    }

    private void currentlyPlayingOrObserving() throws ResponseException {
        if (state == State.IN_LOBBY) {
            throw new ResponseException(UnauthorizedError, "You must be playing or observing to use this command");
        }
    }

    private void assertSignedIn() throws ResponseException {
        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException(UnauthorizedError, "You must be signed in to perform this action.");
        }
    }
}
