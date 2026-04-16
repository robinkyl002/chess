package handler.websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import exception.ResponseException;
import io.javalin.websocket.*;
import model.GameData;
import model.SessionData;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import service.*;
import websocket.commands.*;
import websocket.messages.*;

import static chess.ChessGame.TeamColor.*;
import static exception.ResponseException.Code.*;
import static exception.ResponseException.errorMessageFromCode;
import static websocket.messages.ServerMessage.ServerMessageType.*;

public class WebsocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private final ConnectionManager connectionManager = new ConnectionManager();
    private final GameService gameService;
    private final UserService userService;

    public WebsocketHandler(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) {
        System.out.println("Websocket connection closed");
    }

    @Override
    public void handleConnect(@NotNull WsConnectContext ctx) {
        System.out.println("Websocket connection opened");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext ctx) throws ResponseException {
        try {
            var userGameCommand = new Gson().fromJson(ctx.message(), UserGameCommand.class);

            switch (userGameCommand.getCommandType()) {
                case CONNECT -> {
                    var connectCommand = new Gson().fromJson(ctx.message(), ConnectCommand.class);
                    connect(connectCommand, ctx.session);
                }
                case MAKE_MOVE -> {
                    var makeMoveCommand = new Gson().fromJson(ctx.message(), MakeMoveCommand.class);
                    makeMove(makeMoveCommand, ctx.session);
                }
                case LEAVE -> {
                    var leaveCommand = new Gson().fromJson(ctx.message(), LeaveCommand.class);
                    leave(leaveCommand, ctx.session);
                }
                case RESIGN -> {
                    var resignCommand = new Gson().fromJson(ctx.message(), ResignCommand.class);
                    resign(resignCommand, ctx.session);
                }
            }
        } catch (IllegalStateException e) {
            throw new ResponseException(ServerError, e.getMessage());
        }
    }

    private void connect(ConnectCommand connectCommand, Session session) throws ResponseException {
        if (!userService.validAuth(connectCommand.getAuthToken())) {
            connectionManager.personalMessage(session, new ErrorMessage(ERROR, "Error: invalid auth token"));
            throw new ResponseException(UnauthorizedError, errorMessageFromCode(UnauthorizedError));
        }

        String username = userService.getUsername(connectCommand.getAuthToken());
        var currGame = gameService.retrieveGame(connectCommand.getGameID());

        if (currGame == null) {
            connectionManager.personalMessage(session, new ErrorMessage(ERROR, "Error: This game does not exist"));
            throw new ResponseException(BadRequestError, errorMessageFromCode(BadRequestError));
        }
        connectionManager.add(session, new SessionData(connectCommand.getAuthToken(), connectCommand.getGameID(),
                connectCommand.isObserver()));

        var color = (username.equals(currGame.whiteUsername())) ? WHITE : BLACK;
        var message = (connectCommand.isObserver()) ?
                String.format("%s is now observing the game", username) :
                String.format("%s is now playing the game as %s", username, color.toString().toLowerCase());

        var notification = new Notification(NOTIFICATION, message);

        connectionManager.broadcast(session, notification, connectCommand.getGameID());

        LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, currGame);
        connectionManager.personalMessage(session, loadGameMessage);

    }

    private void makeMove(MakeMoveCommand makeMoveCommand, Session session) throws ResponseException {
        if (!userService.validAuth(makeMoveCommand.getAuthToken())) {
            connectionManager.personalMessage(session, new ErrorMessage(ERROR, "Error: invalid auth token"));
            throw new ResponseException(UnauthorizedError, errorMessageFromCode(UnauthorizedError));
        }

        var currGame = gameService.retrieveGame(makeMoveCommand.getGameID());
        if (currGame.completed()) {
            connectionManager.personalMessage(session, new ErrorMessage(ERROR, "Error: This game is already completed"));
            throw new ResponseException(UnauthorizedError, errorMessageFromCode(UnauthorizedError));
        }
        var startPos = makeMoveCommand.getMove().getStartPosition();

        ChessGame.TeamColor color;
        String username = userService.getUsername(makeMoveCommand.getAuthToken());
        var game = gameService.retrieveGame(makeMoveCommand.getGameID());
        if (username.equals(game.whiteUsername())) {
            color = WHITE;
        }
        else if (username.equals(game.blackUsername())) {
            color = BLACK;
        }
        else {
            connectionManager.personalMessage(session, new ErrorMessage(ERROR, "Error: Cannot move pieces as an observer"));
            throw new ResponseException(BadRequestError, errorMessageFromCode(BadRequestError));
        }

        if (currGame.game().isInCheckmate(color) || currGame.game().isInStalemate(color)) {
            gameService.updateGame(currGame, true);
            connectionManager.personalMessage(session, new Notification(NOTIFICATION, "Error: This game is over"));
            return;
        }

        if (currGame.game().getBoard().getPiece(startPos).getTeamColor() != color) {
            var notification = new ErrorMessage(ERROR, "You cannot move the other team's pieces");
            connectionManager.personalMessage(session, notification);
            return;
        }

        try {
            currGame.game().makeMove(makeMoveCommand.getMove());

            gameService.updateGame(currGame, false);
        } catch (InvalidMoveException e) {
            connectionManager.personalMessage(session, new ErrorMessage(ERROR, "This move is not valid. Please try another."));
            return;
        }

        var notification =  new Notification(NOTIFICATION, String.format("%s moved from %s to %s", username,
                makeMoveCommand.getMove().getStartPosition().toString(), makeMoveCommand.getMove().getEndPosition().toString()));

        var updatedGame = gameService.retrieveGame(makeMoveCommand.getGameID());
        ChessGame.TeamColor nextTeam = updatedGame.game().getTeamTurn();
        if (updatedGame.game().isInCheckmate(nextTeam)) {
            gameService.updateGame(updatedGame, true);
            connectionManager.broadcast(null, new Notification(NOTIFICATION, nextTeam + " is in checkmate"), updatedGame.gameID());

        } else if (updatedGame.game().isInStalemate(nextTeam)) {
            gameService.updateGame(updatedGame, true);
            connectionManager.broadcast(null, new Notification(NOTIFICATION, "The game is in stalemate"), updatedGame.gameID());
        } else if (updatedGame.game().isInCheck(nextTeam)) {
            connectionManager.broadcast(null, new Notification(NOTIFICATION, nextTeam + " is in check"), updatedGame.gameID());
        }

        connectionManager.broadcast(session, notification, makeMoveCommand.getGameID());

        var loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, currGame);
        connectionManager.broadcast(session, loadGameMessage,  makeMoveCommand.getGameID());
        connectionManager.personalMessage(session, loadGameMessage);
    }

    private void leave(LeaveCommand leaveCommand, Session session) throws ResponseException {
        if (!userService.validAuth(leaveCommand.getAuthToken())) {
            throw new ResponseException(UnauthorizedError, errorMessageFromCode(UnauthorizedError));
        }

        String username = userService.getUsername(leaveCommand.getAuthToken());
        var game = gameService.retrieveGame(leaveCommand.getGameID());
        if (username.equals(game.whiteUsername())) {
            gameService.removePlayer(leaveCommand.getGameID(), WHITE);
        }
        else if (username.equals(game.blackUsername())) {
            gameService.removePlayer(leaveCommand.getGameID(), BLACK);
        }


        var message = String.format("%s left the game", username);

        var notification = new Notification(NOTIFICATION, message);

        connectionManager.broadcast(session, notification, leaveCommand.getGameID());
        connectionManager.remove(session);
    }

    private void resign(ResignCommand resignCommand, Session session) throws ResponseException {
        if (!userService.validAuth(resignCommand.getAuthToken())) {
            throw new ResponseException(UnauthorizedError, errorMessageFromCode(UnauthorizedError));
        }
        var game = gameService.retrieveGame(resignCommand.getGameID());

        if (game.completed()) {
            connectionManager.personalMessage(session, new ErrorMessage(ERROR, "Error: This game is already completed"));
            return;        }

        String username = userService.getUsername(resignCommand.getAuthToken());
        if (username.equals(game.whiteUsername())) {
            gameService.removePlayer(resignCommand.getGameID(), WHITE);
        }
        else if (username.equals(game.blackUsername())) {
            gameService.removePlayer(resignCommand.getGameID(), BLACK);
        }
        else {
            connectionManager.personalMessage(session, new ErrorMessage(ERROR, "You cannot resign the game as an observer"));
            throw new ResponseException(BadRequestError, errorMessageFromCode(BadRequestError));
        }

        gameService.updateGame(game, true);

        var notification = new Notification(NOTIFICATION, String.format("%s resigned", username));
        connectionManager.broadcast(session, notification, resignCommand.getGameID());

        var personalNotification = new Notification(NOTIFICATION, "You have successfully resigned");
        connectionManager.personalMessage(session, personalNotification);
    }

    private ChessGame.TeamColor getPlayerColor(GameData game, String username) {
        if (username.equals(game.whiteUsername())) {
            return WHITE;
        }
        else if (username.equals(game.blackUsername())) {
            return BLACK;
        }
        else {
            return null;
        }
    }
}
