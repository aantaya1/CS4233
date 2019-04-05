/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016 Gary F. Pollice
 *******************************************************************************/

package strategy.aantaya.version.beta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import strategy.Board;
import strategy.Piece;
import strategy.Piece.PieceColor;
import strategy.Piece.PieceType;
import strategy.StrategyException;
import strategy.aantaya.PieceImpl;
import strategy.aantaya.Square;

/**
 * Description
 * @version Mar 18, 2019
 */
public class BoardImpl implements Board {
	
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
	public BoardImpl(List<Piece.PieceType> redTeam, List<Piece.PieceType> blueTeam) {	
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
	
	/**
	 * Converting a board that implements the Board interface to my specific implementation
	 */
	public BoardImpl(Board b) {
		theBoard = new HashMap<Square, Piece>();
		
		//Populate red team
		for(int row=0; row<2; row++) {
			for(int column=0; column<MAX_COLUMNS; column++) {
				//Convert the board to our implementation, including converting each piece to our
				//	implementation using copy constructor				
				theBoard.put(new Square(row, column), 
						new PieceImpl(b.getPieceAt(row, column)));
			}
		}
		
		//Populate blue team
		for(int row=4; row<6; row++) {
			for(int column=0; column<MAX_COLUMNS; column++) {			
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
	
	public static boolean isWithinBounds(Square s) {
		int row = s.getRow(), column = s.getColumn();
		if((row > MAX_ROWS) || (row < 0) || (column > MAX_COLUMNS) || (column < 0)) return false;
		else return true;
	}
	
	public boolean movablePiece(Square s) {
		PieceType p = theBoard.get(s).getPieceType();
		if((p == PieceType.BOMB) || (p == PieceType.FLAG)) return false;
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
		boolean t = theBoard.get(square) != null;
		return t;
	}
}
