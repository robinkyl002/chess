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
                    ChessPosition diagonalRight = new ChessPosition(pos.getRow()+1, pos.getColumn()+1);
                    if (board.getPiece(diagonalRight) != null) {
                        if (!currPiece.getTeamColor().equals(board.getPiece(diagonalRight).getTeamColor())) {
                            moves.add(new ChessMove(pos, diagonalRight, null));
                        }
                    }
                }
                if (pos.getColumn() - 1 >= 1) {
                    ChessPosition diagonalLeft = new ChessPosition(pos.getRow()+1, pos.getColumn()-1);
                    if (board.getPiece(diagonalLeft) != null) {
                        if (!currPiece.getTeamColor().equals(board.getPiece(diagonalLeft).getTeamColor())) {
                            moves.add(new ChessMove(pos, diagonalLeft, null));
                        }
                    }
                }

            } else if (pos.getRow() + 1 == 8){
                ChessPosition newPos = new ChessPosition(pos.getRow() + 1, pos.getColumn());
                addPromotionMoves(pos, newPos, moves);
                if (pos.getColumn() + 1 <=8) {
                    ChessPosition diagonalRight = new ChessPosition(pos.getRow()+1, pos.getColumn()+1);
                    if (board.getPiece(diagonalRight) != null) {
                        if (!currPiece.getTeamColor().equals(board.getPiece(diagonalRight).getTeamColor())) {
                            addPromotionMoves(pos, diagonalRight, moves);
                        }
                    }
                }
                if (pos.getColumn() - 1 >= 1) {
                    ChessPosition diagonalLeft = new ChessPosition(pos.getRow()+1, pos.getColumn()-1);
                    if (board.getPiece(diagonalLeft) != null) {
                        if (!currPiece.getTeamColor().equals(board.getPiece(diagonalLeft).getTeamColor())) {
                            addPromotionMoves(pos, diagonalLeft, moves);
                        }
                    }
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
                    ChessPosition diagonalRight = new ChessPosition(pos.getRow()-1, pos.getColumn()+1);
                    if (board.getPiece(diagonalRight) != null) {
                        if (!currPiece.getTeamColor().equals(board.getPiece(diagonalRight).getTeamColor())) {
                            moves.add(new ChessMove(pos, diagonalRight, null));
                        }
                    }
                }
                if (pos.getColumn() - 1 >= 1) {
                    ChessPosition diagonalLeft = new ChessPosition(pos.getRow()-1, pos.getColumn()-1);
                    if (board.getPiece(diagonalLeft) != null) {
                        if (!currPiece.getTeamColor().equals(board.getPiece(diagonalLeft).getTeamColor())) {
                            moves.add(new ChessMove(pos, diagonalLeft, null));
                        }
                    }
                }
            } else if (pos.getRow() - 1 == 1){
                ChessPosition newPos = new ChessPosition(pos.getRow()-1, pos.getColumn());
                addPromotionMoves(pos, newPos, moves);
                if (pos.getColumn() + 1 <=8) {
                    ChessPosition diagonalRight = new ChessPosition(pos.getRow()-1, pos.getColumn()+1);
                    if (board.getPiece(diagonalRight) != null) {
                        if (!currPiece.getTeamColor().equals(board.getPiece(diagonalRight).getTeamColor())) {
                            addPromotionMoves(pos, diagonalRight, moves);
                        }
                    }
                }
                if (pos.getColumn() - 1 >= 1) {
                    ChessPosition diagonalLeft = new ChessPosition(pos.getRow()-1, pos.getColumn()-1);
                    if (board.getPiece(diagonalLeft) != null) {
                        if (!currPiece.getTeamColor().equals(board.getPiece(diagonalLeft).getTeamColor())) {
                            addPromotionMoves(pos, diagonalLeft, moves);
                        }
                    }
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
}
