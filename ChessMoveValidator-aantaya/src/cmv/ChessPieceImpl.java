/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2019 Alexander Antaya
 *******************************************************************************/
package cmv;

/**
 * @author Alexander Antaya
 *
 */
public class ChessPieceImpl implements ChessPiece
{
	PieceType type;
	PieceColor color;

	/**
	 * 
	 */
	public ChessPieceImpl(PieceType _type, PieceColor _color)
	{
		this.type = _type;
		this.color = _color;
	}
	
	
	/**
	 * @return the piece type
	 */
	@Override
	public PieceType getPieceType()
	{
		return this.type;
	}
	
	/**
	 * @return the piece color
	 */
	@Override
	public PieceColor getPieceColor()
	{
		return this.color;
	}

}
