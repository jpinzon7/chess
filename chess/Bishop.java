package chess;

public class Bishop extends Piece {
    public Bishop(boolean isWhite, PieceFile file, int rank) {
        super(isWhite, file, rank);

        if (isWhite) {
            this.pieceType = PieceType.WB;
        } else {
            this.pieceType = PieceType.BB;
        }
    }

    @Override
    public boolean isLegalMove(ReturnPiece endPiece, char startPieceFile, int startPieceRank, char endPieceFile,
            int endPieceRank) {
        // Bishop can move diagonally
        if (isSameColor(this, endPiece)) {
            return false;
        }
        if (Math.abs(endPieceFile - startPieceFile) == Math.abs(endPieceRank - startPieceRank)) {
            int fileDirection = endPieceFile - startPieceFile > 0 ? 1 : -1;
            int rankDirection = endPieceRank - startPieceRank > 0 ? 1 : -1;

            int file = startPieceFile + fileDirection;
            int rank = startPieceRank + rankDirection;

            while (file != endPieceFile && rank != endPieceRank) {
                if (Chess.board[rank - 1][file - 'a'] != null) {
                    return false;
                }
                file += fileDirection;
                rank += rankDirection;
            }
            return true;
        }
        return false;
    }
}
