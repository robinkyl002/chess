package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class BishopMoves extends GeneralMovesCalculator{
    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition pos) {
        var moves = new HashSet<ChessMove>();
        calculateMoves(pos, 1, 1, moves, board, true);

        return moves;
    }
}
