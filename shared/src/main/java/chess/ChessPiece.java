package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("Current Piece {color=%s, type=%s}", pieceColor, type);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(pieceColor);
        result = 31 * result + Objects.hashCode(type);
        return result;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        return switch (type) {
            case PieceType.BISHOP -> {
                BishopMovesCalculator bishopMoves = new BishopMovesCalculator();
                yield bishopMoves.moves(board, myPosition);
            }
            case PieceType.ROOK -> {
                RookMovesCalculator rookMoves = new RookMovesCalculator();
                yield rookMoves.moves(board, myPosition);
            }
            case PieceType.QUEEN -> {
                QueenMovesCalculator queenMoves = new QueenMovesCalculator();
                yield queenMoves.moves(board, myPosition);
            }
            case PieceType.KING -> {
                KingMovesCalculator kingMoves = new KingMovesCalculator();
                yield kingMoves.moves(board, myPosition);
            }
            case PieceType.KNIGHT -> {
                KnightMovesCalculator knightMoves = new KnightMovesCalculator();
                yield knightMoves.moves(board, myPosition);
            }
            case PieceType.PAWN -> {
                PawnMovesCalculator pawnMoves = new PawnMovesCalculator();
                yield pawnMoves.moves(board, myPosition);
            }
        };
    }
}
