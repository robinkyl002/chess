package handler.websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import exception.ResponseException;
import io.javalin.websocket.*;
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
    private ConnectionManager connectionManager = new ConnectionManager();
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
                    resign();
                }
            }
        } catch (IllegalStateException e) {
            throw new ResponseException(ServerError, e.getMessage());
        }
    }

    private void connect(ConnectCommand connectCommand, Session session) throws ResponseException {
        if (!userService.validAuth(connectCommand.getAuthToken())) {
            throw new ResponseException(UnauthorizedError, errorMessageFromCode(UnauthorizedError));
        }

        var currGame = gameService.retrieveGame(connectCommand.getGameID());

        connectionManager.add(session, new SessionData(connectCommand.getAuthToken(), connectCommand.getGameID(),
                connectCommand.isObserver()));

        var color = (connectCommand.getUsername().equals(currGame.whiteUsername())) ? WHITE : BLACK;
        var message = (connectCommand.isObserver()) ?
                String.format("%s is now observing the game", connectCommand.getUsername()) :
                String.format("%s is now playing the game as %s", connectCommand.getUsername(), color.toString().toLowerCase());

        var notification = new Notification(NOTIFICATION, message);

        connectionManager.broadcast(session, notification, connectCommand.getGameID());

        LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, currGame);
        connectionManager.personalMessage(session, loadGameMessage);

    }

    private void makeMove(MakeMoveCommand makeMoveCommand, Session session) throws ResponseException {
        if (!userService.validAuth(makeMoveCommand.getAuthToken())) {
            throw new ResponseException(UnauthorizedError, errorMessageFromCode(UnauthorizedError));
        }

        var currGame = gameService.retrieveGame(makeMoveCommand.getGameID());
        var startPos = makeMoveCommand.getMove().getStartPosition();

        if (currGame.game().getBoard().getPiece(startPos).getTeamColor() != makeMoveCommand.getPlayerColor()) {
            var notification = new ErrorMessage(ERROR, "You cannot move the other team's pieces");
            connectionManager.personalMessage(session, notification);
            return;
        }

        try {
            currGame.game().makeMove(makeMoveCommand.getMove());

            gameService.updateGame(currGame);
        } catch (InvalidMoveException e) {
            connectionManager.personalMessage(session, new ErrorMessage(ERROR, "This move is not valid. Please try another."));
            return;
        }

        var notification =  new Notification(NOTIFICATION, String.format("%s moved from %s to %s", makeMoveCommand.getUsername(),
                makeMoveCommand.getMove().getStartPosition().toString(), makeMoveCommand.getMove().getEndPosition().toString()));

        connectionManager.broadcast(session, notification, makeMoveCommand.getGameID());

        var loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, currGame);
        connectionManager.broadcast(session, loadGameMessage,  makeMoveCommand.getGameID());
        connectionManager.personalMessage(session, loadGameMessage);
    }

    private void leave(LeaveCommand leaveCommand, Session session) throws ResponseException {
        if (!userService.validAuth(leaveCommand.getAuthToken())) {
            throw new ResponseException(UnauthorizedError, errorMessageFromCode(UnauthorizedError));
        }

        var message = String.format("%s left the game", leaveCommand.getUsername());

        var notification = new Notification(NOTIFICATION, message);

        var personalMessage = new Notification(NOTIFICATION, "You have left the game");

        connectionManager.broadcast(session, notification, leaveCommand.getGameID());
        connectionManager.personalMessage(session, personalMessage);
        connectionManager.remove(session);
    }

    private void resign() {

    }
}
