package websocket.messages;

public class LoadGameMessage extends ServerMessage{
    private int id;

    public LoadGameMessage(ServerMessageType type, int id) {
        super(type);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
