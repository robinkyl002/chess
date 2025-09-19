package chess;

import java.util.Collection;
import java.util.HashSet;

public class BishopMovesCalculator extends MovesCalculator {

    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition pos) {
        var moves = new HashSet<ChessMove>();
        calculateMoves(board, pos, 1, 1, moves, true);
        calculateMoves(board, pos, 1, -1, moves, true);
        calculateMoves(board, pos, -1, 1, moves, true);
        calculateMoves(board, pos, -1, -1, moves, true);
        return moves;
    }
}
