package chess;

public class Knight extends Piece {
    public Knight(boolean isWhite, PieceFile file, int rank) {
        super(isWhite, file, rank);

        if (isWhite) {
            this.pieceType = PieceType.WN;
        } else {
            this.pieceType = PieceType.BN;
        }
    }

    @Override
    public boolean isLegalMove(ReturnPiece endPiece, char startPieceFile, int startPieceRank, char endPieceFile,
            int endPieceRank) {
        // Knights can move in an L shape
        if (isSameColor(this, endPiece)) {
            return false;
        }
        if (Math.abs(endPieceFile - startPieceFile) == 2 && Math.abs(endPieceRank - startPieceRank) == 1
                || Math.abs(endPieceFile - startPieceFile) == 1 && Math.abs(endPieceRank - startPieceRank) == 2) {
            return true;
        }
        return false;
    }
}
