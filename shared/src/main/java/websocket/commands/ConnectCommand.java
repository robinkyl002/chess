package websocket.commands;

public class ConnectCommand extends UserGameCommand{
    private final boolean observer;
    private final String username;

    public ConnectCommand(CommandType commandType, String authToken, Integer gameID, boolean observer, String username) {
        super(commandType, authToken, gameID);
        this.observer = observer;
        this.username = username;
    }

    public boolean isObserver() {
        return observer;
    }
    public String getUsername() {
        return username;
    }
}
