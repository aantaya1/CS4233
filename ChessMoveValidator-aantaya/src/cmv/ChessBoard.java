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

import java.util.*;

/**
 * This class represents the chess board for the move validation assignment.
 * It describes the methods that you can call to satisfy the assignment. All of the tests for
 * this assignment will pass a ChessBoard instance to the tests. The board will 
 * have the configuration that is to be tested.
 * 
 * NOTE: Students MAY NOT modify this class.
 * @version Jan 25, 2019
 */
public final class ChessBoard
{
	private final Map<Square, ChessPiece> theBoard;
	
	/**
	 * Constructor that takes the configuration.
	 * @param configuration the Map
	 */
	public ChessBoard(Map<Square, ChessPiece> configuration)
	{
		theBoard = configuration;
	}
	
	/**
	 * @param square the square to query
	 * @return the ChessPiece at the specified square or null if there is none
	 * @throws CMVException if the square is invalid
	 */
	public ChessPiece getPieceAt(Square square) 
	{
		return theBoard.get(square);
	}
	
	/**
	 * @param square
	 * @return true if the square has a piece on it, false otherwise
	 */
	public boolean isSquareOccupied(Square square)
	{
		boolean t = theBoard.get(square) != null;
		return t;
	}
}
