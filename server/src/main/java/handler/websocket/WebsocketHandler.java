package handler.websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import io.javalin.websocket.*;
import model.SessionData;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import service.*;
import websocket.commands.ConnectCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import static exception.ResponseException.Code.*;
import static exception.ResponseException.errorMessageFromCode;

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
    public void handleMessage(@NotNull WsMessageContext ctx) throws Exception {
        var userGameCommand = new Gson().fromJson(ctx.message(), UserGameCommand.class);

        switch (userGameCommand.getCommandType()) {
            case CONNECT -> {
                var connectCommand = new Gson().fromJson(ctx.message(), ConnectCommand.class);
                connect(connectCommand, ctx.session);
            }
            case MAKE_MOVE -> {
                makeMove();
            }
            case LEAVE ->  {
                leave();
            }
            case RESIGN -> {
                resign();
            }
        }
    }

    private void connect(ConnectCommand connectCommand, Session session) throws ResponseException {
        if (!userService.validAuth(connectCommand.getAuthToken())) {
            throw new ResponseException(UnauthorizedError, errorMessageFromCode(UnauthorizedError));
        }

        var currGame = gameService.retrieveGame(connectCommand.getGameID());

        connectionManager.add(session, new SessionData(connectCommand.getAuthToken(), connectCommand.getGameID(),
                connectCommand.isObserver()));
        var message = (connectCommand.isObserver()) ?
                String.format("%s is now observing the game", connectCommand.getUsername()) :
                String.format("%s is now playing the game", connectCommand.getUsername());

        var notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);

        connectionManager.broadcast(session, notification, connectCommand.getGameID());

        LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, currGame);
        connectionManager.personalMessage(session, loadGameMessage);

    }

    private void makeMove() {

    }

    private void leave() {

    }

    private void resign() {

    }
}
