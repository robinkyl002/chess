package websocket.commands;

public class ConnectCommand extends UserGameCommand{
    private boolean observer;

    public ConnectCommand(CommandType commandType, String authToken, Integer gameID, boolean observer) {
        super(commandType, authToken, gameID);
        this.observer = observer;
    }

    public boolean isObserver() {
        return observer;
    }
}
