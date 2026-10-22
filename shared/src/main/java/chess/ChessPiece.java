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

    //pawn helper: needs current, final, check promo, moves
    private void addPawnMove(ChessPosition myPosition, ChessPosition newPosition, boolean isPromotionRow, Collection<ChessMove> moves) {
        //promote to queen, bishop, rook, knight
        if (isPromotionRow) {
            ChessMove moveQ = new ChessMove(myPosition, newPosition, PieceType.QUEEN);
            moves.add(moveQ);

            ChessMove moveB = new ChessMove(myPosition, newPosition, PieceType.BISHOP);
            moves.add(moveB);

            ChessMove moveR = new ChessMove(myPosition, newPosition, PieceType.ROOK);
            moves.add(moveR);

            ChessMove moveK = new ChessMove(myPosition, newPosition, PieceType.KNIGHT);
            moves.add(moveK);
        } else {
            ChessMove move = new ChessMove(myPosition, newPosition, null);
            moves.add(move);
        }
    }

    private static boolean isOnBoard(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    private void addSlidingMoves(ChessBoard board, ChessPosition myPosition, int[][] offsets, Collection<ChessMove> moves) {

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        for (int[] offset : offsets) {
            int rowOffset = offset[0];
            int colOffset = offset[1];

            int newRow = row + rowOffset;
            int newCol = col + colOffset;

            //check bounds, e/f piece, take another step
            while (isOnBoard(newRow, newCol)) {
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece pieceAtNewPosition = board.getPiece(newPosition);

                if (pieceAtNewPosition != null && pieceAtNewPosition.getTeamColor() == this.pieceColor) {
                    break;
                } else if (pieceAtNewPosition != null) {
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
    }

    private void addSteppingMoves(ChessBoard board, ChessPosition myPosition, int[][] offsets, Collection<ChessMove> moves) {

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        for (int[] offset : offsets) {
            int rowOffset = offset[0];
            int colOffset = offset[1];
            int newRow = row + rowOffset;
            int newCol = col + colOffset;

            //check boundary, check new position for e/f piece
            if (!isOnBoard(newRow, newCol)) {
                continue;
            }

            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            ChessPiece pieceAtNewPosition = board.getPiece(newPosition);

            if (pieceAtNewPosition != null && pieceAtNewPosition.getTeamColor() == this.pieceColor) {
                continue;
            }

            ChessMove move = new ChessMove(myPosition, newPosition, null);
            moves.add(move);
        }
    }

    private void addPawnMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int direction;
        int startingRow;
        int promotionRow;
        if (this.pieceColor == ChessGame.TeamColor.WHITE) {
            direction = 1;
            startingRow = 2;
            promotionRow = 8;
        } else {
            direction = -1;
            startingRow = 7;
            promotionRow = 1;
        }

        int newRow = row + direction;
        int newCol = col;

        if (isOnBoard(newRow, newCol)) {
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            ChessPiece pieceAtNewPosition = board.getPiece(newPosition);

            if (pieceAtNewPosition == null) {
                addPawnMove(myPosition, newPosition, newRow == promotionRow, moves);

                //initial double
                if (row == startingRow) {

                    int newRowPlusTwo = row + (direction * 2);
                    ChessPosition twoForward = new ChessPosition(newRowPlusTwo, col);
                    ChessPiece pieceAtTwoForward = board.getPiece(twoForward);

                    if (pieceAtTwoForward == null) {
                        ChessMove move = new ChessMove(myPosition, twoForward, null);
                        moves.add(move);
                    }
                }
            }
        }

        //diagonal capture
        int[] captureSides = {-1, 1};  // left, right
        for (int captureSide : captureSides) {
            int captureCol = col + captureSide;
            if (isOnBoard(newRow, captureCol)) {
                ChessPosition capturePosition = new ChessPosition(newRow, captureCol);
                ChessPiece pieceAtCapture = board.getPiece(capturePosition);
                if (pieceAtCapture != null && pieceAtCapture.getTeamColor() != this.pieceColor) {
                    addPawnMove(myPosition, capturePosition, newRow == promotionRow, moves);
                }
            }
        }
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();

        switch (type) { //cases for each piece
            case PAWN: {
                addPawnMoves(board, myPosition, moves);
                break;
            }

            case ROOK: {

                int[][] rookOffsets = {
                        {1, 0}, //up
                        {-1, 0}, //down
                        {0, -1}, //left
                        {0, 1} //right
                };
                addSlidingMoves(board, myPosition, rookOffsets, moves);
                break;
            }

            case KNIGHT: {

                int[][] knightOffsets = {
                        {2, 1}, //up-right
                        {2, -1}, //up-left
                        {-2, 1}, //down-right
                        {-2, -1}, //down-left
                        {1, 2}, //right-up
                        {-1, 2}, //right-down
                        {1, -2}, //left-up
                        {-1, -2} //left-down
                };
                addSteppingMoves(board, myPosition, knightOffsets, moves);
                break;
            }

            case BISHOP: {

                int[][] bishopOffsets = {
                        {1, 1}, //up-right
                        {-1, 1}, //down-right
                        {1, -1}, //up-left
                        {-1, -1} //down-left
                };
                addSlidingMoves(board, myPosition, bishopOffsets, moves);
                break;
            }

            case QUEEN: {
                int[][] queenOffsets = {
                        {1, 0}, //up
                        {-1, 0}, //down
                        {0, -1}, //left
                        {0, 1}, //right
                        {1, 1}, //up-right
                        {-1, 1}, //down-right
                        {1, -1}, //up-left
                        {-1, -1} //down-left
                };
                addSlidingMoves(board, myPosition, queenOffsets, moves);
                break;
            }

            case KING: {

                int[][] kingOffsets = {
                        {1, 0}, //up
                        {-1, 0}, //down
                        {0, -1}, //left
                        {0, 1}, //right
                        {1, 1}, //up-right
                        {1, -1}, //up-left
                        {-1, 1}, //down-right
                        {-1, -1} //down-left
                };
                addSteppingMoves(board, myPosition, kingOffsets, moves);
                break;
            }
        }
        return moves;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ChessPiece other)) {
            return false;
        }
        return this.pieceColor == other.pieceColor && this.type == other.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
