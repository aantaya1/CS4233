/**
 * 
 */
package strategy.delta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import strategy.Piece;
import strategy.StrategyException;
import strategy.Board;
import strategy.Piece.PieceType;
import strategy.aantaya.PieceImpl;
import strategy.aantaya.Square;

/**
 * @author Owner
 *
 */
public class MyTestBoard implements Board{
	private Map<Square, Piece> theBoard;
	
	//Constants
	private static final int MAX_ROWS = 9;
	private static final int MAX_COLUMNS = 9;
	
	public MyTestBoard(List<Piece.PieceType> redTeam, List<Piece.PieceType> blueTeam) {	
		int index = 0;
		
		theBoard = new HashMap<Square, Piece>();
		
		//Fill in the red team, for this version, each square is a normal type
		for(int row=0; row<=3; row++) {
			for(int column=0; column<=MAX_COLUMNS; column++) {
				Piece p = new PieceImpl(redTeam.get(index++), Piece.PieceColor.RED);
				theBoard.put(new Square(row, column), p);
			}
		}
		
		//Fill in blue team
		index = 0;
		for(int row=6; row<=MAX_ROWS; row++) {
			for(int column=0; column<=MAX_COLUMNS; column++) {
				Piece p = new PieceImpl(blueTeam.get(index++), Piece.PieceColor.BLUE);
				theBoard.put(new Square(row, column), p);
			}
		}
	}
	
	@Override
	public Piece getPieceAt(int row, int column) {
		return theBoard.get(new Square(row, column));
	}
	
	@Override
	public SquareType getSquareTypeAt(int row, int column) {
		return SquareType.NORMAL;
	}
}
