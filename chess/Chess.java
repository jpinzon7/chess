// Jorge Pinzon and Maxim Vyshnevsky

package chess;

import java.util.ArrayList;

class ReturnPiece {
	static enum PieceType {
		WP, WR, WN, WB, WQ, WK,
		BP, BR, BN, BB, BK, BQ
	}; // P = pawn, R = rook, N = knight, B = bishop, Q = queen, K = king

	static enum PieceFile {
		a, b, c, d, e, f, g, h
	};

	PieceType pieceType;
	PieceFile pieceFile;
	int pieceRank; // 1..8

	public String toString() {
		return "" + pieceFile + pieceRank + ":" + pieceType;
	}

	public boolean equals(Object other) {
		if (other == null || !(other instanceof ReturnPiece)) {
			return false;
		}
		ReturnPiece otherPiece = (ReturnPiece) other;
		return pieceType == otherPiece.pieceType &&
				pieceFile == otherPiece.pieceFile &&
				pieceRank == otherPiece.pieceRank;
	}
}

class ReturnPlay {
	enum Message {
		ILLEGAL_MOVE, DRAW,
		RESIGN_BLACK_WINS, RESIGN_WHITE_WINS,
		CHECK, CHECKMATE_BLACK_WINS, CHECKMATE_WHITE_WINS,
		STALEMATE
	};

	ArrayList<ReturnPiece> piecesOnBoard;
	Message message;
}

public class Chess {

	enum Player {
		white, black
	}

	/**
	 * Plays the next move for whichever player has the turn.
	 * 
	 * @param move String for next move, e.g. "a2 a3"
	 * 
	 * @return A ReturnPlay instance that contains the result of the move.
	 *         See the section "The Chess class" in the assignment description for
	 *         details of
	 *         the contents of the returned ReturnPlay instance.
	 */

	public static ArrayList<ReturnPiece> piecesOnBoard; // arraylist of the pieces on the board
	public static ReturnPiece[][] board; // array of the acttual board
	public static Player turn; // white or black
	public static ReturnPlay res = new ReturnPlay(); // stores the result of the move
	public static int turnCount = 0; // counts the number of turns

	public static void movePiece(ReturnPiece startPiece, ReturnPiece endPiece, char endLetter, int endNumber) {
		turnCount++;

		if (startPiece instanceof Pawn) {
			((Pawn) startPiece).numMoves++;
			((Pawn) startPiece).isFirstMove = false;
			((Pawn) startPiece).turnMoved = turnCount;

			if (!(startPiece.pieceFile.toString().equals(String.valueOf(endLetter))) && endPiece == null) { // En passant
				if (startPiece.pieceType == ReturnPiece.PieceType.WP) {
					endPiece = board[endNumber - 2][endLetter - 'a'];
					board[endNumber - 2][endLetter - 'a'] = null;
				} else {
					endPiece = board[endNumber][endLetter - 'a'];
					board[endNumber][endLetter - 'a'] = null;
				}
				piecesOnBoard.remove(endPiece);
				endPiece = null;
			}
		}
		if (startPiece instanceof King) {
			((King) startPiece).hasMoved = true;
		}
		if (startPiece instanceof Rook) {
			((Rook) startPiece).hasMoved = true;
		}

		// Update board
		board[endNumber - 1][endLetter - 'a'] = startPiece;
		board[startPiece.pieceRank - 1][startPiece.pieceFile.ordinal()] = null;

		// Move the piece from the start position to the end position
		if (endPiece != null) { // if there is a piece at end, change file and rank of start piece to end piece
			startPiece.pieceFile = endPiece.pieceFile;
			startPiece.pieceRank = endPiece.pieceRank;
			// Remove the piece at the end position
			piecesOnBoard.remove(endPiece);
		} else {
			startPiece.pieceFile = ReturnPiece.PieceFile.valueOf(String.valueOf(endLetter));
			startPiece.pieceRank = endNumber;
		}

		// TESTING
		// for (int r = 7; r >= 0; r--) {
		// 	for (int c = 0; c < 8; c++) {
		// 		if (board[r][c] == null) {
		// 			System.out.print("__ ");
		// 		} else {
		// 			System.out.print(board[r][c].pieceType + " ");
		// 		}
		// 	}
		// 	System.out.println();
		// }
	}

	public static boolean startVerify(ReturnPiece startPiece) { // checks if the piece you want to move is valid
		// Check if the start position is empty
		if (startPiece == null) {
			res.message = ReturnPlay.Message.ILLEGAL_MOVE;
			return false;
		}

		// Check if the piece at the start position is the same color as the turn
		if (turn == Player.white && startPiece.pieceType.toString().charAt(0) == 'B') {
			res.message = ReturnPlay.Message.ILLEGAL_MOVE;
			return false;
		} else if (turn == Player.black && startPiece.pieceType.toString().charAt(0) == 'W') {
			res.message = ReturnPlay.Message.ILLEGAL_MOVE;
			return false;
		}

		return true;
	}

	public static boolean performCastlingIfValid(ReturnPiece startPiece, char endLetter, int endNumber) {
		// Check if the start piece is a king
		if (!(startPiece.pieceType == ReturnPiece.PieceType.WK || startPiece.pieceType == ReturnPiece.PieceType.BK)) {
			return false;
		}

		// Check if the king is in check
		if (((King) startPiece).isInCheck()) {
			return false;
		}

		// Check if the king and rook have not moved before
		if (!(startPiece instanceof Piece) || ((Piece) startPiece).hasMoved) {
			return false;
		}

		ReturnPiece rook;
		boolean isKingside = false;
		if ((endLetter - 'a' - 1) > startPiece.pieceFile.ordinal()) {
			// Kingside castling
			rook = board[startPiece.pieceRank - 1]['h' - 'a'];
			isKingside = true;
		} else {
			// Queenside castling
			rook = board[startPiece.pieceRank - 1]['a' - 'a'];
		}

		if (rook == null || !(rook instanceof Piece) || ((Piece) rook).hasMoved) {
			return false;
		}

		// Check if the path between the king and the rook is clear
		int startFile = Math.min(startPiece.pieceFile.ordinal(), rook.pieceFile.ordinal());
		int endFile = Math.max(startPiece.pieceFile.ordinal(), rook.pieceFile.ordinal());
		for (int file = startFile + 1; file < endFile; file++) {
			if (board[startPiece.pieceRank - 1][file] != null) {
				return false;
			}
		}

		// Check if the path the king moves through is safe
		char[] kingPath;
		if (isKingside) {
			// Kingside castling
			kingPath = new char[]{'f', 'g'};
		} else {
			// Queenside castling
			kingPath = new char[]{'d', 'c'};
		}
	
		for (char file : kingPath) {
			// Check if the square is threatened
			if (!((Piece) startPiece).isKingSafeAfterMove(null, startPiece.pieceFile.name().charAt(0), startPiece.pieceRank, file, startPiece.pieceRank)) {
				return false;
			}
		}



		// Check if the king is in check after move
		boolean isKingChecked = false;
		King king = getKing(turn == Player.white);
		if (isKingside) {
			// Set the king's and rook's places
			king.pieceFile = ReturnPiece.PieceFile.g;
			rook.pieceFile = ReturnPiece.PieceFile.f;
			// Update it on the board
			board[startPiece.pieceRank - 1]['g' - 'a'] = startPiece;
			board[startPiece.pieceRank - 1]['e' - 'a'] = null;
			board[startPiece.pieceRank - 1]['f' - 'a'] = rook;
			board[startPiece.pieceRank - 1]['h' - 'a'] = null;
			// Check if the king is in check
			if (king.isInCheck()) {
				isKingChecked = true;
			}
			// Revert the piecefile
			king.pieceFile = ReturnPiece.PieceFile.e;
			rook.pieceFile = ReturnPiece.PieceFile.h;
			// Revert the board
			board[startPiece.pieceRank - 1]['e' - 'a'] = startPiece;
			board[startPiece.pieceRank - 1]['g' - 'a'] = null;
			board[startPiece.pieceRank - 1]['f' - 'a'] = null;
			board[startPiece.pieceRank - 1]['h' - 'a'] = rook;
			if (isKingChecked) {
				return false;
			}
		} else {
			// Set the king's and rook's places
			king.pieceFile = ReturnPiece.PieceFile.c;
			rook.pieceFile = ReturnPiece.PieceFile.d;
			// Update it on the board
			board[startPiece.pieceRank - 1]['c' - 'a'] = startPiece;
			board[startPiece.pieceRank - 1]['e' - 'a'] = null;
			board[startPiece.pieceRank - 1]['d' - 'a'] = rook;
			board[startPiece.pieceRank - 1]['a' - 'a'] = null;
			// Check if the king is in check
			if (king.isInCheck()) {
				isKingChecked = true;
			}
			// Revert the piecefile
			king.pieceFile = ReturnPiece.PieceFile.e;
			rook.pieceFile = ReturnPiece.PieceFile.a;
			// Revert the board
			board[startPiece.pieceRank - 1]['e' - 'a'] = startPiece;
			board[startPiece.pieceRank - 1]['c' - 'a'] = null;
			board[startPiece.pieceRank - 1]['d' - 'a'] = null;
			board[startPiece.pieceRank - 1]['a' - 'a'] = rook;
			if (isKingChecked) {
				return false;
			}
		}

		// Perform the castling move
		// Move the king
		movePiece(startPiece, null, endLetter, endNumber);
		if (startPiece instanceof Piece) {
			((Piece) startPiece).hasMoved = true;
		}

		// Move the rook
		if (isKingside) {
			// Kingside castling
			movePiece(rook, null, (char) (endLetter - 1), endNumber);
		} else {
			// Queenside castling
			movePiece(rook, null, (char) (endLetter + 1), endNumber);
		}
		if (rook instanceof Piece) {
			((Piece) rook).hasMoved = true;
		}

		return true;
	}

	public static King getKing(boolean isWhite) { // gets the king of the specified color
		for (ReturnPiece piece : piecesOnBoard) {
			if ((piece.pieceType == ReturnPiece.PieceType.WK && isWhite) ||
					(piece.pieceType == ReturnPiece.PieceType.BK && !isWhite)) {
				if (piece instanceof King) {
					return (King) piece;
				}
			}
		}
		return null; // Return null if no king of the specified color is found
	}

	public static boolean checkStatus(boolean isWhite) { // checks if the king is in check
		boolean isOtherColor = turn != Player.white;
		King king = getKing(isOtherColor);
		if (king.isInCheck()) {
			if (king.isInCheckMate()) {
				res.message = (isOtherColor ? ReturnPlay.Message.CHECKMATE_BLACK_WINS
						: ReturnPlay.Message.CHECKMATE_WHITE_WINS);
				System.out.println((isOtherColor ? "White" : "Black") + " King is in checkmate!");
			} else {
				res.message = ReturnPlay.Message.CHECK;
				System.out.println((isOtherColor ? "White" : "Black") + " King is in check!");
			}
			return true;
		}

		return false;
	}

	public static ReturnPlay makeMove(String move) {
		char startLetter = move.charAt(0);
		int startNumber = Integer.parseInt(move.substring(1, 2));
		char endLetter = move.charAt(3);
		int endNumber = Integer.parseInt(move.substring(4, 5));

		ReturnPiece startPiece = board[startNumber - 1][startLetter - 'a'];
		// Run checks on the start position
		if (!startVerify(startPiece)) {
			res.piecesOnBoard = piecesOnBoard;
			return res;
		}

		// Get the piece at the end position from the board
		ReturnPiece endPiece = board[endNumber - 1][endLetter - 'a'];

		switch (startPiece.pieceType.toString().charAt(1)) {
			case 'P':
				// Handle pawn move
				Pawn played = (Pawn) startPiece;
				if (played.isLegalMove(endPiece, startLetter, startNumber, endLetter, endNumber)) {
					if (!played.isKingSafeAfterMove(endPiece, startLetter, startNumber, endLetter, endNumber)) {
						res.message = ReturnPlay.Message.ILLEGAL_MOVE;
						res.piecesOnBoard = piecesOnBoard;
						return res;
					}
					movePiece(startPiece, endPiece, endLetter, endNumber);
					// Check if the pawn can be promoted
					if (played.canBePromoted()) {
						char promotion;
						if (move.length() > 6) {
							promotion = move.charAt(6);
						} else {
							promotion = 'Q';
						}
						played.promote(promotion);
					}
				} else {
					res.message = ReturnPlay.Message.ILLEGAL_MOVE;
					res.piecesOnBoard = piecesOnBoard;
					return res;
				}
				break;
			case 'R':
				Rook playedRook = (Rook) startPiece;
				if (playedRook.isLegalMove(endPiece, startLetter, startNumber, endLetter, endNumber)) {
					if (!playedRook.isKingSafeAfterMove(endPiece, startLetter, startNumber, endLetter, endNumber)) {
						res.message = ReturnPlay.Message.ILLEGAL_MOVE;
						res.piecesOnBoard = piecesOnBoard;
						return res;
					}
					movePiece(startPiece, endPiece, endLetter, endNumber);
				} else {
					res.message = ReturnPlay.Message.ILLEGAL_MOVE;
					res.piecesOnBoard = piecesOnBoard;
					return res;
				}
				break;
			case 'N':
				// Handle knight move
				Knight playedKnight = (Knight) startPiece;
				if (playedKnight.isLegalMove(endPiece, startLetter, startNumber, endLetter, endNumber)) {
					if (!playedKnight.isKingSafeAfterMove(endPiece, startLetter, startNumber, endLetter, endNumber)) {
						res.message = ReturnPlay.Message.ILLEGAL_MOVE;
						res.piecesOnBoard = piecesOnBoard;
						return res;
					}
					movePiece(startPiece, endPiece, endLetter, endNumber);
				} else {
					res.message = ReturnPlay.Message.ILLEGAL_MOVE;
					res.piecesOnBoard = piecesOnBoard;
					return res;
				}
				break;
			case 'B':
				// Handle bishop move
				Bishop playedBishop = (Bishop) startPiece;
				if (playedBishop.isLegalMove(endPiece, startLetter, startNumber, endLetter, endNumber)) {
					if (!playedBishop.isKingSafeAfterMove(endPiece, startLetter, startNumber, endLetter, endNumber)) {
						res.message = ReturnPlay.Message.ILLEGAL_MOVE;
						res.piecesOnBoard = piecesOnBoard;
						return res;
					}
					movePiece(startPiece, endPiece, endLetter, endNumber);
				} else {
					res.message = ReturnPlay.Message.ILLEGAL_MOVE;
					res.piecesOnBoard = piecesOnBoard;
					return res;
				}
				break;
			case 'Q':
				// Handle queen move
				Queen playedQueen = (Queen) startPiece;
				if (playedQueen.isLegalMove(endPiece, startLetter, startNumber, endLetter, endNumber)) {
					if (!playedQueen.isKingSafeAfterMove(endPiece, startLetter, startNumber, endLetter, endNumber)) {
						res.message = ReturnPlay.Message.ILLEGAL_MOVE;
						res.piecesOnBoard = piecesOnBoard;
						return res;
					}
					movePiece(startPiece, endPiece, endLetter, endNumber);
				} else {
					res.message = ReturnPlay.Message.ILLEGAL_MOVE;
					res.piecesOnBoard = piecesOnBoard;
					return res;
				}
				break;
			case 'K':
				King playedKing = (King) startPiece;
				if (playedKing.isLegalMove(endPiece, startLetter, startNumber, endLetter, endNumber)) {
					movePiece(startPiece, endPiece, endLetter, endNumber);
				} else if (performCastlingIfValid(startPiece, endLetter, endNumber)) {
					// Castling move was performed
				} else {
					res.message = ReturnPlay.Message.ILLEGAL_MOVE;
					res.piecesOnBoard = piecesOnBoard;
					return res;
				}
				break;
		}

		if (checkStatus(turn == Player.white)) {
			res.piecesOnBoard = piecesOnBoard;
			turn = (turn == Player.white) ? Player.black : Player.white;
			System.out.println(turn + " to move");
			return res;
		} // check if the king is in check

		// Change the turn to the other player
		turn = (turn == Player.white) ? Player.black : Player.white;
		System.out.println(turn + " to move");

		res.message = null;
		res.piecesOnBoard = piecesOnBoard;
		return res;
	}

	public static boolean canPawnBePromoted(String move) {
		char startLetter = move.charAt(0);
		int startNumber = Integer.parseInt(move.substring(1, 2));

		ReturnPiece startPiece = board[startNumber - 1][startLetter - 'a'];

		if (startPiece.pieceType == ReturnPiece.PieceType.WP && startPiece.pieceRank == 7) {
			return true;
		}
		if (startPiece.pieceType == ReturnPiece.PieceType.BP && startPiece.pieceRank == 2) {
			return true;
		}

		return false;
	}

	public static ReturnPlay play(String move) {

		/* FILL IN THIS METHOD */
		move = move.trim();
		// if move is "resign" return the appropriate message
		if (move.equals("resign")) {
			res.message = (turn == Player.white) ? ReturnPlay.Message.RESIGN_BLACK_WINS
					: ReturnPlay.Message.RESIGN_WHITE_WINS;
			res.piecesOnBoard = piecesOnBoard;
			return res;
		} else if (move.matches("[a-h][1-8] [a-h][1-8]")) {
			return makeMove(move);
		} else if (move.matches("[a-h][1-8] [a-h][1-8] draw\\?")) {
			makeMove(move);
			if (res.message == null) {
				res.message = ReturnPlay.Message.DRAW;
			}
			return res;
		} else if (move.matches("[a-h][1-8] [a-h][1-8] [QNRB]")) {
			if (canPawnBePromoted(move)) {
				return makeMove(move);
			} else {
				res.message = ReturnPlay.Message.ILLEGAL_MOVE;
				return res;
			}
		} else {
			res.message = ReturnPlay.Message.ILLEGAL_MOVE;
			res.piecesOnBoard = piecesOnBoard;
			return res;
		}

		/* FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY */
		/* WHEN YOU FILL IN THIS METHOD, YOU NEED TO RETURN A ReturnPlay OBJECT */
		// res.message = null;
		// res.piecesOnBoard = piecesOnBoard;
		// return res;
	}

	/**
	 * This method should reset the game, and start from scratch.
	 */
	public static void start() {
		/* FILL IN THIS METHOD */
		piecesOnBoard = new ArrayList<ReturnPiece>();

		// Add all the white pieces to piecesOnBoard
		piecesOnBoard.add(new Rook(true, ReturnPiece.PieceFile.a, 1));
		piecesOnBoard.add(new Knight(true, ReturnPiece.PieceFile.b, 1));
		piecesOnBoard.add(new Bishop(true, ReturnPiece.PieceFile.c, 1));
		piecesOnBoard.add(new Queen(true, ReturnPiece.PieceFile.d, 1));
		piecesOnBoard.add(new King(true, ReturnPiece.PieceFile.e, 1));
		piecesOnBoard.add(new Bishop(true, ReturnPiece.PieceFile.f, 1));
		piecesOnBoard.add(new Knight(true, ReturnPiece.PieceFile.g, 1));
		piecesOnBoard.add(new Rook(true, ReturnPiece.PieceFile.h, 1));
		for (int i = 0; i < 8; i++) {
			piecesOnBoard.add(new Pawn(true, ReturnPiece.PieceFile.values()[i], 2));
		}

		// Add all the black pieces to piecesOnBoard
		piecesOnBoard.add(new Rook(false, ReturnPiece.PieceFile.a, 8));
		piecesOnBoard.add(new Knight(false, ReturnPiece.PieceFile.b, 8));
		piecesOnBoard.add(new Bishop(false, ReturnPiece.PieceFile.c, 8));
		piecesOnBoard.add(new Queen(false, ReturnPiece.PieceFile.d, 8));
		piecesOnBoard.add(new King(false, ReturnPiece.PieceFile.e, 8));
		piecesOnBoard.add(new Bishop(false, ReturnPiece.PieceFile.f, 8));
		piecesOnBoard.add(new Knight(false, ReturnPiece.PieceFile.g, 8));
		piecesOnBoard.add(new Rook(false, ReturnPiece.PieceFile.h, 8));
		for (int i = 0; i < 8; i++) {
			piecesOnBoard.add(new Pawn(false, ReturnPiece.PieceFile.values()[i], 7));
		}

		// Print the board
		PlayChess.printBoard(piecesOnBoard);

		// Make a 2D array of the board
		board = new Piece[8][8];
		for (ReturnPiece piece : piecesOnBoard) {
			board[piece.pieceRank - 1][piece.pieceFile.ordinal()] = piece;
		}

		// TESTING
		// Print the 2D array of the board, print out the piece type
		// for (int r = 0; r < 8; r++) {
		// for (int c = 0; c < 8; c++) {
		// if (board[r][c] == null) {
		// System.out.print("__ ");
		// } else {
		// System.out.print(board[r][c].pieceType + " ");
		// }
		// }
		// System.out.println();
		// }

		// Set the turn to white
		turn = Player.white;
		System.out.println(turn + " to move");
	}
}
