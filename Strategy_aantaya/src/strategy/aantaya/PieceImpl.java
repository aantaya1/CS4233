package strategy.aantaya;

import strategy.Piece;

public class PieceImpl implements Piece{

	private final PieceType type;
	private final PieceColor color;
	private final int rank;
	
	public PieceImpl(PieceType t, PieceColor c) {
		this.type = t;
		this.color = c;
		
		if(t == PieceType.MARSHALL) rank = 12;
		else if(t == PieceType.GENERAL) rank = 11;
		else if(t == PieceType.COLONEL) rank = 10;
		else if(t == PieceType.MAJOR) rank = 9;
		else if(t == PieceType.CAPTAIN) rank = 8;
		else if(t == PieceType.LIEUTENANT) rank = 7;
		else if(t == PieceType.SERGEANT) rank = 6;
		else if(t == PieceType.MINER) rank = 5;
		else if(t == PieceType.SCOUT) rank = 4;
		else if(t == PieceType.SPY) rank = 3;
		else if(t == PieceType.BOMB) rank = 2;
		else rank = 1;
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

	/**
	 * @return the rank
	 */
	public int getRank() {
		return rank;
	}	
}
