package chess;

import java.util.Collection;
import java.util.HashSet;
import static chess.ChessPiece.PieceType.*;

public class PawnMovesCalculator implements MoveRules{
    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition pos) {
        var moves = new HashSet<ChessMove>();
        var currPiece = board.getPiece(pos);
        var currColor = currPiece.getTeamColor();

        forwardMoves(moves, board, pos, currColor);
        diagonalMoves(moves, board, pos, currColor);

        return moves;
    }

    private void promotionMoves(ChessPosition currPosition,
                                ChessPosition newPosition,
                                Collection<ChessMove> moves) {
        moves.add(new ChessMove(currPosition, newPosition, ROOK));
        moves.add(new ChessMove(currPosition, newPosition, BISHOP));
        moves.add(new ChessMove(currPosition, newPosition, KNIGHT));
        moves.add(new ChessMove(currPosition, newPosition, QUEEN));
    }

    private void capture(ChessBoard board, ChessPiece piece, Collection<ChessMove> moves,
                         ChessPosition pos, int vertical, int horizontal, boolean promote) {

    }

    private void diagonalMoves(Collection<ChessMove> moves, ChessBoard board,
                               ChessPosition pos, ChessGame.TeamColor teamColor) {


    }

    private void forwardMoves(Collection<ChessMove> moves, ChessBoard board,
                              ChessPosition pos, ChessGame.TeamColor teamColor) {

    }
}
