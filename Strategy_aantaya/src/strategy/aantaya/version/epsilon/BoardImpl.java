/**
 * 
 */
package strategy.aantaya.version.epsilon;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import strategy.Board;
import strategy.Piece;
import strategy.StrategyException;
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

	private int mar=1, gen=1, col=2, maj=3, cap=4, lie=4, ser=4, min=5, scout=8, spy=1, bomb=6, flag=1;
	
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
				if(b.getPieceAt(row, column) == null) throw new StrategyException("Inproper board layout");
				PieceImpl p = new PieceImpl(b.getPieceAt(row, column));
				
				indexPiece(p);
				
				theBoard.put(new Square(row, column), 
						new PieceImpl(b.getPieceAt(row, column)));
			}
		}
		
		if(!isCorrectPieceCount()) throw new StrategyException("Inproper board layout");
		else resetPieceCounts();
		
		//Populate blue team
		for(int row=6; row<=MAX_ROWS; row++) {
			for(int column=0; column<=MAX_COLUMNS; column++) {	
				if(b.getPieceAt(row, column) == null) throw new StrategyException("Inproper board layout");
				PieceImpl p = new PieceImpl(b.getPieceAt(row, column));
				
				indexPiece(p);
				
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
	
	public void removeOnePiece(Square s) {
		theBoard.remove(s);
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
	
	public boolean isClearPath(Square squareFrom, Square squareTo, boolean includeSquareTo) {
		
		int yDiff = squareFrom.getRow() - squareTo.getRow();
		int xDiff = squareFrom.getColumn() - squareTo.getColumn();
		
		int row = squareFrom.getRow(), column = squareFrom.getColumn();
		
		//Set length to be the absolute value
		int length = Math.max(Math.abs(yDiff), Math.abs(xDiff));
		
		if(!includeSquareTo) length = length - 1;
		
		for(int i=0; i<length; i++) {
			if((yDiff > 0) && (xDiff == 0)) row--;
			else if((yDiff < 0) && (xDiff == 0)) row++;
			else if((xDiff > 0) && (yDiff == 0)) column--;
			else column++;
			
			Square temp = new Square(row, column);
			
			//If the square is occupied
			if(this.isSquareOccupied(temp) || isChokePoint(temp)) return false;
		}
		
		return true;
	}
	
	private void indexPiece(Piece p) {
		if(p.getPieceType() == PieceType.MARSHAL) mar--;
		else if(p.getPieceType() == PieceType.GENERAL) gen--;
		else if(p.getPieceType() == PieceType.COLONEL) col--;
		else if(p.getPieceType() == PieceType.MAJOR) maj--;
		else if(p.getPieceType() == PieceType.CAPTAIN) cap--;
		else if(p.getPieceType() == PieceType.LIEUTENANT) lie--;
		else if(p.getPieceType() == PieceType.SERGEANT) ser--;
		else if(p.getPieceType() == PieceType.MINER) min--;
		else if(p.getPieceType() == PieceType.SCOUT) scout--;
		else if(p.getPieceType() == PieceType.SPY) spy--;
		else if(p.getPieceType() == PieceType.BOMB) bomb--;
		else if(p.getPieceType() == PieceType.FLAG) flag--;
	}
	
	private void resetPieceCounts() {
		mar=1; gen=1; col=2; maj=3; cap=4; lie=4; ser=4; min=5; scout=8; spy=1; bomb=6; flag=1;
	}
	
	private boolean isCorrectPieceCount() {
		int count = mar+gen+col+maj+cap+lie+ser+lie+ser+min+scout+spy+bomb+flag;
		if(count != 0) return false;
		return true;
	}
}
