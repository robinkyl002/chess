package websocket.commands;

import chess.ChessGame;

public class LeaveCommand extends UserGameCommand {
    ChessGame.TeamColor playerColor;

    public LeaveCommand(CommandType commandType, String authToken, Integer gameID, ChessGame.TeamColor playerColor) {
        super(commandType, authToken, gameID);
        this.playerColor = playerColor;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
}
