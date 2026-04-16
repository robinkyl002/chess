package websocket.commands;

public class ResignCommand extends UserGameCommand{
    private final String username;

    public ResignCommand(String authToken, Integer gameID, String username) {
        super(CommandType.RESIGN, authToken, gameID);
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
}
