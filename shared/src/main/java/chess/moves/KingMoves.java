package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KingMoves extends GeneralMovesCalculator{
    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition pos) {
        var moves = new HashSet<ChessMove>();
        calculateMoves(pos, 1, 0, moves, board, false);
        calculateMoves(pos, -1, 0, moves, board, false);
        calculateMoves(pos, 0, 1, moves, board, false);
        calculateMoves(pos, 0, -1, moves, board, false);
        calculateMoves(pos, 1, 1, moves, board, false);
        calculateMoves(pos, 1, -1, moves, board, false);
        calculateMoves(pos, -1, 1, moves, board, false);
        calculateMoves(pos, -1, -1, moves, board, false);
        return moves;
    }
}
