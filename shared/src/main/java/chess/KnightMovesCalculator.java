package chess;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator extends MovesCalculator{
    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition pos) {
        var moves = new HashSet<ChessMove>();
        calculateMoves(board, pos, 2, 1, moves, false);
        calculateMoves(board, pos, 2, -1, moves, false);
        calculateMoves(board, pos, 1, 2, moves, false);
        calculateMoves(board, pos, 1, -2, moves, false);
        calculateMoves(board, pos, -1, -2, moves, false);
        calculateMoves(board, pos, -1, 2, moves, false);
        calculateMoves(board, pos, -2, 1, moves, false);
        calculateMoves(board, pos, -2, -1, moves, false);
        return moves;
    }
}
