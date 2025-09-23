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
                ChessPosition newPos = new ChessPosition(pos.getRow() + 1, pos.getColumn());
                if (board.getPiece(newPos) == null) {
                    moves.add(new ChessMove(pos, newPos, null));
                    if (pos.getRow() == 2) {
                        ChessPosition doubleForward = new ChessPosition(pos.getRow() + 2, pos.getColumn());
                        if (board.getPiece(doubleForward) == null) {
                            moves.add(new ChessMove(pos, doubleForward, null));
                        }
                    }
                }

                if (pos.getColumn() + 1 <=8) {
                    capturePiece(board, currPiece, moves, pos, 1, 1, false);
                }
                if (pos.getColumn() - 1 >= 1) {
                    capturePiece(board, currPiece, moves, pos, 1, -1, false);
                }

            } else if (pos.getRow() + 1 == 8){
                ChessPosition newPos = new ChessPosition(pos.getRow() + 1, pos.getColumn());
                addPromotionMoves(pos, newPos, moves);
                if (pos.getColumn() + 1 <=8) {
                    capturePiece(board, currPiece, moves, pos, 1, 1, true);
                }
                if (pos.getColumn() - 1 >= 1) {
                    capturePiece(board, currPiece, moves, pos, 1, -1, true);
                }
            }
        }
        else {
            if (pos.getRow() - 1 > 1) {
                ChessPosition newPos = new ChessPosition(pos.getRow()-1, pos.getColumn());

                if (board.getPiece(newPos) == null) {
                    moves.add(new ChessMove(pos, newPos, null));
                    if (pos.getRow() == 7) {
                        ChessPosition doubleForward = new ChessPosition(pos.getRow() - 2, pos.getColumn());
                        if (board.getPiece(doubleForward) == null) {
                            moves.add(new ChessMove(pos, doubleForward, null));
                        }
                    }
                }

                if (pos.getColumn() + 1 <=8) {
                    capturePiece(board, currPiece, moves, pos, -1, 1, false);
                }
                if (pos.getColumn() - 1 >= 1) {
                    capturePiece(board, currPiece, moves, pos, -1, -1, false);
                }
            } else if (pos.getRow() - 1 == 1){
                ChessPosition newPos = new ChessPosition(pos.getRow()-1, pos.getColumn());
                addPromotionMoves(pos, newPos, moves);
                if (pos.getColumn() + 1 <=8) {
                    capturePiece(board, currPiece, moves, pos, -1, 1, true);
                }
                if (pos.getColumn() - 1 >= 1) {
                    capturePiece(board, currPiece, moves, pos, -1, -1, true);
                }
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

    private void capturePiece(ChessBoard board, ChessPiece currPiece, Collection<ChessMove> moves, ChessPosition pos, int horizontal, int vertical, boolean promotion) {
        ChessPosition diagonalRight = new ChessPosition(pos.getRow() + Integer.signum(horizontal), pos.getColumn()+ Integer.signum(vertical));
        if (board.getPiece(diagonalRight) != null) {
            if (!currPiece.getTeamColor().equals(board.getPiece(diagonalRight).getTeamColor())) {
                if (promotion) {
                    addPromotionMoves(pos, diagonalRight, moves);
                }
                else {
                    moves.add(new ChessMove(pos, diagonalRight, null));
                }
            }
        }
    }
}
