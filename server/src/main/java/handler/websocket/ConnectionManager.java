package handler.websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import model.SessionData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;
import static exception.ResponseException.Code.ServerError;
import static exception.ResponseException.errorMessageFromCode;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Session, SessionData> connections = new ConcurrentHashMap<>();

    public void add(Session session,  SessionData data) {
        connections.put(session, data);
    }

    public void remove(Session session) {
        connections.remove(session);
    }

    public void broadcast(Session excludeSession, ServerMessage message, int gameID) throws ResponseException {

        try {
            String msg = new Gson().toJson(message);
            for (var c : connections.entrySet()) {
                var session = c.getKey();
                var data = c.getValue();

                if (data.gameID() == gameID) {
                    if (session.isOpen()) {
                        if (!session.equals(excludeSession)) {
                            session.getRemote().sendString(msg);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(ServerError, errorMessageFromCode(ServerError));
        }
    }

    public void personalMessage(Session session, ServerMessage message) throws ResponseException {
        try {
            String msg = new Gson().toJson(message);

            if (session.isOpen()) {
                session.getRemote().sendString(msg);
            }
        } catch (Exception e) {
            throw new ResponseException(ServerError, errorMessageFromCode(ServerError));
        }
    }
}
