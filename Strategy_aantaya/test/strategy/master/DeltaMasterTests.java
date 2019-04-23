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
import static strategy.StrategyGame.Version.*;
import static strategy.required.StrategyGameFactory.*;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import strategy.*;
import strategy.StrategyGame.MoveResult;
import static strategy.master.Move.makeMoves;
import static strategy.StrategyGame.MoveResult.*;
/**
 * Description
 * @version Apr 17, 2019
 */
class DeltaMasterTests
{
	private StrategyGame theGame = null;
	private List<Piece> redLineup = null;
	private List<Piece> blueLineup = null;
	private TestBoard theBoard = null;
	
	@BeforeEach
	void betaSetup() throws Exception
	{
		theBoard = new TestBoard(10, 10);
		redLineup = theBoard.makeLineup(RED,
				SCOUT, BOMB, MINER, MAJOR, LIEUTENANT, BOMB, SCOUT, COLONEL, BOMB, BOMB,
				CAPTAIN, LIEUTENANT, GENERAL, SCOUT, SERGEANT, SPY, MAJOR, SERGEANT, SCOUT, LIEUTENANT,
				SERGEANT, MINER, SCOUT, FLAG, CAPTAIN, LIEUTENANT, SCOUT, MINER, CAPTAIN, MINER,
				SCOUT, MARSHAL, CAPTAIN, MINER, MAJOR, BOMB, SERGEANT, COLONEL, BOMB, SCOUT);
		blueLineup = theBoard.makeLineup(BLUE,
				BOMB, CAPTAIN, SCOUT, MAJOR, BOMB, SCOUT, COLONEL, BOMB, MINER, BOMB, 
				BOMB, SERGEANT, GENERAL, MINER, CAPTAIN, SCOUT, LIEUTENANT, SERGEANT, MARSHAL, CAPTAIN,
				SCOUT, BOMB, LIEUTENANT, SERGEANT, CAPTAIN, MAJOR, MINER, LIEUTENANT, SCOUT, SERGEANT,
				FLAG, COLONEL, SCOUT, SCOUT, MINER, LIEUTENANT, SCOUT, MINER, SPY, MAJOR);
		theBoard.initialize(10,  10, redLineup, blueLineup);
		theBoard.setSquareType(4, 2, CHOKE);
		theBoard.setSquareType(4, 3, CHOKE);
		theBoard.setSquareType(5, 2, CHOKE);
		theBoard.setSquareType(5, 3, CHOKE);
		theBoard.setSquareType(4, 6, CHOKE);
		theBoard.setSquareType(4, 7, CHOKE);
		theBoard.setSquareType(5, 6, CHOKE);
		theBoard.setSquareType(5, 7, CHOKE);
		theGame = makeGame(DELTA, theBoard);
	}
	
	@Test
	void invalidBoardConfiguration()
	{
		redLineup = theBoard.makeLineup(RED,
				SCOUT, BOMB, MINER, MAJOR, LIEUTENANT, BOMB, SCOUT, COLONEL, BOMB, BOMB,
				CAPTAIN, LIEUTENANT, GENERAL, SCOUT, SERGEANT, SPY, MAJOR, SERGEANT, SCOUT, LIEUTENANT,
				SERGEANT, MINER, SCOUT, FLAG, CAPTAIN, LIEUTENANT, SCOUT, MINER, CAPTAIN, MINER,
				SCOUT, MARSHAL, CAPTAIN, MAJOR, MAJOR, BOMB, SERGEANT, COLONEL, BOMB, SCOUT);
		assertThrows(StrategyException.class, ()-> makeGame(DELTA, new TestBoard(5, 5)));
	}
	
	@Test
	void blueCannotMove()
	{
		theBoard = new TestBoard(10, 10);
		blueLineup = theBoard.makeLineup(BLUE,
				COLONEL, CAPTAIN, SCOUT, MAJOR, MINER, SCOUT, COLONEL, LIEUTENANT, MINER, SPY, 
				MAJOR, SERGEANT, GENERAL, MINER, CAPTAIN, SCOUT, LIEUTENANT, SERGEANT, MARSHAL, CAPTAIN,
				SCOUT, BOMB, LIEUTENANT, SERGEANT, CAPTAIN, MAJOR, MINER, LIEUTENANT, SCOUT, SERGEANT,
				FLAG, BOMB, SCOUT, SCOUT, BOMB, BOMB, SCOUT, MINER, BOMB, BOMB);
		theBoard.initialize(10,  10, redLineup, blueLineup);
		theBoard.setSquareType(4, 2, CHOKE);
		theBoard.setSquareType(4, 3, CHOKE);
		theBoard.setSquareType(5, 2, CHOKE);
		theBoard.setSquareType(5, 3, CHOKE);
		theBoard.setSquareType(4, 6, CHOKE);
		theBoard.setSquareType(4, 7, CHOKE);
		theBoard.setSquareType(5, 6, CHOKE);
		theBoard.setSquareType(5, 7, CHOKE);
		theGame = makeGame(DELTA, theBoard);
		assertEquals(RED_WINS, theGame.move(3,  1,  4,  1));
	}
	
	@ParameterizedTest
	@MethodSource("masterProvider")
	void masterDeltaTest(List<Move> moves, MoveResult expected, String testName)
	{
		MoveResult mr = null;
		for (Move m : moves) {
			mr = theGame.move(m.fRow, m.fCol, m.tRow, m.tCol);
		}
		assertEquals(expected, mr, testName);
	}
	
	static Stream<Arguments> masterProvider()
	{
		return Stream.of(
				Arguments.of(makeMoves(3, 0, 5, 0), OK, "39: SCOUT moves 2 spaces"),
				Arguments.of(makeMoves(3, 0, 6, 0), BLUE_WINS, "40: SCOUT attacks non-adjacent"),
				Arguments.of(makeMoves(3, 1, 4, 1, 6, 1, 5, 1, 4, 1, 5, 1), 
						STRIKE_RED, "41: Marshal attacks Spy"),
				Arguments.of(makeMoves(3, 1, 4, 1, 6, 1, 5, 1, 3, 9, 4, 9, 5, 1, 4, 1), 
						STRIKE_BLUE, "42: Spy attacks Marshal"),
				Arguments.of(makeMoves(3, 1, 4, 1, 6, 1, 5, 1, 4, 1, 3, 1, 5, 1, 6, 1, 3, 1, 4, 1),
						BLUE_WINS, "43: repetition red"),
				Arguments.of(makeMoves(3, 1, 4, 1, 6, 1, 5, 1, 4, 1, 3, 1, 5, 1, 6, 1, 3, 4, 4, 4, 6, 1, 5, 1),
						RED_WINS, "44: repetition blue"),
				Arguments.of(makeMoves(3, 3, 4, 3), BLUE_WINS, "46: Red move to choke point"),
				Arguments.of(makeMoves(3, 4, 4, 4, 6, 6, 5, 6), RED_WINS,
						"47.  Blue move to choke point"),
				Arguments.of(makeMoves(3, 2, 4, 1), BLUE_WINS, "48: Diagonal move"),
				Arguments.of(makeMoves(3, 0, 4, 0, 6, 0, 5, 0, 4, 0, 6, 0), BLUE_WINS,
						"49: Attempt to jump over a piece"),
				Arguments.of(makeMoves(3, 9, 4, 9, 6, 4, 5, 4, 4, 9, 4, 5), BLUE_WINS,
						"50. Attempt to move over choke point")
			);
	}
}
