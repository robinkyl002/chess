package client;

import websocket.messages.LoadGameMessage;

public interface NotificationHandler {
    void notify(String notification);
    void loadGameNotification(LoadGameMessage loadGameMessage);
}
