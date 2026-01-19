package chess;

import java.util.Collection;
import java.util.HashSet;
import static chess.ChessPiece.PieceType.*;
import static chess.ChessGame.TeamColor.*;

public class PawnMovesCalculator implements MoveRules{
    /**
     * Calculates all valid moves for a Pawn
     * @param board Current Chess board
     * @param pos Position of pawn being moved
     * @return List of valid moves for a pawn. This list may have anywhere between 0 and 4 moves.
     */
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

    /**
     * Adds moves for a pawn from currPosition to newPosition
     * Moves are added with ROOK, KNIGHT, BISHOP, and QUEEN as promotion pieces
     * @param currPosition Pawn's current position
     * @param newPosition Space pawn will move to
     * @param moves List of valid moves for the pawn
     */
    private void promotionMoves(ChessPosition currPosition,
                                ChessPosition newPosition,
                                Collection<ChessMove> moves) {
        moves.add(new ChessMove(currPosition, newPosition, ROOK));
        moves.add(new ChessMove(currPosition, newPosition, BISHOP));
        moves.add(new ChessMove(currPosition, newPosition, KNIGHT));
        moves.add(new ChessMove(currPosition, newPosition, QUEEN));
    }

    /**
     * Adds the moves for a capturing a piece that would not result in a promotion.
     * Calls the promotionMoves function when needing to handle promotion
     * @param board Current chess board
     * @param currPiece Pawn being moved
     * @param moves list of moves for the pawn
     * @param currPos pawn's current position
     * @param newPos Pawn's potential new position
     * @param promo promotion row for the current piece
     */
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

    /**
     * Used to make sure that the row and column of a potential movement are not out of bounds.
     *
     * @param row row of potential move
     * @param col column of potential move
     * @return false if either row or column is greater than 8 or less than 1
     */
    private boolean offBoard(int row, int col) {
        return row > 8 || row < 1 || col > 8 || col < 1;
    }

    /**
     * Looks for and adds any necessary diagonal moves to the list of possible moves for a pawn.
     * Makes sure either of the diagonal moves will not go outside the board, then calls capture to
     * actually add the move to the list of ChessMove
     *
     * @param moves List of all possible moves
     * @param board Current chess board
     * @param pos Position of current Pawn
     * @param pawn Pawn being moved
     * @param promo promotion row for the pawn - value will be 8 for white pawn
     *              value will be 1 for black pawn
     * @param direction direction of movement - Value is 1 if piece color is White
     *                  Value is -1 if piece color is Black
     */
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

    /**
     * Calculates the moves directly forward for a pawn.
     * If the pawn is on the starting row there are 2 potential moves.
     * If not, there is one potential moves.
     *
     * @param moves List of valid moves
     * @param board Current chess board
     * @param pos Pawn's current position
     * @param start starting row for the piece (based on the pawn's color)
     * @param promo promotion row for the pawn (based on the pawn's color)
     * @param direction integer to represent whether pawn moves up or down the board
     *                  Value will be 1 if WHITE, -1 if BLACK
     */
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
