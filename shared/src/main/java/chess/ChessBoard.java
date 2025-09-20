package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private final ChessPiece [][] board = new ChessPiece[8][8];
    public ChessBoard() {}

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() - 1][position.getColumn() - 1] = piece;
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
            board[0][i] = new ChessPiece(ChessGame.TeamColor.WHITE, backRow[i]);
            board[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, backRow[i]);
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
        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
