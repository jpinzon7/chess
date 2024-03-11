package chess;

public class Pawn extends Piece {
    public Pawn(boolean isWhite, PieceFile file, int rank) {
        super(isWhite, file, rank);

        if (isWhite) {
            this.pieceType = PieceType.WP;
        } else {
            this.pieceType = PieceType.BP;
        }

    }

    public boolean isFirstMove = true;
    public int numMoves = 0;
    public int turnMoved = 0;

    @Override
    public boolean isLegalMove(ReturnPiece endPiece, char startPieceFile, int startPieceRank, char endPieceFile,
            int endPieceRank) {
        // Pawns can move one square forward or two squares forward if it's their first
        // move
        if (endPiece == null) { // If the move is to an empty square
            if (isFirstMove) {
                if (startPieceFile == endPieceFile) { // Has to be in the same column
                    if (isWhite) { // White
                        if (endPieceRank == startPieceRank + 1 || endPieceRank == startPieceRank + 2) {
                            return true;
                        }
                    } else { // Black
                        if (endPieceRank == startPieceRank - 1 || endPieceRank == startPieceRank - 2) {
                            return true;
                        }
                    }
                }
            } else { // if not first move
                if (startPieceFile == endPieceFile) { // Has to be in the same column
                    if (isWhite) { // White
                        if (endPieceRank == startPieceRank + 1) {
                            return true;
                        }
                    } else { // Black
                        if (endPieceRank == startPieceRank - 1) {
                            return true;
                        }
                    }
                } else { // En passant
                    if (isWhite && startPieceRank == 5 && endPieceRank == 6) {
                        if (endPieceFile == startPieceFile + 1 || endPieceFile == startPieceFile - 1) {
                            if (Chess.board[startPieceRank - 1][endPieceFile - 'a'] != null
                                    && Chess.board[startPieceRank - 1][endPieceFile - 'a'].pieceType == PieceType.BP) {
                                Pawn removingPawn = (Pawn) Chess.board[startPieceRank - 1][endPieceFile - 'a'];
                                if (removingPawn.numMoves == 1 && removingPawn.turnMoved == Chess.turnCount) {
                                    return true;
                                }
                            }
                        }
                    }
                    if (!isWhite && startPieceRank == 4 && endPieceRank == 3) {
                        if (endPieceFile == startPieceFile + 1 || endPieceFile == startPieceFile - 1) {
                            if (Chess.board[startPieceRank - 1][endPieceFile - 'a'] != null
                                    && Chess.board[startPieceRank - 1][endPieceFile - 'a'].pieceType == PieceType.WP) {
                                Pawn removingPawn = (Pawn) Chess.board[startPieceRank + 1][endPieceFile - 'a'];
                                if (removingPawn.numMoves == 1 && removingPawn.turnMoved == Chess.turnCount) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        } else { // If the move is to a square with a piece
            if (this.pieceType.name().charAt(0) == endPiece.pieceType.name().charAt(0)) {
                return false;
            }
            if (isWhite) {
                if (endPieceRank == startPieceRank + 1
                        && (endPieceFile == startPieceFile + 1 || endPieceFile == startPieceFile - 1)) {
                    return true;
                }
            } else {
                if (endPieceRank == startPieceRank - 1
                        && (endPieceFile == startPieceFile + 1 || endPieceFile == startPieceFile - 1)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean canBePromoted() {
        if (isWhite) {
            if (pieceRank == 8) {
                return true;
            }
        } else {
            if (pieceRank == 1) {
                return true;
            }
        }
        return false;
    }

    public void promote(char pieceType) {
        ReturnPiece promotedPiece = null;

        switch (pieceType) {
            case 'Q':
                if (isWhite) {
                    promotedPiece = new Queen(true, pieceFile, pieceRank);
                } else {
                    promotedPiece = new Queen(false, pieceFile, pieceRank);
                }
                break;
            case 'R':
                if (isWhite) {
                    promotedPiece = new Rook(true, pieceFile, pieceRank);
                } else {
                    promotedPiece = new Rook(false, pieceFile, pieceRank);
                }
                break;
            case 'B':
                if (isWhite) {
                    promotedPiece = new Bishop(true, pieceFile, pieceRank);
                } else {
                    promotedPiece = new Bishop(false, pieceFile, pieceRank);
                }
                break;
            case 'N':
                if (isWhite) {
                    promotedPiece = new Knight(true, pieceFile, pieceRank);
                } else {
                    promotedPiece = new Knight(false, pieceFile, pieceRank);
                }
                break;
        }

        Chess.board[pieceRank - 1][pieceFile.ordinal()] = promotedPiece;
        Chess.piecesOnBoard.add(promotedPiece);
        Chess.piecesOnBoard.remove(this);
    }
}
