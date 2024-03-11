package chess;

public class Rook extends Piece {
    public Rook(boolean isWhite, PieceFile file, int rank) {
        super(isWhite, file, rank);

        if (isWhite) {
            this.pieceType = PieceType.WR;
        } else {
            this.pieceType = PieceType.BR;
        }
    }

    // can move vertically and horizontally across whole board, can go backwards,
    // castling must be implemented
    // can't move through pieces
    // can capture pieces

    public boolean isLegalMove(ReturnPiece endPiece, char startPieceFile, int startPieceRank, char endPieceFile,
            int endPieceRank) {
        if (startPieceFile != endPieceFile && startPieceRank != endPieceRank) {
            return false;
        }

        // Check if the end position is occupied by a piece of the same color
        if (endPiece != null && endPiece.pieceType.toString().charAt(0) == (isWhite ? 'W' : 'B')) {
            return false;
        }

        // Check if there is a piece in the path
        if (startPieceFile == endPieceFile) {
            // The move is vertical
            int start = Math.min(startPieceRank, endPieceRank);
            int end = Math.max(startPieceRank, endPieceRank);
            for (int i = start + 1; i < end; i++) {
                if (Chess.board[i - 1][startPieceFile - 'a'] != null) {
                    return false;
                }
            }
        } else {
            // The move is horizontal
            int start = Math.min(startPieceFile, endPieceFile);
            int end = Math.max(startPieceFile, endPieceFile);
            for (int i = start + 1; i < end; i++) {
                if (Chess.board[startPieceRank - 1][i - 'a'] != null) {
                    return false;
                }
            }
        }

        return true;
    }
}
