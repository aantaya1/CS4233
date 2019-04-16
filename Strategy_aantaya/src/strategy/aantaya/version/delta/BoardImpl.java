/**
 * 
 */
package strategy.aantaya.version.delta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import strategy.Board;
import strategy.Piece;
import strategy.Piece.PieceColor;
import strategy.Piece.PieceType;
import strategy.aantaya.PieceImpl;
import strategy.aantaya.Square;

/**
 * @author Owner
 *
 */
public class BoardImpl implements Board {

	private Map<Square, Piece> theBoard;
	private static final List<Square> chokePoints = Arrays.asList(
			new Square(4,2), new Square(4,3), new Square(5,2), new Square(5,3),
			new Square(4,6), new Square(4,7), new Square(5,6), new Square(5,7)); 
	
	//Constants
	private static final int MAX_ROWS = 9;
	private static final int MAX_COLUMNS = 9;
	
	/**
	 * Converting a board that implements the Board interface to my specific implementation
	 */
	public BoardImpl(Board b) {
		theBoard = new HashMap<Square, Piece>();
		
		//Populate red team
		for(int row=0; row<=3; row++) {
			for(int column=0; column<=MAX_COLUMNS; column++) {
				//Convert the board to our implementation, including converting each piece to our
				//	implementation using copy constructor				
				theBoard.put(new Square(row, column), 
						new PieceImpl(b.getPieceAt(row, column)));
			}
		}
		
		//Populate blue team
		for(int row=6; row<=MAX_ROWS; row++) {
			for(int column=0; column<=MAX_COLUMNS; column++) {			
				theBoard.put(new Square(row, column), 
						new PieceImpl(b.getPieceAt(row, column)));
			}
		}
	}
	
	public void movePiece(Square from, Square to) {
		Piece temp = theBoard.get(from);
		theBoard.remove(from);
		
		//This is if we are moving to a square after a successful strike and a piece is still there
		if(isSquareOccupied(to)) theBoard.remove(to);
		
		theBoard.put(to, temp);
	}
	
	public void removeTwoPieces(Square one, Square two) {
		theBoard.remove(one);
		theBoard.remove(two);
	}
	
	public Piece getPieceAt(int row, int column) {
		return theBoard.get(new Square(row, column));
	}
	
	public Piece getPieceAt(Square s) {
		return getPieceAt(s.getRow(), s.getColumn());
	}
	
	public static boolean isWithinBounds(Square s) {
		int row = s.getRow(), column = s.getColumn();
		if((row > MAX_ROWS) || (row < 0) || (column > MAX_COLUMNS) || (column < 0)) return false;
		else return true;
	}
	
	public boolean movablePiece(Square s) {
		PieceType p = theBoard.get(s).getPieceType();
		if(p == PieceType.FLAG) return false;
		else return true;
	}
	
	//Returns the team color that occupies Square s
	public PieceColor getTeamAtSquare(Square s) {
		return getPieceAt(s.getRow(), s.getColumn()).getPieceColor();
	}
	
	/**
	 * @param square
	 * @return true if the square has a piece on it, false otherwise
	 */
	public boolean isSquareOccupied(Square square) {
		return theBoard.containsKey(square);
	}
	
	public static boolean isChokePoint(Square s) {
		for(Square temp : chokePoints)
			if(s.equals(temp)) return true;
		
		return false;
	}	
	
	public boolean isClearPath(Square squareFrom, Square squareTo) {
		
		int yDiff = squareFrom.getRow() - squareTo.getRow();
		int xDiff = squareFrom.getColumn() - squareTo.getColumn();
		
		int row = squareFrom.getRow(), column = squareFrom.getColumn();
		
		//Set length to be the absolute value
		int length = Math.max(Math.abs(yDiff), Math.abs(xDiff));
		
		for(int i=0; i<length; i++) {
			if((yDiff > 0) && (xDiff == 0)) row--;
			else if((yDiff < 0) && (xDiff == 0)) row++;
			else if((xDiff > 0) && (yDiff == 0)) column--;
			else column++;
			
			//If the square is occupied
			if(this.isSquareOccupied(new Square(row, column))) return false;
		}
		
		return true;
	}
}
