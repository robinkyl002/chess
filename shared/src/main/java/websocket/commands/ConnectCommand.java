package websocket.commands;

public class ConnectCommand extends UserGameCommand{
    private final boolean observer;

    public ConnectCommand(CommandType commandType, String authToken, Integer gameID, boolean observer, String username) {
        super(commandType, authToken, gameID);
        this.observer = observer;
    }

    public boolean isObserver() {
        return observer;
    }
}
