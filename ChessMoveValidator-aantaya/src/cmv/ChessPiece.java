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

/**
 * This interface contains the methods that you may use for the chess pieces in the
 * chess move validator program. The master tests will supply a board with pieces that
 * implement this interface positioned on it.
 * 
 * NOTE: Students must implement an instance of this interface. Tests will only depend upon
 * this interface, but the students can add any behavior needed to their implementation.
 * 
 * @version Jan 25, 2019
 */
public interface ChessPiece
{
	public static enum PieceType {KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN};
	public static enum PieceColor {WHITE, BLACK};
	
	/**
	 * @return the piece type
	 */
	default PieceType getPieceType()
	{
		throw new MethodNotImplementedException("ChessPiece.getPieceType()");
	}
	
	/**
	 * @return the piece color
	 */
	default PieceColor getPieceColor()
	{
		throw new MethodNotImplementedException("ChessPiece.getPieceColor()");
	}
}
