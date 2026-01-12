package chess;

import java.util.Collection;
import java.util.HashSet;

public class QueenMoves extends GeneralMovesCalculator{
    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition pos) {
        var moves = new HashSet<ChessMove>();
        calculateMoves(pos, 1, 0, moves, board, true);
        calculateMoves(pos, -1, 0, moves, board, true);
        calculateMoves(pos, 0, 1, moves, board, true);
        calculateMoves(pos, 0, -1, moves, board, true);
        calculateMoves(pos, 1, 1, moves, board, true);
        calculateMoves(pos, 1, -1, moves, board, true);
        calculateMoves(pos, -1, 1, moves, board, true);
        calculateMoves(pos, -1, -1, moves, board, true);
        return moves;
    }
}
