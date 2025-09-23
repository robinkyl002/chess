package chess;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator implements MovementRule{
    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition pos) {
        var moves = new HashSet<ChessMove>();

        ChessPiece currPiece = board.getPiece(pos);
        if (currPiece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
            if (pos.getRow() + 1 < 8) {
                forwardMoves(ChessGame.TeamColor.WHITE, board, moves, pos);

                diagonalMoves(ChessGame.TeamColor.WHITE, board, currPiece, moves, pos, false);

            } else if (pos.getRow() + 1 == 8){
                ChessPosition newPos = new ChessPosition(pos.getRow() + 1, pos.getColumn());
                if (board.getPiece(newPos) == null) {
                    addPromotionMoves(pos, newPos, moves);
                }

                diagonalMoves(ChessGame.TeamColor.WHITE, board, currPiece, moves, pos, true);
            }
        }
        else {
            if (pos.getRow() - 1 > 1) {
                forwardMoves(ChessGame.TeamColor.BLACK, board, moves, pos);

                diagonalMoves(ChessGame.TeamColor.BLACK, board, currPiece, moves, pos, false);
            } else if (pos.getRow() - 1 == 1){
                ChessPosition newPos = new ChessPosition(pos.getRow()-1, pos.getColumn());
                if (board.getPiece(newPos) == null) {
                    addPromotionMoves(pos, newPos, moves);
                }

                diagonalMoves(ChessGame.TeamColor.BLACK, board, currPiece, moves, pos, true);
            }
        }
        return moves;
    }

    private void addPromotionMoves(ChessPosition pos, ChessPosition newPos, Collection<ChessMove> moves) {
        moves.add(new ChessMove(pos,newPos, ChessPiece.PieceType.KNIGHT));
        moves.add(new ChessMove(pos,newPos, ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(pos,newPos, ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(pos,newPos, ChessPiece.PieceType.ROOK));
    }

    private void capturePiece(ChessBoard board, ChessPiece currPiece, Collection<ChessMove> moves,
                              ChessPosition pos, int horizontal, int vertical, boolean promotion) {
        ChessPosition diagonalPosition = new ChessPosition(pos.getRow() + Integer.signum(horizontal), pos.getColumn()+ Integer.signum(vertical));
        if (board.getPiece(diagonalPosition) != null) {
            if (!currPiece.getTeamColor().equals(board.getPiece(diagonalPosition).getTeamColor())) {
                if (promotion) {
                    addPromotionMoves(pos, diagonalPosition, moves);
                }
                else {
                    moves.add(new ChessMove(pos, diagonalPosition, null));
                }
            }
        }
    }

    private void diagonalMoves(ChessGame.TeamColor color, ChessBoard board, ChessPiece currPiece,
                               Collection<ChessMove> moves, ChessPosition pos, boolean promotion) {
        int horizontal = (color == ChessGame.TeamColor.WHITE) ? 1 : -1;

        if (pos.getColumn() + 1 <=8) {
            capturePiece(board, currPiece, moves, pos, horizontal, 1, promotion);
        }
        if (pos.getColumn() - 1 >= 1) {
            capturePiece(board, currPiece, moves, pos, horizontal, -1, promotion);
        }
    }

    private void forwardMoves(ChessGame.TeamColor color, ChessBoard board,
                              Collection<ChessMove> moves, ChessPosition pos) {
        int vertical;
        int doubleMove;
        if (color == ChessGame.TeamColor.WHITE) {
            vertical = 1;
            doubleMove = 2;
        }
        else {
            vertical = -1;
            doubleMove = 7;
        }

        ChessPosition newPos = new ChessPosition(pos.getRow() + vertical, pos.getColumn());
        if (board.getPiece(newPos) == null) {
            moves.add(new ChessMove(pos, newPos, null));
            if (pos.getRow() == doubleMove) {
                ChessPosition doubleForward = new ChessPosition(pos.getRow() + (2 * vertical), pos.getColumn());
                if (board.getPiece(doubleForward) == null) {
                    moves.add(new ChessMove(pos, doubleForward, null));
                }
            }
        }
    }
}
