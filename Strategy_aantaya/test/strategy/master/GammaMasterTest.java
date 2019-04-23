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

package strategy.master;

import static org.junit.jupiter.api.Assertions.*;
import static strategy.Board.SquareType.CHOKE;
import static strategy.Piece.PieceColor.*;
import static strategy.Piece.PieceType.*;
import static strategy.StrategyGame.MoveResult.*;
import static strategy.StrategyGame.Version.*;
import static strategy.required.StrategyGameFactory.makeGame;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import strategy.*;
import strategy.StrategyGame.MoveResult;
import static strategy.master.Move.makeMoves;;

/**
 * Master tests for Gamma Strategy
 * @version Apr 18, 2019
 */
class GammaMasterTest
{
	private StrategyGame theGame = null;
	private List<Piece> redLineup = null;
	private List<Piece> blueLineup = null;
	private TestBoard theBoard = null;
	
	@BeforeEach
	void betaSetup() throws Exception
	{
		theBoard = new TestBoard(6, 6);
		redLineup = theBoard.makeLineup(RED,
				SERGEANT, SERGEANT, COLONEL, CAPTAIN, LIEUTENANT, LIEUTENANT,
				FLAG, MARSHAL, COLONEL, CAPTAIN, LIEUTENANT, SERGEANT);
		blueLineup = theBoard.makeLineup(BLUE,
				CAPTAIN, COLONEL, SERGEANT, SERGEANT, LIEUTENANT, LIEUTENANT,
				LIEUTENANT, FLAG, SERGEANT, CAPTAIN, COLONEL, MARSHAL);
		theBoard.initialize(6,  6, redLineup, blueLineup);
		theBoard.setSquareType(2, 2, CHOKE);
		theBoard.setSquareType(2, 3, CHOKE);
		theBoard.setSquareType(3, 2, CHOKE);
		theBoard.setSquareType(3, 3, CHOKE);
		theGame = makeGame(GAMMA, theBoard);
	}

	@Test
	void invalidBoardConfiguration()
	{
		assertThrows(StrategyException.class, ()-> makeGame(GAMMA, new TestBoard(1, 1)));
	}
	
	@ParameterizedTest
	@MethodSource("validMoveProvider")
	void validMoves(List<Move> moves, MoveResult expected, String testName)
	{
		MoveResult mr = null;
		for (Move m : moves) {
			mr = theGame.move(m.fRow, m.fCol, m.tRow, m.tCol);
		}
		assertEquals(expected, mr, testName);
	}
	
	@Test 
	void okAfterEightTurns()
	{
		theGame.move(1, 1, 2, 1);	// Move 1
		theGame.move(4, 0, 3, 0);
		theGame.move(2, 1, 1, 1);	// Move 2
		theGame.move(3, 0, 4, 0);
		theGame.move(1, 4, 2, 4);	// Move 3
		theGame.move(4, 5, 3, 5);
		theGame.move(2, 4, 1, 4);	// Move 4
		theGame.move(3, 5, 4, 5);
		theGame.move(1, 1, 2, 1);	// Move 5
		theGame.move(4, 0, 3, 0);
		theGame.move(2, 1, 1, 1);	// Move 6
		theGame.move(3, 0, 4, 0);
		theGame.move(1, 4, 2, 4);	// Move 7
		theGame.move(4, 5, 3, 5);
		theGame.move(2, 4, 1, 4);	// Move 8
		assertEquals(OK, theGame.move(3, 5, 4, 5));
	}
	
	static Stream<Arguments> validMoveProvider()
	{
		return Stream.of(
				Arguments.of(makeMoves(1, 5, 2, 5), OK, "1"),
				Arguments.of(makeMoves(1, 1, 2, 1, 4, 1, 3, 1), OK, "8"),
				Arguments.of(makeMoves(1, 1, 2, 1, 4, 1, 3, 1, 2, 1, 2, 0), OK,
						"14: Second move of a piece"),
				Arguments.of(makeMoves(1, 1, 2, 1, 4, 1, 3, 1, 2, 1, 3, 1), STRIKE_RED,
						"15: RED attacks and wins"),
				Arguments.of(makeMoves(1, 5, 2, 5, 4, 5, 3, 5, 2, 5, 3, 5), STRIKE_BLUE,
						"16: RED attacks and loses"),
				Arguments.of(makeMoves(1, 5, 2, 5, 4, 5, 3, 5, 2, 5, 3, 5, 2, 5, 2, 4), 
						OK, "17: BLUE moves after battle"),
				Arguments.of(makeMoves(1, 4, 2, 4, 4, 5, 3, 5, 2, 4, 2, 5, 3, 5, 2, 5),
						OK, "18: Draw in battle"),
				Arguments.of(makeMoves(1, 1, 2, 1, 4, 0, 3, 0, 1, 4, 2, 4, 3, 0, 3, 1, 2, 1, 3, 1, 4, 1, 3, 1),
						OK, "19: Move afer draw in battle"),
				Arguments.of(makeMoves(1, 4, 2, 4, 4, 0, 3, 0, 2, 4, 3, 4, 3, 0, 2, 0, 3, 4, 4, 4),
						RED_WINS, "20. Red captures the flag"),
				Arguments.of(makeMoves(1, 1, 2, 1, 4, 0, 3, 0, 2, 1, 3, 1, 3, 0, 2, 0, 3, 1, 2, 1, 2, 0, 1, 0),
						BLUE_WINS, "21: Blue captures the flag"),
				Arguments.of(makeMoves(1, 4, 2, 4, 4, 0, 3, 0, 2, 4, 3, 4, 3, 0, 2, 0, 3, 4, 4, 4, 4, 3, 3, 3),
						GAME_OVER, "22. Move after game over")
			);
	}
	
	@ParameterizedTest
	@MethodSource("invalidMoveProvider")
	void invalidMoves(List<Move> moves, MoveResult expected, String testName)
	{
		MoveResult mr = null;
		for (Move m : moves) {
			mr = theGame.move(m.fRow, m.fCol, m.tRow, m.tCol);
		}
		assertEquals(expected, mr, testName);
	}
	
	static Stream<Arguments> invalidMoveProvider()
	{
		return Stream.of(
				Arguments.of(makeMoves(4, 5, 3, 5), BLUE_WINS, "2: wrong color"),
				Arguments.of(makeMoves(1, 1, 1, 2), BLUE_WINS, "3: strike own color"),
				Arguments.of(makeMoves(2, 3, 2, 4), BLUE_WINS, "4: no piece on start square"),
				Arguments.of(makeMoves(1, 1, 3, 1), BLUE_WINS, "5: attempt to move two squares"),
				Arguments.of(makeMoves(1, 2, 2, 3), BLUE_WINS, "6: attempt to move diagonally"),
				Arguments.of(makeMoves(1, 0, 2, 0), BLUE_WINS, "7: attempt to move the FLAG"),
				Arguments.of(makeMoves(1, 1, 2, 1, 4, 3, 4, 2), RED_WINS, "9: strike own color"),
				Arguments.of(makeMoves(1, 1, 2, 1, 1, 4, 2, 4), RED_WINS, "10: wrong color"),
				Arguments.of(makeMoves(1, 1, 2, 1, 4, 0, 2, 0), RED_WINS, "11: attempt to move two squares"),
				Arguments.of(makeMoves(1, 4, 2, 4, 4, 2, 3, 3), RED_WINS, "12: attempt to move diagonally"),
				Arguments.of(makeMoves(1, 5, 2, 5, 4, 4, 3, 4), RED_WINS, "13: attempt to move FLAG"),
				Arguments.of(makeMoves(1, 1, 1, 1), BLUE_WINS, "23: attempt to move to same square"),
				Arguments.of(makeMoves(1, 5, 2, 6), BLUE_WINS, "24: Bad target square"),
				Arguments.of(makeMoves(1, 1, 2, 1, -1, 4, 3, 4), RED_WINS, "25: Bad source square"),
				Arguments.of(makeMoves(1, 3, 2, 3), BLUE_WINS, "30: Attempt to move onto CHOKE point"),
				Arguments.of(makeMoves(1, 1, 2, 1, 4, 3, 3, 3), RED_WINS, "31: Attempt to move onto CHOKE point"),
				Arguments.of(makeMoves(1, 1, 2, 1, 4, 0, 3, 0, 2, 1, 1, 1, 3, 0, 4, 0, 1, 1, 2, 1), 
						BLUE_WINS, "32: Move repetition"),
				Arguments.of(makeMoves(1, 1, 2, 1, 4, 0, 3, 0, 2, 1, 1, 1, 3, 0, 4, 0, 1, 5, 2, 5, 4, 0, 3, 0),
						RED_WINS, "33: Move repetition"),
				Arguments.of(makeMoves(1, 1, 2, 1, 4, 1, 3, 1, 2, 1, 1, 1, 3, 1, 2, 1, 1, 1, 2, 1), 
						STRIKE_RED, "34: Strike cancels repetition")
			);
	}
}
