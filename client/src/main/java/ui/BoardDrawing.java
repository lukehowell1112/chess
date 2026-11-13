package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class BoardDrawing {

    private String whiteChar(ChessPiece.PieceType type) {
        return switch (type) {
            case ROOK -> EscapeSequences.WHITE_ROOK;
            case BISHOP -> EscapeSequences.WHITE_BISHOP;
            case PAWN -> EscapeSequences.WHITE_PAWN;
            case KNIGHT -> EscapeSequences.WHITE_KNIGHT;
            case QUEEN -> EscapeSequences.WHITE_QUEEN;
            case KING -> EscapeSequences.WHITE_KING;
        };
    }

    private String blackChar(ChessPiece.PieceType type) {
        return switch (type) {
            case ROOK -> EscapeSequences.BLACK_ROOK;
            case BISHOP -> EscapeSequences.BLACK_BISHOP;
            case PAWN -> EscapeSequences.BLACK_PAWN;
            case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
            case QUEEN -> EscapeSequences.BLACK_QUEEN;
            case KING -> EscapeSequences.BLACK_KING;
        };
    }

    private String decidePiece(ChessPiece piece) {
        if (piece == null) {
            return EscapeSequences.EMPTY;
        }
        if (piece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
            return whiteChar(piece.getPieceType());
        }
        return blackChar(piece.getPieceType());
    }

    private String buildSquare(int row, int col, ChessPiece piece) {
        String background;
        if ((row + col) % 2 == 0) {
            background = EscapeSequences.SET_BG_COLOR_DARK_GREY;
        } else {
            background = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
        }
        return background + decidePiece(piece) + EscapeSequences.RESET_BG_COLOR;
    }

    private String buildRow(int row, ChessBoard board) {
        StringBuilder line = new StringBuilder();
        for (int col = 1; col <= 8; col++) {
            ChessPiece piece = board.getPiece(new ChessPosition(row, col));
            line.append(buildSquare(row, col, piece));
        }
        line.append("\n");
        return line.toString();
    }
}
