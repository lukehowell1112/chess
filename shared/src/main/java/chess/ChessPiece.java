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
                // pawn logic
                break;
            case ROOK:
                // rook logic
                break;
            case KNIGHT:
                // knight logic
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
                        continue;
                    }

                    ChessPosition newPosition = new ChessPosition(newRow, newCol);

                    ChessMove move = new ChessMove(myPosition, newPosition, null);
                }

                break;
            case QUEEN:
                // queen logic
                break;
            case BISHOP:
                // bishop logic
                break;
        }
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
