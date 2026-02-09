package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KnightMoves extends GeneralMovesCalculator{
    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition pos) {
        var moves = new HashSet<ChessMove>();
        calculateMoves(pos, 2, 1, moves, board, false);
        calculateMoves(pos, 2, -1, moves, board, false);
        calculateMoves(pos, -2, 1, moves, board, false);
        calculateMoves(pos, -2, -1, moves, board, false);
        calculateMoves(pos, 1, 2, moves, board, false);
        calculateMoves(pos, 1, -2, moves, board, false);
        calculateMoves(pos, -1, 2, moves, board, false);
        calculateMoves(pos, -1, -2, moves, board, false);
        return moves;
    }
}
