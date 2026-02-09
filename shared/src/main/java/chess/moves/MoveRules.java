package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public interface MoveRules {
    Collection<ChessMove> moves (ChessBoard board, ChessPosition pos);
}
