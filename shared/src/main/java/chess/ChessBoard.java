package chess;

import java.util.Arrays;
import java.util.Objects;

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
        board[position.getRow() - 1][position.getColumn() - 1] = piece;

        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                whiteKingPosition = position;
            }
            else {
                blackKingPosition = position;
            }
        }
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

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow() - 1][position.getColumn() - 1];
    }

    public void movePiece(ChessPosition startPos, ChessPosition endPos, ChessPiece.PieceType promotionPiece) {

        ChessPiece currPiece = board[startPos.getRow()-1][startPos.getColumn()-1];

        if (currPiece.getPieceType() == ChessPiece.PieceType.PAWN && endPos.getRow() == 1 || endPos.getRow() == 8) {
            board[endPos.getRow()-1][endPos.getColumn()-1] = new ChessPiece(currPiece.getTeamColor(), promotionPiece);
            board[startPos.getRow()-1][startPos.getColumn()-1] = null;
        }
        else {
            board[endPos.getRow()-1][endPos.getColumn()-1] = currPiece;
            board[startPos.getRow()-1][startPos.getColumn()-1] = null;
        }
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessPiece.PieceType[] backRow = {ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP,
                        ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KING, ChessPiece.PieceType.BISHOP,
                        ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK};

        for (ChessPiece[] chessPieces : board) {
            Arrays.fill(chessPieces, null);
        }

        for (int i = 0; i < backRow.length; i++) {
            addPiece(new ChessPosition(1, i+1), new ChessPiece(ChessGame.TeamColor.WHITE, backRow[i]));
            addPiece(new ChessPosition(8, i+1), new ChessPiece(ChessGame.TeamColor.BLACK, backRow[i]));
        }

        Arrays.fill(board[1], new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        Arrays.fill(board[6], new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(board, that.board) && Objects.equals(whiteKingPosition, that.whiteKingPosition) && Objects.equals(blackKingPosition, that.blackKingPosition);
    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(board);
        result = 31 * result + Objects.hashCode(whiteKingPosition);
        result = 31 * result + Objects.hashCode(blackKingPosition);
        return result;
    }
}
