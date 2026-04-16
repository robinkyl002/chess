package websocket.commands;

import chess.ChessGame;
import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    private final ChessMove move;
    private final ChessGame.TeamColor playerColor;

    public MakeMoveCommand(String authToken, Integer gameID, ChessMove move, String username,  ChessGame.TeamColor playerColor) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = move;
        this.playerColor = playerColor;
    }

    public ChessMove getMove() {
        return move;
    }
    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
}
