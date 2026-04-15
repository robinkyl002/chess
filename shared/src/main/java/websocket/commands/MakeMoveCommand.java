package websocket.commands;

import chess.ChessGame;
import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    private final ChessMove move;
    private final String username;
    private final ChessGame.TeamColor playerColor;

    public MakeMoveCommand(String authToken, Integer gameID, ChessMove move, String username,  ChessGame.TeamColor playerColor) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = move;
        this.username = username;
        this.playerColor = playerColor;
    }

    public ChessMove getMove() {
        return move;
    }
    public String getUsername() {
        return username;
    }
    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
}
