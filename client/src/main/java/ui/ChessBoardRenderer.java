package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;


public class ChessBoardRenderer {
    private final static int SQUARES_ON_SIDE = 8;
    private final static String[] HORIZONTAL_HEADERS = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private final static String[] VERTICAL_HEADERS = {"1", "2", "3", "4", "5", "6", "7", "8"};

    public static String drawBoard (GameData gameData, ChessGame.TeamColor color) {
        var gameBoard = gameData.game().getBoard();

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        int increment = (color == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int startRow = (color == ChessGame.TeamColor.WHITE) ? 0 : SQUARES_ON_SIDE - 1;
        int endRow = (color == ChessGame.TeamColor.WHITE) ? SQUARES_ON_SIDE : -1;
        drawHorizontalBorder(out, increment);

        for (int i = startRow; i != endRow; i += increment) {
            drawRow(out, i, gameBoard, color);
        }
        drawHorizontalBorder(out, increment);
        return String.format("Successfully joined game with id %d as color %s\n", gameData.gameID(), color.name());
    }

    private static void drawHorizontalBorder(PrintStream out, int increment) {
        setBorderColors(out);
        out.print(EMPTY);

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

    private static void drawRow(PrintStream out, int currRow, ChessBoard board, ChessGame.TeamColor playerColor) {
        setBorderColors(out);
        out.print(" " + VERTICAL_HEADERS[currRow] + " ");

        int startColumn = (playerColor == ChessGame.TeamColor.WHITE) ? 0 : SQUARES_ON_SIDE - 1;
        int endColumn = (playerColor == ChessGame.TeamColor.WHITE) ? SQUARES_ON_SIDE : -1;
        int step = (playerColor == ChessGame.TeamColor.WHITE) ? 1 : -1;
        for (int j = startColumn; j != endColumn; j += step) {
            var piece = board.getPiece(new ChessPosition(currRow + 1, j + 1));

            out.print(((currRow + j) % 2 == 0) ? SET_BG_COLOR_BLACK : SET_BG_COLOR_WHITE);

            if (piece == null) {
                out.print(EMPTY);
            } else {
                out.print(getPieceColor(piece));
                out.print(getPieceSymbol(piece));
            }
        }
        setBorderColors(out);
        out.print(" " + VERTICAL_HEADERS[currRow] + " ");
        out.print(RESET);
        out.println();
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

