package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class RookMovesCalculator extends MovesCalculator {
    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition pos) {
        var moves = new HashSet<ChessMove>();
        calculateMoves(board, pos, 1, 0, moves, true);
        calculateMoves(board, pos, -1, 0, moves, true);
        calculateMoves(board, pos, 0, 1, moves, true);
        calculateMoves(board, pos, 0, -1, moves, true);
        return moves;
    }
}
