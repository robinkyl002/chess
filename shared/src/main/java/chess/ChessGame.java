package chess;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor currTeam;
    public ChessGame() {
        board = new ChessBoard();
        currTeam = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currTeam;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currTeam = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        var piece = board.getPiece(startPosition);

        if (piece == null) {
            return null;
        }

        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);

        moves.removeIf(move -> !testMove(move));

        return moves;
    }

    private boolean testMove(ChessMove move) {
        ChessPiece movingPiece = board.getPiece(move.getStartPosition());
        ChessPiece capturedPiece = board.getPiece(move.getEndPosition());

        performTestMove(move);

        boolean validMove = !isInCheck(movingPiece.getTeamColor());

        revertTestMove(movingPiece, capturedPiece, move.getStartPosition(), move.getEndPosition());

        return validMove;
    }

    private void revertTestMove(ChessPiece moving, ChessPiece captured, ChessPosition start, ChessPosition end) {
        board.addPiece(start, moving);
        board.addPiece(end, captured);
    }

    private void performTestMove(ChessMove move) {
        ChessPiece mover = board.getPiece(move.getStartPosition());

        if (move.getPromotionPiece() == null) {
            board.addPiece(move.getEndPosition(), mover);
        }
        else {
            board.addPiece(move.getEndPosition(), new ChessPiece(mover.getTeamColor(), move.getPromotionPiece()));
        }
        board.addPiece(move.getStartPosition(), null);

    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition initPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();

        Collection<ChessMove> validMoves = validMoves(initPosition);
        if (validMoves == null) {
            throw new InvalidMoveException();
        } else {
            boolean validMove = validMoves.contains(move);
            if (!validMove) {
                throw new InvalidMoveException();
            }
        }

        ChessPiece currPiece = board.getPiece(initPosition);
        if (currPiece.getTeamColor() != currTeam) {
            throw new InvalidMoveException();
        }

        board.movePiece(initPosition, endPosition, move.getPromotionPiece());

        if (currPiece.getPieceType() == ChessPiece.PieceType.KING) {
            board.setKingPosition(currPiece.getTeamColor(), endPosition);
        }

        currTeam = (currTeam == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        for (int i = 1; i < 9; i++) {
            for(int j = 1; j < 9; j++) {
                ChessPosition pos = new ChessPosition(i, j);
                ChessPiece selectedPiece = board.getPiece(pos);
                if(selectedPiece != null && checkIndividualPieceForCheck(selectedPiece, pos, teamColor)) {
                    return true;
                }
            }
        }

        return false;
    }


    private boolean checkIndividualPieceForCheck(ChessPiece piece, ChessPosition pos, TeamColor teamColor) {

        if (piece.getTeamColor() != teamColor) {
            var moves = piece.pieceMoves(board, pos);


            Iterator<ChessMove> it = moves.iterator();

            while (it.hasNext()) {
                ChessMove move = it.next();
                if (move.getEndPosition().equals(board.getKingPosition(teamColor))) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && noValidMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && noValidMoves(teamColor);
    }

    private boolean noValidMoves(TeamColor teamColor) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition pos = new ChessPosition(i, j);
                if (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() == teamColor) {
                    var moves = validMoves(pos);
                    if (!moves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && currTeam == chessGame.currTeam;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(board);
        result = 31 * result + Objects.hashCode(currTeam);
        return result;
    }
}
