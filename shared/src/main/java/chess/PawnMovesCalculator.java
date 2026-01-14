package chess;

import java.util.Collection;
import java.util.HashSet;
import static chess.ChessPiece.PieceType.*;
import static chess.ChessGame.TeamColor.*;

public class PawnMovesCalculator implements MoveRules{
    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition pos) {
        var moves = new HashSet<ChessMove>();
        var currPiece = board.getPiece(pos);
        var currColor = currPiece.getTeamColor();

        int direction = (currColor.equals(WHITE)) ? 1 : -1;
        int initRow = (currColor.equals(WHITE)) ? 2 : 7;
        int promoRow = (currColor.equals(WHITE)) ? 8 : 1;

        forwardMoves(moves, board, pos, initRow, promoRow, direction);
        diagonalMoves(moves, board, pos, currPiece, promoRow, direction);

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

    private void capture(ChessBoard board, ChessPiece currPiece, Collection<ChessMove> moves,
                         ChessPosition currPos, ChessPosition newPos, int promo) {

        var newSpacePiece = board.getPiece(newPos);

        if (newSpacePiece != null && newSpacePiece.getTeamColor() != currPiece.getTeamColor()) {
            if (newPos.getRow() == promo) {
                promotionMoves(currPos, newPos, moves);
            }
            else {
                moves.add(new ChessMove(currPos, newPos, null));
            }
        }
    }

    private boolean offBoard(int row, int col) {
        return row > 8 || row < 1 || col > 8 || col < 1;
    }

    private void diagonalMoves(Collection<ChessMove> moves, ChessBoard board,
                               ChessPosition pos, ChessPiece pawn, int promo, int direction) {

        if (!offBoard(pos.getRow() + direction, pos.getColumn() + 1)) {
            var firstMove = new ChessPosition(pos.getRow() + direction, pos.getColumn() + 1);
            capture(board, pawn, moves, pos, firstMove, promo);
        }

        if (!offBoard(pos.getRow() + direction, pos.getColumn() -1)) {
            var secondMove = new ChessPosition(pos.getRow() + direction, pos.getColumn() - 1);
            capture(board, pawn, moves, pos, secondMove, promo);
        }

    }

    private void forwardMoves(Collection<ChessMove> moves, ChessBoard board,
                              ChessPosition pos, int start, int promo, int direction) {
        var singleSpacePosition = new ChessPosition(pos.getRow() + direction, pos.getColumn());
        if (offBoard(singleSpacePosition.getRow(), singleSpacePosition.getColumn())) {
            return;
        }

        if (board.getPiece(singleSpacePosition) == null) {
            if (singleSpacePosition.getRow() == promo) {
                promotionMoves(pos, singleSpacePosition, moves);
            }
            else {
                moves.add(new ChessMove(pos, singleSpacePosition, null));
                if(pos.getRow() == start) {
                    var twoSpacePosition = new ChessPosition(pos.getRow() + 2 * direction, pos.getColumn());
                    if(board.getPiece(twoSpacePosition) == null) {
                        moves.add(new ChessMove(pos, twoSpacePosition, null));
                    }
                }
            }
        }
    }
}
