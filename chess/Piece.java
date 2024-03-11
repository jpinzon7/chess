package chess;

public abstract class Piece extends ReturnPiece {
    // abstract class
    public boolean isWhite;
    public boolean hasMoved;

    public Piece(boolean isWhite, PieceFile file, int rank) {
        this.isWhite = isWhite;
        this.pieceFile = file;
        this.pieceRank = rank;
    }

    public abstract boolean isLegalMove(ReturnPiece endPiece, char startPieceFile, int startPieceRank, char endPieceFile, int endPieceRank);

    public static boolean isSameColor(ReturnPiece startPiece, ReturnPiece endPiece) {
        if (endPiece != null && startPiece.pieceType.name().charAt(0) == endPiece.pieceType.name().charAt(0)) {
            return true;
        }
        return false;
    }

    public boolean isKingSafeAfterMove(ReturnPiece endPiece, char startPieceFile, int startPieceRank, char endPieceFile, int endPieceRank) {
        // // Save the current state of the board
        // ArrayList<ReturnPiece> savedPiecesOnBoard = new ArrayList<>(Chess.piecesOnBoard);
    
        // // Simulate the move
        // this.pieceFile = PieceFile.valueOf(String.valueOf(endPieceFile));
        // this.pieceRank = endPieceRank;
    
        // // Check if the king of the current player is in check
        // King king = Chess.getKing(this.isWhite);
        // boolean kingInCheck = king.isInCheck();
    
        // // Revert the board to the saved state
        // Chess.piecesOnBoard = savedPiecesOnBoard;
    
        // return !kingInCheck;

        // Check if pawn is en passant
        boolean isEnPassant = false;
        ReturnPiece enPassantPiece = null;
        if (this.pieceType == PieceType.WP || this.pieceType == PieceType.BP) {
            if (startPieceFile != endPieceFile && endPiece == null) {
                if (this.isWhite) {
                    enPassantPiece = Chess.board[endPieceRank - 2][endPieceFile - 'a'];
                    Chess.piecesOnBoard.remove(Chess.board[endPieceRank - 2][endPieceFile - 'a']);
                    Chess.board[endPieceRank - 2][endPieceFile - 'a'] = null;
                }
                else {
                    enPassantPiece = Chess.board[endPieceRank][endPieceFile - 'a'];
                    Chess.piecesOnBoard.remove(Chess.board[endPieceRank][endPieceFile - 'a']);
                    Chess.board[endPieceRank][endPieceFile - 'a'] = null;
                }
                isEnPassant = true;
            }
        }

        // Assign the startPiece to the endPiece file and rank
        this.pieceFile = PieceFile.valueOf(String.valueOf(endPieceFile));
        this.pieceRank = endPieceRank;

        // Update board
        Chess.board[endPieceRank - 1][endPieceFile - 'a'] = this;
        Chess.board[startPieceRank - 1][startPieceFile - 'a'] = null;

        // if (endPiece != null) {
            Chess.piecesOnBoard.remove(endPiece);
        // }

        // Check if the king of the current player is in check
        King king = Chess.getKing(this.isWhite); //works
        boolean kingInCheck = king.isInCheck();

        // Revert the board to the original state
        this.pieceFile = PieceFile.valueOf(String.valueOf(startPieceFile));
        this.pieceRank = startPieceRank;
        Chess.board[startPieceRank - 1][startPieceFile - 'a'] = this;
        Chess.board[endPieceRank - 1][endPieceFile - 'a'] = endPiece;

        if (isEnPassant) {
            if (this.isWhite) {
                Chess.board[endPieceRank - 2][endPieceFile - 'a'] = enPassantPiece;
                Chess.piecesOnBoard.add(enPassantPiece);
            }
            else {
                Chess.board[endPieceRank][endPieceFile - 'a'] = enPassantPiece;
                Chess.piecesOnBoard.add(enPassantPiece);
            }
        }

        if (endPiece != null) {
            Chess.piecesOnBoard.add(endPiece);
        }

        if (kingInCheck) {
            System.out.println("You cannot place the king in check");
        }

        return !kingInCheck;
    }
}
