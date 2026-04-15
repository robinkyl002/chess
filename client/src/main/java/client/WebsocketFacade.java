package client;

import com.google.gson.Gson;
import exception.ResponseException;
import jakarta.websocket.*;
import websocket.commands.*;
import websocket.messages.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static exception.ResponseException.Code.*;
import static exception.ResponseException.errorMessageFromCode;

public class WebsocketFacade extends Endpoint {
    private final Session session;
    private final NotificationHandler notificationHandler;

    public WebsocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);

                    switch (serverMessage.getServerMessageType()) {
                        case LOAD_GAME -> {
                            LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
                            notificationHandler.loadGameNotification(loadGameMessage);
                        }
                        case ERROR -> {
                            ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                            notificationHandler.notify(errorMessage.getErrorMessage());
                        }
                        case NOTIFICATION ->  {
                            Notification notification = new Gson().fromJson(message, Notification.class);
                            notificationHandler.notify(notification.getMessage());
                        }
                    }
                }
            });

        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(BadRequestError, errorMessageFromCode(BadRequestError));
        }
    }
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect(String token, int gameID, boolean observer, String username)  throws ResponseException {
        try {
            var connectCommand = new ConnectCommand(UserGameCommand.CommandType.CONNECT, token, gameID, observer, username);
            session.getBasicRemote().sendText(new Gson().toJson(connectCommand));
        } catch (IOException ex) {
            throw new ResponseException(ServerError, errorMessageFromCode(ServerError));
        }
    }

    public void leaveGame(String token, int gameID, String username) throws ResponseException {
        try {
            var leaveCommand = new LeaveCommand(UserGameCommand.CommandType.LEAVE, token, gameID, username);
            session.getBasicRemote().sendText(new Gson().toJson(leaveCommand));
        } catch (IOException ex) {
            throw new ResponseException(ServerError, errorMessageFromCode(ServerError));
        }
    }

}
