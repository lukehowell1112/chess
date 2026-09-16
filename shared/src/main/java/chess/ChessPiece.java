package chess;

import java.util.ArrayList;
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
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {

        this.pieceColor = pieceColor;
        this.type = type;
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

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        Collection<ChessMove> moves = new ArrayList<>();

        switch (type) {
            case PAWN:

                // move forward logic
                int direction;
                if (this.pieceColor.equals(ChessGame.TeamColor.WHITE)) {
                    direction = 1;
                } else {
                    direction = -1;
                }

                int newRow = row + direction;
                int newCol = col;

                if (!(newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8)) {
                    if (this.pieceColor.equals(ChessGame.TeamColor.WHITE) && newRow != 8
                            || this.pieceColor.equals(ChessGame.TeamColor.BLACK) && newRow != 1) {
                        ChessPosition newPosition = new ChessPosition(newRow, newCol);
                        ChessPiece pieceAtNewPosition = board.getPiece(newPosition);

                        if (pieceAtNewPosition == null) {
                            ChessMove move = new ChessMove(myPosition, newPosition, null);
                            moves.add(move);
                        }
                    }
                }

                // diagonal capture logic
                ChessPosition forwardLeft = new ChessPosition(newRow, col - 1);
                ChessPosition forwardRight = new ChessPosition(newRow, col + 1);

                if (!(forwardLeft.getRow() < 1 || forwardLeft.getRow() > 8 || forwardLeft.getColumn() < 1 || forwardLeft.getColumn() > 8)) {

                    ChessPiece pieceAtForwardLeft = board.getPiece(forwardLeft);

                    if (pieceAtForwardLeft != null && !(pieceAtForwardLeft.getTeamColor().equals(this.pieceColor))) {
                        ChessMove move = new ChessMove(myPosition, forwardLeft, null);
                        moves.add(move);
                    }
                }

                if (!(forwardRight.getRow() < 1 || forwardRight.getRow() > 8 || forwardRight.getColumn() < 1 || forwardRight.getColumn() > 8)) {

                    ChessPiece pieceAtForwardRight = board.getPiece(forwardRight);

                    if (pieceAtForwardRight != null && !(pieceAtForwardRight.getTeamColor().equals(this.pieceColor))) {
                        ChessMove move = new ChessMove(myPosition, forwardRight, null);
                        moves.add(move);
                    }
                }

                // initial double move logic
                if (this.pieceColor.equals(ChessGame.TeamColor.WHITE) && row == 2
                        || this.pieceColor.equals(ChessGame.TeamColor.BLACK) && row == 7) {

                    int newRowPlusTwo = row + (direction * 2);
                    ChessPosition twoForward = new ChessPosition(newRowPlusTwo, col);
                    ChessPiece pieceAtTwoForward = board.getPiece(twoForward);

                    ChessPosition newPosition = new ChessPosition(newRow, newCol);
                    ChessPiece pieceAtNewPosition = board.getPiece(newPosition);

                    if (pieceAtNewPosition == null && pieceAtTwoForward == null) {
                        ChessMove move = new ChessMove(myPosition, twoForward, null);
                        moves.add(move);
                    }
                }

                // promotion logic
                if (this.pieceColor.equals(ChessGame.TeamColor.WHITE) && newRow == 8
                        || this.pieceColor.equals(ChessGame.TeamColor.BLACK) && newRow == 1) {

                    ChessPosition newPosition = new ChessPosition(newRow, newCol);
                    ChessPiece pieceAtNewPosition = board.getPiece(newPosition);

                    if (pieceAtNewPosition == null) {
                        ChessMove moveQ = new ChessMove(myPosition, newPosition, PieceType.QUEEN);
                        moves.add(moveQ);

                        ChessMove moveR = new ChessMove(myPosition, newPosition, PieceType.ROOK);
                        moves.add(moveR);

                        ChessMove moveB = new ChessMove(myPosition, newPosition, PieceType.BISHOP);
                        moves.add(moveB);

                        ChessMove moveK = new ChessMove(myPosition, newPosition, PieceType.KNIGHT);
                        moves.add(moveK);

                    }
                }
                break;

            case ROOK:
                int[][] rookOffsets = {
                        {1, 0},   // up
                        {-1, 0},  // down
                        {0, -1},  // left
                        {0, 1}    // right
                };

                for (int i = 0; i < 4; i++) {
                    int rowOffset = rookOffsets[i][0];
                    int colOffset = rookOffsets[i][1];

                    int newRow = row + rowOffset;
                    int newCol = col + colOffset;

                    while (!(newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8)) {
                        // check board bounds, friendly/enemy piece, add move, then
                        // update newRow/newCol to take another step in the same direction
                        ChessPosition newPosition = new ChessPosition(newRow, newCol);
                        ChessPiece pieceAtNewPosition = board.getPiece(newPosition);

                        if (pieceAtNewPosition != null && pieceAtNewPosition.getTeamColor().equals(this.pieceColor)) {
                            break; // check if newPos has a piece and is ours
                        }
                        else if (pieceAtNewPosition != null && !(pieceAtNewPosition.getTeamColor().equals(this.pieceColor))) {
                            ChessMove move = new ChessMove(myPosition, newPosition, null);
                            moves.add(move);
                            break; // check if the piece is an enemy piece, capture the square
                        } else {
                            ChessMove move = new ChessMove(myPosition, newPosition, null);
                            moves.add(move);
                            newRow += rowOffset;
                            newCol += colOffset;
                        } // open square, move, prepare for next iteration
                    }
                }
                break;
            case KNIGHT:

                int[][] knightOffsets = {
                        {2, 1}, //up-right
                        {2, -1}, //up-left
                        {1, 2}, //right-up
                        {-1, 2}, //right-down
                        {-2, 1}, //down-right
                        {-2, -1}, //down-left
                        {1, -2}, //left-up
                        {-1, -2} //left-down
                };

                for (int i = 0; i < 8; i++) {
                    int rowOffset = knightOffsets[i][0];
                    int colOffset = knightOffsets[i][1];
                    int newRow = row + rowOffset;
                    int newCol = col + colOffset;

                    if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {
                        continue; // check if new pos is on the board
                    }

                    ChessPosition newPosition = new ChessPosition(newRow, newCol);
                    ChessPiece pieceAtNewPosition = board.getPiece(newPosition); // get the square of the newPos on the board

                    if (pieceAtNewPosition != null && pieceAtNewPosition.getTeamColor().equals(this.pieceColor)) {
                        continue; // check if newPos has a piece and is ours
                    }

                    ChessMove move = new ChessMove(myPosition, newPosition, null);
                    moves.add(move); // all checks pass, add it to a possible move
                }

                break;
            case KING:

                int[][] kingOffsets = {
                        {1, 0},   // up
                        {-1, 0},  // down
                        {0, -1},  // left
                        {0, 1},   // right
                        {1, 1},   // upright
                        {1, -1},  // upleft
                        {-1, 1},  // downright
                        {-1, -1}  // downleft
                };

                for (int i = 0; i < 8; i++) {
                    int rowOffset = kingOffsets[i][0];
                    int colOffset = kingOffsets[i][1];
                    int newRow = row + rowOffset;
                    int newCol = col + colOffset;

                    if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {
                        continue; // check if new pos is on the board
                    }

                    ChessPosition newPosition = new ChessPosition(newRow, newCol);
                    ChessPiece pieceAtNewPosition = board.getPiece(newPosition); // get the square of the newPos on the board

                    if (pieceAtNewPosition != null && pieceAtNewPosition.getTeamColor().equals(this.pieceColor)) {
                        continue; // check if newPos has a piece and is ours
                    }

                    ChessMove move = new ChessMove(myPosition, newPosition, null);
                    moves.add(move); // all checks pass, add it to a possible move
                }
                
                break;
            case QUEEN: //merge rook and bishop logic

                int[][] queenOffsets = {
                        {1, 0},   // up
                        {-1, 0},  // down
                        {0, -1},  // left
                        {0, 1},   // right
                        {1, 1},   // up-right
                        {-1, -1},  // down-left
                        {1, -1},  // up-left
                        {-1, 1}    // down-right
                };

                for (int i = 0; i < 8; i++) {
                    int rowOffset = queenOffsets[i][0];
                    int colOffset = queenOffsets[i][1];

                    int newRow = row + rowOffset;
                    int newCol = col + colOffset;

                    while (!(newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8)) {
                        ChessPosition newPosition = new ChessPosition(newRow, newCol);
                        ChessPiece pieceAtNewPosition = board.getPiece(newPosition);

                        if (pieceAtNewPosition != null && pieceAtNewPosition.getTeamColor().equals(this.pieceColor)) {
                            break;
                        }
                        else if (pieceAtNewPosition != null && !(pieceAtNewPosition.getTeamColor().equals(this.pieceColor))) {
                            ChessMove move = new ChessMove(myPosition, newPosition, null);
                            moves.add(move);
                            break;
                        } else {
                            ChessMove move = new ChessMove(myPosition, newPosition, null);
                            moves.add(move);
                            newRow += rowOffset;
                            newCol += colOffset;
                        }
                    }
                }
                break;

            case BISHOP: // should be the same as rook, just diagonal
                int[][] bishopOffsets = {
                        {1, 1},   // up-right
                        {-1, -1},  // down-left
                        {1, -1},  // up-left
                        {-1, 1}    // down-right
                };

                for (int i = 0; i < 4; i++) {
                    int rowOffset = bishopOffsets[i][0];
                    int colOffset = bishopOffsets[i][1];

                    int newRow = row + rowOffset;
                    int newCol = col + colOffset;

                    while (!(newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8)) {
                        ChessPosition newPosition = new ChessPosition(newRow, newCol);
                        ChessPiece pieceAtNewPosition = board.getPiece(newPosition);

                        if (pieceAtNewPosition != null && pieceAtNewPosition.getTeamColor().equals(this.pieceColor)) {
                            break;
                        }
                        else if (pieceAtNewPosition != null && !(pieceAtNewPosition.getTeamColor().equals(this.pieceColor))) {
                            ChessMove move = new ChessMove(myPosition, newPosition, null);
                            moves.add(move);
                            break;
                        } else {
                            ChessMove move = new ChessMove(myPosition, newPosition, null);
                            moves.add(move);
                            newRow += rowOffset;
                            newCol += colOffset;
                        }
                    }
                }
                break;
        }

        return moves;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ChessPiece other)) {
            return false;
        }
        return this.pieceColor.equals(other.pieceColor) && this.type.equals(other.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
