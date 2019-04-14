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
}