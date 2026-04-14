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

    public void joinGameAsPlayer(String playerName) throws ResponseException {
        try {
            session.getBasicRemote().sendText(playerName);
        } catch (IOException ex) {
            throw new ResponseException(ServerError, errorMessageFromCode(ServerError));
        }
    }
    public void joinGameAsObserver(String playerName) throws ResponseException {
        try {
            session.getBasicRemote().sendText(playerName);
        } catch (IOException ex) {
            throw new ResponseException(ServerError, errorMessageFromCode(ServerError));
        }
    }

    public void connect(String token, int gameID)  throws ResponseException {
        try {
            var userGameCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, token, gameID);
            session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (IOException ex) {
            throw new ResponseException(ServerError, errorMessageFromCode(ServerError));
        }
    }

}
