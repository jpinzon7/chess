package chess;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    public King(boolean isWhite, PieceFile file, int rank) {
        super(isWhite, file, rank);

        if (isWhite) {
            this.pieceType = PieceType.WK;
        } else {
            this.pieceType = PieceType.BK;
        }
    }

    List<ReturnPiece> piecesOnBoardCopy = new ArrayList<>(Chess.piecesOnBoard);

    public boolean isLegalMove(ReturnPiece endPiece, char startPieceFile, int startPieceRank, char endPieceFile,
            int endPieceRank) {
        // Calculate the file and rank differences
        int fileDifference = Math.abs(startPieceFile - endPieceFile);
        int rankDifference = Math.abs(startPieceRank - endPieceRank);

        // Check if the move is one square in any direction
        if (fileDifference > 1 || rankDifference > 1) { // king can only move 1
            return false;
        }

        // Check if the end position is occupied by a piece of the same color
        if (endPiece != null && endPiece.pieceType.toString().charAt(0) == (isWhite ? 'W' : 'B')) {
            return false;
        }

        // Check if the move puts the king in a check
        ReturnPiece tempEnd = Chess.board[endPieceRank - 1][endPieceFile - 'a'];
        ReturnPiece tempStart = Chess.board[startPieceRank - 1][startPieceFile - 'a'];
        piecesOnBoardCopy.remove(tempEnd);
        piecesOnBoardCopy.remove(tempStart);
        King newKing = new King(isWhite, PieceFile.valueOf(String.valueOf(endPieceFile)), endPieceRank);
        piecesOnBoardCopy.add(newKing);
        Chess.board[endPieceRank - 1][endPieceFile - 'a'] = newKing;
        Chess.board[startPieceRank - 1][startPieceFile - 'a'] = null;

        if (newKing.isInCheckCopy()) {
            piecesOnBoardCopy.remove(newKing);
            piecesOnBoardCopy.add(tempEnd);
            piecesOnBoardCopy.add(tempStart);
            Chess.board[endPieceRank - 1][endPieceFile - 'a'] = tempEnd;
            Chess.board[startPieceRank - 1][startPieceFile - 'a'] = tempStart;
            
            System.out.println("Cannot move into check");
            return false;
        }
        piecesOnBoardCopy.remove(newKing);
        piecesOnBoardCopy.add(tempEnd);
        piecesOnBoardCopy.add(tempStart);
        Chess.board[endPieceRank - 1][endPieceFile - 'a'] = tempEnd;
        Chess.board[startPieceRank - 1][startPieceFile - 'a'] = tempStart;

        return true;
    }

    // public ArrayList<ReturnPiece> threats = new ArrayList<>();

    public boolean isInCheck() {
        for (ReturnPiece piece : Chess.piecesOnBoard) {
            // Skip pieces of the same color
            if (piece.pieceType.toString().charAt(0) == this.pieceType.toString().charAt(0)) {
                continue;
            }
            if (piece.pieceFile.toString().charAt(0) == this.pieceFile.toString().charAt(0)
                    && piece.pieceRank == this.pieceRank) {
                continue;
            }

            // Check if the piece can move to the king's position
            switch (piece.pieceType.toString().charAt(1)) {
                case 'P':
                    if (((Pawn) piece).isLegalMove(this, piece.pieceFile.toString().charAt(0), piece.pieceRank,
                            this.pieceFile.toString().charAt(0), this.pieceRank)) {
                        // threats.add(piece);
                        return true;
                    }
                    break;
                case 'R':
                    if (((Rook) piece).isLegalMove(this, piece.pieceFile.toString().charAt(0), piece.pieceRank,
                            this.pieceFile.toString().charAt(0), this.pieceRank)) {
                        // threats.add(piece);
                        return true;
                    }
                    break;
                case 'N':
                    if (((Knight) piece).isLegalMove(this, piece.pieceFile.toString().charAt(0), piece.pieceRank,
                            this.pieceFile.toString().charAt(0), this.pieceRank)) {
                        // threats.add(piece);
                        return true;
                    }
                    break;
                case 'B':
                    if (((Bishop) piece).isLegalMove(this, piece.pieceFile.toString().charAt(0), piece.pieceRank,
                            this.pieceFile.toString().charAt(0), this.pieceRank)) {
                        // threats.add(piece);
                        return true;
                    }
                    break;
                case 'Q':
                    if (((Queen) piece).isLegalMove(this, piece.pieceFile.toString().charAt(0), piece.pieceRank,
                            this.pieceFile.toString().charAt(0), this.pieceRank)) {
                        // threats.add(piece);
                        return true;
                    }
                    break;
                case 'K':
                    if (((King) piece).isLegalMove(this, piece.pieceFile.toString().charAt(0), piece.pieceRank,
                            this.pieceFile.toString().charAt(0), this.pieceRank)) {
                        // threats.add(piece);
                        return true;
                    }
                    break;
            }
        }

        // if (threats.size() > 0) {
        // return true;
        // }
        return false;
    }

    public boolean isInCheckCopy() {
        for (ReturnPiece piece : piecesOnBoardCopy) {
            // Skip pieces of the same color
            if (piece.pieceType.toString().charAt(0) == this.pieceType.toString().charAt(0)) {
                continue;
            }
            if (piece.pieceFile.toString().charAt(0) == this.pieceFile.toString().charAt(0)
                    && piece.pieceRank == this.pieceRank) {
                continue;
            }
            if (piece.pieceType.toString().charAt(1) == 'K' && this.pieceType.toString().charAt(1) == 'K') {
                if (Math.abs(piece.pieceFile.ordinal() - this.pieceFile.ordinal()) <= 1
                        && Math.abs(piece.pieceRank - this.pieceRank) <= 1) {
                    return true;
                }
            }

            // Check if the piece can move to the king's position
            switch (piece.pieceType.toString().charAt(1)) {
                case 'P':
                    if (((Pawn) piece).isLegalMove(this, piece.pieceFile.toString().charAt(0), piece.pieceRank,
                            this.pieceFile.toString().charAt(0), this.pieceRank)) {
                        // threats.add(piece);
                        return true;
                    }
                    break;
                case 'R':
                    if (((Rook) piece).isLegalMove(this, piece.pieceFile.toString().charAt(0), piece.pieceRank,
                            this.pieceFile.toString().charAt(0), this.pieceRank)) {
                        // threats.add(piece);
                        return true;
                    }
                    break;
                case 'N':
                    if (((Knight) piece).isLegalMove(this, piece.pieceFile.toString().charAt(0), piece.pieceRank,
                            this.pieceFile.toString().charAt(0), this.pieceRank)) {
                        // threats.add(piece);
                        return true;
                    }
                    break;
                case 'B':
                    if (((Bishop) piece).isLegalMove(this, piece.pieceFile.toString().charAt(0), piece.pieceRank,
                            this.pieceFile.toString().charAt(0), this.pieceRank)) {
                        // threats.add(piece);
                        return true;
                    }
                    break;
                case 'Q':
                    if (((Queen) piece).isLegalMove(this, piece.pieceFile.toString().charAt(0), piece.pieceRank,
                            this.pieceFile.toString().charAt(0), this.pieceRank)) {
                        // threats.add(piece);
                        return true;
                    }
                    break;
                case 'K':
                    if (((King) piece).isLegalMove(this, piece.pieceFile.toString().charAt(0), piece.pieceRank,
                            this.pieceFile.toString().charAt(0), this.pieceRank)) {
                        // threats.add(piece);
                        return true;
                    }
                    break;
            }
        }

        // if (threats.size() > 0) {
        // return true;
        // }
        return false;
    }

    public boolean isInCheckMate() {
        // First check if the king can move to any square
        // and not be in check
        for (int file = -1; file <= 1; file++) {
            for (int rank = -1; rank <= 1; rank++) {
                if (file == 0 && rank == 0) {
                    continue;
                }
                if (this.pieceFile.ordinal() + file < 0 || this.pieceFile.ordinal() + file > 7
                        || this.pieceRank - 1 + rank < 0 || this.pieceRank - 1 + rank > 7) {
                    continue;
                }
                if (Chess.board[this.pieceRank - 1 + rank][this.pieceFile.ordinal() + file] == null) {
                    ReturnPiece temp = this;
                    Chess.piecesOnBoard.remove(this);
                    King newKing = new King(isWhite, PieceFile.values()[this.pieceFile.ordinal() + file],
                            this.pieceRank + rank);
                    Chess.piecesOnBoard.add(newKing);
                    if (!newKing.isInCheck()) {
                        Chess.piecesOnBoard.remove(newKing);
                        Chess.piecesOnBoard.add(temp);
                        return false;
                    }
                    Chess.piecesOnBoard.remove(newKing);
                    Chess.piecesOnBoard.add(temp);
                }
            }
        }

        // If king cannot move to any square
        // Find all the squares where the threat would be blocked
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // If there is an opposite color piece, save it and replace it with a temp pawn
                // of the same color as the king,
                // then check if the king is still in check, if not add that place on the board
                // to an arraylist
                // Next test if any piece of the same color as the king can move to that square
                if (Chess.board[i][j] == null) {
                    Chess.piecesOnBoard.add(new Pawn(isWhite, PieceFile.values()[j], i + 1));
                    Chess.board[i][j] = Chess.piecesOnBoard.get(Chess.piecesOnBoard.size() - 1);

                    if (!isInCheck()) {
                        // Remove the pawn and add the original piece back
                        Chess.piecesOnBoard.remove(Chess.piecesOnBoard.size() - 1);
                        Chess.board[i][j] = null;

                        for (ReturnPiece piece : Chess.piecesOnBoard) {
                            if (piece.pieceType.toString().charAt(0) == this.pieceType.toString().charAt(0) && piece != this) {
                                switch (piece.pieceType.toString().charAt(1)) {
                                    case 'P':
                                        if (((Pawn) piece).isLegalMove(
                                                Chess.piecesOnBoard.get(Chess.piecesOnBoard.size() - 1),
                                                piece.pieceFile.toString().charAt(0),
                                                piece.pieceRank, PieceFile.values()[j].toString().charAt(0), i + 1)) {
                                            return false;
                                        }
                                        break;
                                    case 'R':
                                        if (((Rook) piece).isLegalMove(
                                                Chess.piecesOnBoard.get(Chess.piecesOnBoard.size() - 1),
                                                piece.pieceFile.toString().charAt(0),
                                                piece.pieceRank, PieceFile.values()[j].toString().charAt(0), i + 1)) {
                                            return false;
                                        }
                                        break;
                                    case 'N':
                                        if (((Knight) piece).isLegalMove(
                                                Chess.piecesOnBoard.get(Chess.piecesOnBoard.size() - 1),
                                                piece.pieceFile.toString().charAt(0),
                                                piece.pieceRank, PieceFile.values()[j].toString().charAt(0), i + 1)) {
                                            return false;
                                        }
                                        break;
                                    case 'B':
                                        if (((Bishop) piece).isLegalMove(
                                                Chess.piecesOnBoard.get(Chess.piecesOnBoard.size() - 1),
                                                piece.pieceFile.toString().charAt(0),
                                                piece.pieceRank, PieceFile.values()[j].toString().charAt(0), i + 1)) {
                                            return false;
                                        }
                                        break;
                                    case 'Q':
                                        if (((Queen) piece).isLegalMove(
                                                Chess.piecesOnBoard.get(Chess.piecesOnBoard.size() - 1),
                                                piece.pieceFile.toString().charAt(0),
                                                piece.pieceRank, PieceFile.values()[j].toString().charAt(0), i + 1)) {
                                            return false;
                                        }
                                        break;
                                    case 'K':
                                        if (((King) piece).isLegalMove(
                                                Chess.piecesOnBoard.get(Chess.piecesOnBoard.size() - 1),
                                                piece.pieceFile.toString().charAt(0),
                                                piece.pieceRank, PieceFile.values()[j].toString().charAt(0), i + 1)) {
                                            return false;
                                        }
                                        break;
                                }
                            }
                        }
                    }
                    else {
                        Chess.piecesOnBoard.remove(Chess.piecesOnBoard.size() - 1);
                        Chess.board[i][j] = null;
                    }
                } else if (Chess.board[i][j].pieceType.toString().charAt(0) != this.pieceType.toString().charAt(0)) {
                    ReturnPiece temp = Chess.board[i][j];
                    Chess.piecesOnBoard.remove(temp);
                    Chess.piecesOnBoard.add(new Pawn(isWhite, PieceFile.values()[j], i + 1));
                    Chess.board[i][j] = Chess.piecesOnBoard.get(Chess.piecesOnBoard.size() - 1);

                    if (!isInCheck()) {
                        // Remove the pawn and add the original piece back
                        Chess.piecesOnBoard.remove(Chess.piecesOnBoard.size() - 1);
                        Chess.piecesOnBoard.add(temp);
                        Chess.board[i][j] = temp;

                        for (ReturnPiece piece : Chess.piecesOnBoard) {
                            if (piece.pieceType.toString().charAt(0) == this.pieceType.toString().charAt(0)) {
                                switch (piece.pieceType.toString().charAt(1)) {
                                    case 'P':
                                        if (((Pawn) piece).isLegalMove(temp, piece.pieceFile.toString().charAt(0),
                                                piece.pieceRank, PieceFile.values()[j].toString().charAt(0), i + 1)) {
                                            return false;
                                        }
                                        break;
                                    case 'R':
                                        if (((Rook) piece).isLegalMove(temp, piece.pieceFile.toString().charAt(0),
                                                piece.pieceRank, PieceFile.values()[j].toString().charAt(0), i + 1)) {
                                            return false;
                                        }
                                        break;
                                    case 'N':
                                        if (((Knight) piece).isLegalMove(temp, piece.pieceFile.toString().charAt(0),
                                                piece.pieceRank, PieceFile.values()[j].toString().charAt(0), i + 1)) {
                                            return false;
                                        }
                                        break;
                                    case 'B':
                                        if (((Bishop) piece).isLegalMove(temp, piece.pieceFile.toString().charAt(0),
                                                piece.pieceRank, PieceFile.values()[j].toString().charAt(0), i + 1)) {
                                            return false;
                                        }
                                        break;
                                    case 'Q':
                                        if (((Queen) piece).isLegalMove(temp, piece.pieceFile.toString().charAt(0),
                                                piece.pieceRank, PieceFile.values()[j].toString().charAt(0), i + 1)) {
                                            return false;
                                        }
                                        break;
                                    case 'K':
                                        if (((King) piece).isLegalMove(temp, piece.pieceFile.toString().charAt(0),
                                                piece.pieceRank, PieceFile.values()[j].toString().charAt(0), i + 1)) {
                                            return false;
                                        }
                                        break;
                                }
                            }
                        } // Iteration ends

                    } else {
                        Chess.piecesOnBoard.remove(Chess.piecesOnBoard.size() - 1);
                        Chess.piecesOnBoard.add(temp);
                        Chess.board[i][j] = temp;
                    }
                }
            }
        }

        return true;
    }
}