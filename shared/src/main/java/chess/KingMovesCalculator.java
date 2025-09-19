package chess;

import java.util.Collection;
import java.util.HashSet;

public class KingMovesCalculator extends MovesCalculator{
    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition pos) {
        var moves = new HashSet<ChessMove>();
        calculateMoves(board, pos, 1, 1, moves, false);
        calculateMoves(board, pos, 1, -1, moves, false);
        calculateMoves(board, pos, -1, 1, moves, false);
        calculateMoves(board, pos, -1, -1, moves, false);
        calculateMoves(board, pos, 1, 0, moves, false);
        calculateMoves(board, pos, -1, 0, moves, false);
        calculateMoves(board, pos, 0, 1, moves, false);
        calculateMoves(board, pos, 0, -1, moves, false);
        return moves;
    }
}
