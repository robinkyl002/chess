package ui;

import chess.*;
import model.GameData;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;

import static ui.EscapeSequences.*;


public class ChessBoardRenderer {
    private final static int SQUARES_ON_SIDE = 8;
    private final static String[] HORIZONTAL_HEADERS = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private final static String[] VERTICAL_HEADERS = {"8", "7", "6", "5", "4", "3", "2", "1"};

    public static void drawBoard (GameData gameData, ChessGame.TeamColor color, Collection<ChessMove> validMoves) {
        var gameBoard = gameData.game().getBoard();

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        int increment = (color == ChessGame.TeamColor.WHITE) ? -1 : 1;
        int startRow = (color == ChessGame.TeamColor.WHITE) ? SQUARES_ON_SIDE : 1;
        int endRow = (color == ChessGame.TeamColor.WHITE) ? 0 : SQUARES_ON_SIDE + 1 ;
        drawHorizontalBorder(out, color);

        HashSet<ChessMove> possibleMoves;

        if (validMoves == null || validMoves.isEmpty()) {
            possibleMoves = new HashSet<>();
        } else {
            possibleMoves = new HashSet<>(validMoves);
        }

        var startPos = (!possibleMoves.isEmpty()) ? possibleMoves.iterator().next().getStartPosition() : null;
        for (int i = startRow; i != endRow; i += increment) {
            HashSet<ChessPosition> rowMoves = new HashSet<>();
            if (!possibleMoves.isEmpty()) {
                for (ChessMove move : possibleMoves) {
                    if (move.getEndPosition().getRow() == i) {
                        rowMoves.add(move.getEndPosition());
                    }
                }
            }
            drawRow(out, i, gameBoard, color, rowMoves, startPos);
        }
        drawHorizontalBorder(out, color);
    }

    private static void drawHorizontalBorder(PrintStream out, ChessGame.TeamColor playerColor) {
        setBorderColors(out);
        out.print(EMPTY);

        int increment = (playerColor == ChessGame.TeamColor.WHITE) ? 1 : -1;

        if (increment == 1) {
            for (int i = 0; i < SQUARES_ON_SIDE; i += increment) {
                out.print(" " + HORIZONTAL_HEADERS[i] + " ");
            }
        } else {
            for (int i = SQUARES_ON_SIDE - 1; i >= 0; i += increment) {
                out.print(" " + HORIZONTAL_HEADERS[i] + " ");
            }
        }
        out.print(EMPTY);
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        out.println();
    }

    private static void drawRow(PrintStream out, int currRow, ChessBoard board, ChessGame.TeamColor playerColor,
                                Collection<ChessPosition> validPositions, ChessPosition startPos) {
        setBorderColors(out);
        out.print(" " + (currRow) + " ");

        int startColumn = (playerColor == ChessGame.TeamColor.WHITE) ? 1 : SQUARES_ON_SIDE ;
        int endColumn = (playerColor == ChessGame.TeamColor.WHITE) ? SQUARES_ON_SIDE + 1: 0;
        int step = (playerColor == ChessGame.TeamColor.WHITE) ? 1 : -1;

        boolean highlightRow = (validPositions != null && !validPositions.isEmpty())
                || (startPos != null && startPos.getRow() == currRow);

        if (highlightRow) {
            drawRowWithHighlight(out, currRow, startColumn, endColumn, step, board, startPos, validPositions);
        }
        else {
            drawStandardRow(out, currRow, startColumn, endColumn, step, board);
        }

        setBorderColors(out);
        out.print(" " + (currRow) + " ");
        out.print(RESET);
        out.println();
    }

    private static void drawStandardRow(PrintStream out, int currRow, int startColumn,
                                        int endColumn, int step, ChessBoard board) {
        for (int j = startColumn; j != endColumn; j += step) {
            var currPosition = new ChessPosition(currRow, j);
            var piece = board.getPiece(currPosition);
            out.print(((currRow + j) % 2 == 0) ? SET_BG_COLOR_BLACK : SET_BG_COLOR_WHITE);

            if (piece == null) {
                out.print(EMPTY);
            } else {
                out.print(getPieceColor(piece));
                out.print(getPieceSymbol(piece));
            }
        }
    }

    private static void drawRowWithHighlight(PrintStream out, int currRow, int startColumn,
                                             int endColumn, int step, ChessBoard board,
                                             ChessPosition startPos, Collection<ChessPosition> validPositions) {
        for (int j = startColumn; j != endColumn; j += step) {
            var currPosition = new ChessPosition(currRow, j);
            var piece = board.getPiece(currPosition);

            if (validPositions.contains(currPosition) || currPosition.equals(startPos)) {
                out.print(((currRow + j) % 2 == 0) ? SET_BG_COLOR_GREEN : SET_BG_COLOR_DARK_GREEN);
            } else {
                out.print(((currRow + j) % 2 == 0) ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLACK);
            }

            if (piece == null) {
                out.print(EMPTY);
            } else {
                out.print((currPosition.equals(startPos)) ? SET_TEXT_COLOR_BLACK : getPieceColor(piece));
                out.print(getPieceSymbol(piece));
            }
        }
    }

    private static void setBorderColors(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static String getPieceSymbol(ChessPiece piece) {
        return switch (piece.getPieceType()) {
            case KING -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KING : BLACK_KING;
            case QUEEN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_QUEEN : BLACK_QUEEN;
            case ROOK -> piece.getTeamColor()  == ChessGame.TeamColor.WHITE ? WHITE_ROOK : BLACK_ROOK;
            case KNIGHT -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KNIGHT : BLACK_KNIGHT;
            case BISHOP -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_BISHOP : BLACK_BISHOP;
            case PAWN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_PAWN : BLACK_PAWN;
        };
    }

    private static String getPieceColor(ChessPiece piece) {
        return (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? SET_TEXT_COLOR_RED : SET_TEXT_COLOR_BLUE;
    }
}

