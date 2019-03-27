/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016-2017 Gary F. Pollice
 *******************************************************************************/
package cmv;

import cmv.ChessPiece.PieceColor;
import cmv.ChessPiece.PieceType;

/**
 * The MoveValidator has a single method that takes a ChessBoard instance
 * and two squares. It validates that the piece on the first square can move to
 * the second square on the given board.
 * 
 * Students must implement this method
 * 
 * @version Feb 15, 2019
 */
public class MoveValidator
{
	/**
	 * Determines if a move can be made
	 * @param board the board state
	 * @param from the square the piece is moving from
	 * @param to the square the piece is moving to
	 * @return true if the move can be made false otherwise
	 * @throws CMVException if there is an error, such as no piece on the from square
	 */
	public static boolean canMove(ChessBoard board, Square from, Square to)
	{
		if(board.getPieceAt(from) == null) return false;
		
		//Check to make sure we are not going off the board
		int row=to.getRow(), column=to.getColumn() - 'a' + 1;
		if((row > 8 || row < 0) || (column > 8 || column < 0)) return false;
		
		PieceType type = board.getPieceAt(from).getPieceType();
		
		switch(type) {
			case KING:
				return kingMoveValidator(board, from, to);
			case QUEEN:
				return queenMoveValidator(board, from, to);
			case BISHOP:
				return bishopMoveValidator(board, from, to);
			case KNIGHT:
				return knightMoveValidator(board, from, to);
			case ROOK:
				return rookMoveValidator(board, from, to);
			case PAWN:
				return pawnMoveValidator(board, from, to);
			default:
				return false;
		}
	}

	private static boolean kingMoveValidator(ChessBoard b, Square f, Square t){
		int yDiff = Math.abs(f.getRow() - t.getRow());
		int xDiff = Math.abs(f.getColumn() - t.getColumn());

		//Check to see if the move violates physical constraints
		if((yDiff == 0 && xDiff == 0) || ((yDiff > 1) || (xDiff > 1))) return false;

		//Check if one of it's own pieces is there
		if(b.isSquareOccupied(t)) {
			if(b.getPieceAt(t).getPieceColor() == b.getPieceAt(f).getPieceColor()) return false;
		}
		return true;
		//TODO: Make sure king doesn't move into check
	}

	private static boolean queenMoveValidator(ChessBoard b, Square f, Square t){
		int yDiff = Math.abs(f.getRow() - t.getRow());
		int xDiff = Math.abs(f.getColumn() - t.getColumn());

		//Make sure piece moved
		if(yDiff == 0 && xDiff == 0) return false;

		//If not going diagonally and not going straight
		if((yDiff != xDiff) && (yDiff != 0 && xDiff != 0)) return false;
		
		//Check that the piece did not go thru another piece
		//	probably also need to check whether it went thru an opponent
		//	maybe we can print something to the screen saying you must capture
		if(isMoveThruPiece(b, f, t, true)) return false;
		else return true;
	}

	private static boolean rookMoveValidator(ChessBoard b, Square f, Square t){
		int yDiff = Math.abs(f.getRow() - t.getRow());
		int xDiff = Math.abs(f.getColumn() - t.getColumn());

		//Make sure piece moved
		if(yDiff == 0 && xDiff == 0) return false;

		//Cannot go diagonally
		if(yDiff != 0 && xDiff != 0) return false;
		
		//Check that the piece did not go thru another piece
		if(isMoveThruPiece(b, f, t, false)) return false;
		else return true;
	}

	private static boolean bishopMoveValidator(ChessBoard b, Square f, Square t){
		int yDiff = Math.abs(f.getRow() - t.getRow());
		int xDiff = Math.abs(f.getColumn() - t.getColumn());

		//Make sure piece moved
		if(yDiff == 0 && xDiff == 0) return false;
		
		//Must move diagonally
		if((yDiff != xDiff)) return false;
		
		//Cannot move thru pieces
		if(isMoveThruPiece(b, f, t, false)) return false;
		else return true;
	}

	private static boolean knightMoveValidator(ChessBoard b, Square f, Square t){
		int yDiff = Math.abs(f.getRow() - t.getRow());
		int xDiff = Math.abs(f.getColumn() - t.getColumn());
		
		//Knight can only make an L shaped move
		if((yDiff == 2 && xDiff == 1) || (xDiff == 2 && yDiff == 1)) return true;
		else return false;
	}

	private static boolean pawnMoveValidator(ChessBoard b, Square f, Square t){
		int xDiff = Math.abs(f.getColumn() - t.getColumn());
		int yDiff = Math.abs(f.getRow() - t.getRow());
		PieceColor color = b.getPieceAt(f).getPieceColor();
		
		//Make sure piece moved
		if(yDiff == 0 && xDiff == 0) return false;
		
		boolean isStartingPosition;
		
		//Black pieces move down the board, white pieces move up
		if(color == ChessPiece.PieceColor.BLACK) {
			isStartingPosition = f.getRow() == 7;
			if(!isStartingPosition && (yDiff > 1)) return false;
			
			//If we are moving 'up' the board
			if((f.getRow() - t.getRow()) < 0) return false;
			
			//A pawn can only move diagonally if there is an opponent there
			// at this point we know that we will be moving only one square in the positive diagonal direction
			if(b.isSquareOccupied(t)) {
				if((color == b.getPieceAt(t).getPieceColor()) && (yDiff == xDiff)) return false;
			}
		}else {//White piece
			isStartingPosition = f.getRow() == 2;
			if(!isStartingPosition && (yDiff > 1)) return false;
			
			//If we are moving 'down' the board
			if((f.getRow() - t.getRow()) > 0) return false;
			
			//A pawn can only move diagonally if there is an opponent there
			// at this point we know that we will be moving only one square in the positive diagonal direction
			if(b.isSquareOccupied(t)) {
				if((color == b.getPieceAt(t).getPieceColor()) && (yDiff == xDiff)) return false;
			}
		}
		
		return true;
	}
	
	private static boolean isMoveThruPiece(ChessBoard b, Square f, Square t, boolean isQueen){
		//If we are going diagonally then neither the x nor y values will be the same in the squares
		boolean diagonal = ((f.getRow() != t.getRow()) && (f.getColumn() != t.getColumn()));
		int yDiff = f.getRow() - t.getRow();
		int xDiff = f.getColumn() - t.getColumn();
		//For the value of k we need to convert a-h to 1-8
		int row=f.getRow(), column=f.getColumn() - 'a' + 1;
		
		//If the square you are going to is occupied by own color piece move is invalid
		//if(b.getPieceAt(t).getPieceColor() == b.getPieceAt(f).getPieceColor()) return true;
		
		if(diagonal) {
			//The # of squares in a diagonal is equal to the length of it's x or y length
			int length = Math.abs(f.getRow() - t.getRow());
			for(int i=0; i<length; i++) {
				//Ternary operators to determine if we need to traverse in the -/+ x/y direction
				if((yDiff > 0) && (xDiff > 0)) { row++; column++; }
				else if((yDiff > 0) && (xDiff < 0)) { row++; column--; }
				else if((yDiff < 0) && (xDiff > 0)) { row--; column++; }
				else row--; column--;
				
				//If the square is occupied
				if(b.isSquareOccupied(new Square(mapToChar(column), row))) {
					if(!isQueen) return true; //If its not a queen then color doesn't matter
					//If it is a queen check the color if its the same return true
					else if (b.getPieceAt(new Square(mapToChar(column), row)).getPieceColor() == b.getPieceAt(f).getPieceColor()) return true;
				}
			}
		}else {//We are moving in a straight line
			//Set length to be the non-zero value
			int length = Math.max(Math.abs(yDiff), Math.abs(xDiff));
			for(int i=0; i<length; i++) {
				if((yDiff > 0) && (xDiff == 0)) row--;
				else if((yDiff < 0) && (xDiff == 0)) row++;
				else if((xDiff > 0) && (yDiff == 0)) column--;
				else column++;
				
				//If the square is occupied
				if(b.isSquareOccupied(new Square(mapToChar(column), row))) {
					if(!isQueen) return true; //If its not a queen then color doesn't matter
					//If it is a queen check the color if its the same return true
					else if (b.getPieceAt(new Square(mapToChar(column), row)).getPieceColor() == b.getPieceAt(f).getPieceColor()) return true;
				}
			}
		}
		return false;
	}
	
	private static char mapToChar(int i) {
		//'a' in ascii is 97, then we subtract 2 because of zero-based indexing
		return (char)(i + 97 - 1);
	}
}



























