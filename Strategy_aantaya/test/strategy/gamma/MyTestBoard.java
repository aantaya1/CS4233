package strategy.gamma;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import strategy.Board;
import strategy.Piece;
import strategy.Piece.PieceType;
import strategy.StrategyException;
import strategy.aantaya.PieceImpl;
import strategy.aantaya.Square;

public class MyTestBoard implements Board{
	private Map<Square, Piece> theBoard;
	
	//Constants
	private static final int MAX_ROWS = 5;
	private static final int MAX_COLUMNS = 5;
	
	/**
	 * We will fill the teams on the board in this order:
	 * 
	 * R = the redTeam Parameter
	 * B = the blueTeam Parameter
	 * 
	 * Note: In the diagram below, Y.x represents the x'th index in the Y list
	 * Example: R.4 is the forth index in the redTeam list

         0       1       2       3       4       5    
	  +-------+-------+-------+-------+-------+-------+
	  |       |       |       |       |       |       |
	5 |  B.6  |  B.7  |  B.8  |  B.9  | B.10  | B.11  | 5
	  |       |       |       |       |       |       |
	  +-------+-------+-------+-------+-------+-------+
	  |       |       |       |       |       |       |
	4 |  B.0  |  B.1  |  B.2  |  B.3  |  B.4  |  B.5  | 4
	  |       |       |       |       |       |       |
	  +-------+-------+-------+-------+-------+-------+
	  |       |       |       |       |       |       |
	3 |       |       |       |       |       |       | 3
	  |       |       |       |       |       |       |
	  +-------+-------+-------+-------+-------+-------+
	  |       |       |       |       |       |       |
	2 |       |       |       |       |       |       | 2
	  |       |       |       |       |       |       |
	  +-------+-------+-------+-------+-------+-------+
	  |       |       |       |       |       |       |
	1 |  R.6  |  R.7  |  R.8  |  R.9  | R.10  | R.11  | 1
	  |       |       |       |       |       |       |
	  +-------+-------+-------+-------+-------+-------+
	  |       |       |       |       |       |       |
	0 |  R.0  |  R.1  |  R.2  |  R.3  |  R.4  |  R.5  | 0
	  |       |       |       |       |       |       |
	  +-------+-------+-------+-------+-------+-------+
	      0       1       2       3       4       5    
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	public MyTestBoard(List<Piece.PieceType> redTeam, List<Piece.PieceType> blueTeam) {	
		int index = 0;
		
		theBoard = new HashMap<Square, Piece>();
		
		//Fill in the red team, for this version, each square is a normal type
		for(int row=0; row<2; row++) {
			for(int column=0; column<=MAX_COLUMNS; column++) {
				Piece p = new PieceImpl(redTeam.get(index++), Piece.PieceColor.RED);
				if((p.getPieceType() == PieceType.BOMB) || (p.getPieceType() == PieceType.SCOUT)) 
					throw new StrategyException("Found Invalid Piece Type");
				theBoard.put(new Square(row, column), p);
			}
		}
		
		//Fill in blue team
		index = 0;
		for(int row=4; row<=MAX_ROWS; row++) {
			for(int column=0; column<=MAX_COLUMNS; column++) {
				Piece p = new PieceImpl(blueTeam.get(index++), Piece.PieceColor.BLUE);
				if((p.getPieceType() == PieceType.BOMB) || (p.getPieceType() == PieceType.SCOUT)) 
					throw new StrategyException("Found Invalid Piece Type");
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
