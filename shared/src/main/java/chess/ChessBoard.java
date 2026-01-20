package chess;

import java.util.Arrays;
import java.util.Objects;

import static chess.ChessGame.TeamColor.*;
import static chess.ChessPiece.PieceType.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private final ChessPiece [][] board = new ChessPiece[8][8];
    private ChessPosition whiteKingPosition;
    private ChessPosition blackKingPosition;
    public ChessBoard() {
        whiteKingPosition = null;
        blackKingPosition = null;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()-1][position.getColumn()-1] = piece;

        if (piece == null) {
            return;
        }

        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                whiteKingPosition = position;
            }
            else {
                blackKingPosition = position;
            }
        }
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()-1][position.getColumn()-1];
    }

    void setKingPosition(ChessGame.TeamColor color, ChessPosition pos) {
        if (color == ChessGame.TeamColor.WHITE){
            whiteKingPosition = pos;
        }
        else {
            blackKingPosition = pos;
        }
    }

    ChessPosition getKingPosition(ChessGame.TeamColor color) {
        return (color == ChessGame.TeamColor.WHITE) ? whiteKingPosition : blackKingPosition;
    }

    public void movePiece(ChessPosition start, ChessPosition end, ChessPiece.PieceType promotionPiece) {
        ChessPiece currPiece = board[start.getRow()-1][start.getColumn()-1];

        if (currPiece.getPieceType() == PAWN && (end.getRow() == 1 || end.getRow() == 8)) {
            board[end.getRow()-1][end.getColumn()-1] = new ChessPiece(currPiece.getTeamColor(), promotionPiece);
        }
        else {
            board[end.getRow()-1][end.getColumn()-1] = currPiece;
        }
        board[start.getRow()-1][start.getColumn()-1] = null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
       ChessPiece.PieceType[] backRow = {ROOK, KNIGHT, BISHOP, QUEEN, KING, BISHOP, KNIGHT, ROOK};

       for (ChessPiece[] currPieces : board) {
           Arrays.fill(currPieces, null);
       }

       for (int i = 0; i < backRow.length; i++) {
           board[0][i] = new ChessPiece(WHITE, backRow[i]);
           board[7][i] = new ChessPiece(BLACK, backRow[i]);
       }

       Arrays.fill(board[1], new ChessPiece(WHITE, PAWN));
       Arrays.fill(board[6], new ChessPiece(BLACK, PAWN));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(board, that.board) && Objects.equals(whiteKingPosition,
                that.whiteKingPosition) && Objects.equals(blackKingPosition, that.blackKingPosition);
    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(board);
        result = 31 * result + Objects.hashCode(whiteKingPosition);
        result = 31 * result + Objects.hashCode(blackKingPosition);
        return result;
    }
}
