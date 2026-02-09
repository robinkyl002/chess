package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public abstract class GeneralMovesCalculator implements MoveRules{
    protected void calculateMoves(ChessPosition pos, int rowIncrement, int colIncrement,
                                  Collection<ChessMove> moves, ChessBoard board,
                                  boolean repeatMove) {
        if (pos.getRow() + rowIncrement > 8 || pos.getRow() + rowIncrement < 1 ||
                pos.getColumn() + colIncrement > 8 || pos.getColumn() + colIncrement < 1) {
            return;
        }

        ChessPosition newPosition = new ChessPosition(pos.getRow() + rowIncrement,
                pos.getColumn() + colIncrement);
        if (board.getPiece(newPosition) == null) {
            moves.add(new ChessMove(pos, newPosition, null));
            if (repeatMove) {
                calculateMoves(pos, rowIncrement + Integer.signum(rowIncrement),
                        colIncrement + Integer.signum(colIncrement), moves, board, repeatMove);
            }
        }
        else {
            if (board.getPiece(newPosition).getTeamColor() == board.getPiece(pos).getTeamColor()) {
                return;
            }
            else {
                moves.add(new ChessMove(pos, newPosition,null));
                return;
            }
        }
    }


    public abstract Collection<ChessMove> moves(ChessBoard board, ChessPosition pos);
}
