package handler.websocket;

import model.SessionData;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Session, SessionData> connections = new ConcurrentHashMap<>();

    public void add(Session session,  SessionData data) {
        connections.put(session, data);
    }

    public void remove(Session session) {
        connections.remove(session);
    }

//    public void broadcast(Session excludeSession, Notification notification) throws IOException {
//        String msg = notification.toString();
//        for (Session c : connections.values()) {
//            if (c.isOpen()) {
//                if (!c.equals(excludeSession)) {
//                    c.getRemote().sendString(msg);
//                }
//            }
//        }
//    }
}
