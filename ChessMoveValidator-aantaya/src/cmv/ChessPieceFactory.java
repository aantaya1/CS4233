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

import cmv.ChessPiece.*;

/**
 * A simple factory for creating chess pieces. This needs to be implemented
 * by the student.
 * @version Feb 15, 2019
 */
public class ChessPieceFactory
{
	public static ChessPiece makePiece(PieceColor color, PieceType type)
	{
		return new ChessPieceImpl(type, color);
	}
}
