package chess;

public class Queen extends Piece {
    public Queen(boolean isWhite, PieceFile file, int rank) {
        super(isWhite, file, rank);

        if (isWhite) {
            this.pieceType = PieceType.WQ;
        } else {
            this.pieceType = PieceType.BQ;
        }

    }

    @Override
    public boolean isLegalMove(ReturnPiece endPiece, char startPieceFile, int startPieceRank, char endPieceFile,
            int endPieceRank) {
        // Queen can move diagonally or straight
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

        if (startPieceFile != endPieceFile && startPieceRank != endPieceRank) {
            return false;
        }

        if (endPieceFile - startPieceFile == 0) {
            int rankDirection = endPieceRank - startPieceRank > 0 ? 1 : -1;
            int rank = startPieceRank + rankDirection;
            while (rank != endPieceRank) {
                if (Chess.board[rank - 1][startPieceFile - 'a'] != null) {
                    return false;
                }
                rank += rankDirection;
            }
            return true;
        } else if (endPieceRank - startPieceRank == 0) {
            int fileDirection = endPieceFile - startPieceFile > 0 ? 1 : -1;
            int file = startPieceFile + fileDirection;
            while (file != endPieceFile) {
                if (Chess.board[startPieceRank - 1][file - 'a'] != null) {
                    return false;
                }
                file += fileDirection;
            }
            return true;
        }

        return false;
    }
}

