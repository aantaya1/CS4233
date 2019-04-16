package strategy.aantaya;

import strategy.Piece;

public class PieceImpl implements Piece{

	private final PieceType type;
	private final PieceColor color;
	
	public PieceImpl(PieceType t, PieceColor c) {
		this.type = t;
		this.color = c;
	}
	
	//Copy Constructor
	public PieceImpl(Piece p) {
		this(p.getPieceType(), p.getPieceColor());
	}

	/**
	 * @return the type
	 */
	@Override
	public PieceType getPieceType() {
		return type;
	}

	/**
	 * @return the color
	 */
	@Override
	public PieceColor getPieceColor() {
		return color;
	}
	
	public static int rankToInt(PieceType type) {
		if(type == PieceType.MARSHAL) return 12;
		else if(type == PieceType.GENERAL) return 11;
		else if(type == PieceType.COLONEL) return 10;
		else if(type == PieceType.MAJOR) return 9;
		else if(type == PieceType.CAPTAIN) return 8;
		else if(type == PieceType.LIEUTENANT) return 7;
		else if(type == PieceType.SERGEANT) return 6;
		else if(type == PieceType.MINER) return 5;
		else if(type == PieceType.SCOUT) return 4;
		else if(type == PieceType.SPY) return 3;
		else if(type == PieceType.BOMB) return 2;
		else return 1;
	}
	
	public static boolean isValidPhysicalMove(Square squareFrom, Square squareTo, Piece p) {
		int yDiff = Math.abs(squareFrom.getRow() - squareTo.getRow());
		int xDiff = Math.abs(squareFrom.getColumn() - squareTo.getColumn());
		
		//Piece must move
		if((xDiff == 0) && (yDiff == 0)) return false;
		
		//Piece cannot move diagonally
		if(yDiff == xDiff) return false;
		
		if(p.getPieceType() != PieceType.SCOUT)
			if((yDiff > 1) || (xDiff > 1)) return false;
		
		return true;
	}
}