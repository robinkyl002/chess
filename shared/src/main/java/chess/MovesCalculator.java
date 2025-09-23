package chess;

import java.util.Collection;

public abstract class MovesCalculator implements MovementRule {
    protected void calculateMoves(ChessBoard board, ChessPosition pos, int rowIncrement, int colIncrement, Collection<ChessMove> moves, boolean allowDistance) {
        if (pos.getRow() + rowIncrement > 8 || pos.getRow() + rowIncrement < 1 ||
                pos.getColumn() + colIncrement > 8 || pos.getColumn() + colIncrement < 1) {
            return;
        }
        ChessPosition newPos = new ChessPosition(pos.getRow() + rowIncrement, pos.getColumn() + colIncrement);
        if (board.getPiece(newPos) == null) {
            moves.add(new ChessMove(pos, newPos, null));
            if (allowDistance) {
                calculateMoves(board, pos, rowIncrement + Integer.signum(rowIncrement),
                        colIncrement + Integer.signum(colIncrement), moves, allowDistance);
            }
        }
        else {
            if (board.getPiece(pos).getTeamColor().equals(board.getPiece(newPos).getTeamColor())) {
                return;
            }
            else {
                moves.add(new ChessMove(pos, newPos, null));
                return;
            }
        }
    }
    public abstract Collection<ChessMove> moves(ChessBoard board, ChessPosition pos);
}
