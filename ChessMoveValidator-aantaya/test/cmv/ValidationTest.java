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

package cmv;

import java.util.*;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import static org.junit.jupiter.api.Assertions.*;
import static cmv.SquareFactory.makeSquare;
import static cmv.MoveValidator.canMove;
import static cmv.ChessPiece.PieceColor.*;
import static cmv.ChessPiece.PieceType.*;
import static cmv.ChessPieceFactory.*;

/**
 * This is a sample of the type of tests that will be run on your code. You should make sure
 * that these run on your assignment code before submitting your work.
 * @version Mar 9, 2019
 */
class ValidationTest
{
	@ParameterizedTest
	@MethodSource("testCaseProvider")
	void validationTest(ChessBoard board, Square source, Square target, boolean expected)
	{
		assertEquals(expected, canMove(board, source, target));
	}
	
	static Stream<Arguments> testCaseProvider()
	{
		return Stream.of(
			// Empty board
			Arguments.of(makeBoard(), makeSquare('e', 4), makeSquare('e', 5), false),
			
			// Rook tests
			Arguments.of(makeBoard(makeSquare('h', 1), makePiece(WHITE, ROOK)), //FALSE: Move not straight
					makeSquare('h', 1), makeSquare('e', 3), false),
			Arguments.of(makeBoard(makeSquare('h', 1), makePiece(WHITE, ROOK)), //FALSE: Go off the board via row
					makeSquare('h', 1), makeSquare('h', 9), false),
			Arguments.of(makeBoard(makeSquare('h', 1), makePiece(WHITE, ROOK)), //FALSE: Go off the board via column
					makeSquare('h', 1), makeSquare('i', 3), false),
			Arguments.of(makeBoard(makeSquare('h', 1), makePiece(WHITE, ROOK)), //TRUE: Move straight
					makeSquare('h', 1), makeSquare('h', 3), true),
			Arguments.of(makeBoard(makeSquare('h', 1), makePiece(WHITE, ROOK), makeSquare('h', 2), makePiece(WHITE, KNIGHT)), //FALSE: Move over another piece
					makeSquare('h', 1), makeSquare('h', 4), false),
			Arguments.of(makeBoard(makeSquare('h', 1), makePiece(WHITE, ROOK), makeSquare('h', 2), makePiece(WHITE, KNIGHT)), //FALSE: Move to square occupied by own color piece
					makeSquare('h', 1), makeSquare('h', 2), false),
			
			// Bishop tests
			Arguments.of(makeBoard(makeSquare('c', 1), makePiece(BLACK, BISHOP)), //TRUE: Move diagonally
					makeSquare('c', 1), makeSquare('e', 3), true),
			Arguments.of(makeBoard(makeSquare('e', 3), makePiece(WHITE, BISHOP)), //FALSE: Move not diagonally
					makeSquare('e', 3), makeSquare('h', 1), false),
			Arguments.of(makeBoard(makeSquare('e', 3), makePiece(WHITE, BISHOP)), //FALSE: Move straight
					makeSquare('e', 3), makeSquare('e', 4), false),
			Arguments.of(makeBoard(makeSquare('h', 1), makePiece(WHITE, BISHOP), makeSquare('h', 2), makePiece(WHITE, BISHOP)), //FALSE: Move over another piece
					makeSquare('h', 1), makeSquare('h', 4), false),
			
			// Queen tests
			Arguments.of(makeBoard(makeSquare('c', 1), makePiece(BLACK, QUEEN)), //TRUE: Can move diagonally
					makeSquare('c', 1), makeSquare('e', 3), true),
			Arguments.of(makeBoard(makeSquare('e', 3), makePiece(WHITE, QUEEN)), //FALSE: Cannot move non-linearly
					makeSquare('e', 3), makeSquare('h', 1), false),
			Arguments.of(makeBoard(makeSquare('c', 1), makePiece(BLACK, QUEEN)), //TRUE: Can move backwards straight
					makeSquare('c', 1), makeSquare('c', 0), true),
			Arguments.of(makeBoard(makeSquare('e', 3), makePiece(WHITE, QUEEN)), //TRUE: Can move backwards diagonally
					makeSquare('e', 3), makeSquare('c', 1), true),
			Arguments.of(makeBoard(makeSquare('h', 1), makePiece(WHITE, QUEEN), makeSquare('h', 2), makePiece(WHITE, BISHOP)), //FALSE: Go over her own pieces
					makeSquare('h', 1), makeSquare('h', 4), false),
			Arguments.of(makeBoard(makeSquare('h', 1), makePiece(WHITE, QUEEN), makeSquare('h', 2), makePiece(BLACK, BISHOP)), //TRUE: Go over enemy pieces
					makeSquare('h', 1), makeSquare('h', 4), true),
			Arguments.of(makeBoard(makeSquare('e', 1), makePiece(WHITE, QUEEN)), //TRUE: Can move left
					makeSquare('e', 1), makeSquare('c', 1), true),
			Arguments.of(makeBoard(makeSquare('e', 1), makePiece(WHITE, QUEEN)), //TRUE: Can move right
					makeSquare('e', 1), makeSquare('h', 1), true),
			
			// King tests
			Arguments.of(makeBoard(makeSquare('c', 1), makePiece(BLACK, KING)), //TRUE: Can move backwards straight one square
					makeSquare('c', 1), makeSquare('c', 0), true),
			Arguments.of(makeBoard(makeSquare('e', 3), makePiece(WHITE, KING)), //TRUE: Can move backwards diagonally one square
					makeSquare('e', 3), makeSquare('d', 2), true),
			Arguments.of(makeBoard(makeSquare('c', 1), makePiece(BLACK, KING)), //FALSE: Cannot move forward straight two square
					makeSquare('c', 1), makeSquare('c', 3), false),
			Arguments.of(makeBoard(makeSquare('e', 3), makePiece(WHITE, KING)), //FALSE: Cannot move backwards diagonally two square
					makeSquare('e', 3), makeSquare('c', 1), false),
			
			// Knight tests
			Arguments.of(makeBoard(makeSquare('c', 1), makePiece(BLACK, KNIGHT)), //TRUE: Normal move forward-left
					makeSquare('c', 1), makeSquare('b', 3), true),
			Arguments.of(makeBoard(makeSquare('c', 1), makePiece(BLACK, KNIGHT)), //TRUE: Normal move forward-right
					makeSquare('c', 1), makeSquare('d', 3), true),
			Arguments.of(makeBoard(makeSquare('d', 4), makePiece(BLACK, KNIGHT)), //TRUE: Normal move backward-left
					makeSquare('d', 4), makeSquare('c', 2), true),
			Arguments.of(makeBoard(makeSquare('d', 4), makePiece(BLACK, KNIGHT)), //TRUE: Normal move backward-right
					makeSquare('d', 4), makeSquare('e', 2), true),
			Arguments.of(makeBoard(makeSquare('c', 1), makePiece(BLACK, KNIGHT), makeSquare('c', 2), makePiece(WHITE, BISHOP)), //TRUE: Normal move forward-left over enemy piece
					makeSquare('c', 1), makeSquare('b', 3), true),
			Arguments.of(makeBoard(makeSquare('c', 1), makePiece(BLACK, KNIGHT), makeSquare('c', 2), makePiece(BLACK, BISHOP)), //TRUE: Normal move forward-left over own color piece
					makeSquare('c', 1), makeSquare('b', 3), true),
			Arguments.of(makeBoard(makeSquare('c', 1), makePiece(BLACK, KNIGHT), makeSquare('c', 2), makePiece(BLACK, BISHOP)), //FALSE: Move non-L over own piece
					makeSquare('c', 1), makeSquare('b', 4), false),
			Arguments.of(makeBoard(makeSquare('c', 1), makePiece(BLACK, KNIGHT)), //FALSE: Move non-L
					makeSquare('c', 1), makeSquare('a', 3), false),
			
			// Pawn tests
			Arguments.of(makeBoard(makeSquare('c', 7), makePiece(BLACK, PAWN)), //TRUE: Normal move forward one (down the board)
					makeSquare('c', 7), makeSquare('c', 6), true),
			Arguments.of(makeBoard(makeSquare('e', 3), makePiece(WHITE, PAWN)), //TRUE: Normal move forward one (up the board)
					makeSquare('e', 3), makeSquare('e', 4), true),
			Arguments.of(makeBoard(makeSquare('e', 3), makePiece(WHITE, PAWN)), //FALSE: Normal move forward two (non-starting state)
					makeSquare('e', 3), makeSquare('e', 5), false),
			Arguments.of(makeBoard(makeSquare('e', 3), makePiece(BLACK, PAWN)), //FALSE: Normal move forward two (non-starting state)
					makeSquare('e', 6), makeSquare('e', 4), false)
		);
	}

	// Helper methods
	/**
	 * Create the board configuration. 
	 * @param sp alternating squares and pieces
	 * @return the ChessBoard
	 */
	private static ChessBoard makeBoard(Object ...sp)
	{
		Map<Square, ChessPiece> config = new HashMap<Square, ChessPiece>();
		int i = 0;
		int max = sp.length;
		while (i < max) {
			Square s = (Square)sp[i++];
			ChessPiece p = (ChessPiece)sp[i++];
			config.put(s, p);
		}
		return new ChessBoard(config);
	}
}
