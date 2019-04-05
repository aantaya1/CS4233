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
package strategy.beta;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import strategy.Board;
import strategy.Piece;
import strategy.Piece.PieceType;
import strategy.aantaya.version.beta.BoardImpl;
import strategy.StrategyGame;

import static strategy.StrategyGame.Version.*;
import static strategy.required.StrategyGameFactory.*;

import java.util.Arrays;
import java.util.List;

import static strategy.StrategyGame.MoveResult.*;

/**
 * Test cases for Alpha Strategy.
 * @version April 02, 2019
 */
class BetaStrategyTest {
	private StrategyGame game;
	
	@BeforeEach
	private void setup() {
		game = makeGame(BETA, createBoard());
	}
	
	@Test
	void redMovesPieceFromSquareWithNoPiece() {
		assertEquals(BLUE_WINS, game.move(0, 2, 0, 3));
	}
	
	@Test
	void redMovesOutOfBounds() {
		assertEquals(BLUE_WINS, game.move(0, 1, 0, -1));
	}
	
	@Test
	void redMoveFlag() {
		assertEquals(BLUE_WINS, game.move(0, 0, 0, 1));
	}
	
	@Test
	void redMoveDiagonally() {
		assertEquals(BLUE_WINS, game.move(1, 0, 2, 1));
	}
	
	@Test
	void blueMoveMultipleSquares() {
		game.move(1, 0, 2, 0);//red must go first
		assertEquals(RED_WINS, game.move(4, 0, 2, 0));
	}
	
	@Test
	void redMoveToSameSquare() {
		assertEquals(BLUE_WINS, game.move(1, 0, 1, 0));
	}
	
	@Test
	void blueMoveToSquareOccupiedByOwnTeam() {
		game.move(1, 0, 2, 0);//red must go first
		assertEquals(RED_WINS, game.move(5, 5, 4, 5));
	}
	
	@Test
	void redMoveToSquareOccupiedByOwnTeam() {
		assertEquals(BLUE_WINS, game.move(0, 3, 1, 3));
	}
	
	@Test
	void moveAfterGameIsOver() {//Red team moves 2x in a row which triggers BLUE_WIN, then red tries to move again
		game.move(1, 3, 2, 3);
		game.move(4, 2, 3, 2);
		game.move(1, 5, 2, 5);
		game.move(1, 0, 2, 0);//second red move in a row --> BLUE_WIN
		
		assertEquals(GAME_OVER, game.move(1, 1, 2, 1));
	}
	
	@Test
	void moveSixteenTimes() {
		
		game.move(1, 2, 2, 2);
		game.move(4, 2, 3, 2);
		game.move(2, 2, 1, 2);
		game.move(3, 2, 4, 2);
		game.move(1, 2, 2, 2);
		game.move(4, 2, 3, 2);
		game.move(2, 2, 1, 2);
		game.move(3, 2, 4, 2);
		game.move(1, 2, 2, 2);
		game.move(4, 2, 3, 2);
		game.move(2, 2, 1, 2);
		game.move(3, 2, 4, 2);
		game.move(1, 2, 2, 2);
		game.move(4, 2, 3, 2);
		game.move(2, 2, 1, 2);
		
		assertEquals(RED_WINS, game.move(3, 2, 4, 2));
	}	
	
	@Test
	void moveSeventeenTimes() {
		
		game.move(1, 2, 2, 2);
		game.move(4, 2, 3, 2);
		game.move(2, 2, 1, 2);
		game.move(3, 2, 4, 2);
		game.move(1, 2, 2, 2);
		game.move(4, 2, 3, 2);
		game.move(2, 2, 1, 2);
		game.move(3, 2, 4, 2);
		game.move(1, 2, 2, 2);
		game.move(4, 2, 3, 2);
		game.move(2, 2, 1, 2);
		game.move(3, 2, 4, 2);
		game.move(1, 2, 2, 2);
		game.move(4, 2, 3, 2);
		game.move(2, 2, 1, 2);
		game.move(3, 2, 4, 2);
		
		assertEquals(GAME_OVER, game.move(1, 2, 2, 2));
	}
	
	
	
	
	private Board createBoard() {
		List<PieceType> redTeam = Arrays.asList(PieceType.FLAG, PieceType.MARSHALL, PieceType.COLONEL, 
				PieceType.COLONEL, PieceType.CAPTAIN, PieceType.CAPTAIN, PieceType.LIEUTENANT, PieceType.LIEUTENANT, 
				PieceType.LIEUTENANT, PieceType.SERGEANT, PieceType.SERGEANT, PieceType.SERGEANT);
		
		List<PieceType> blueTeam = Arrays.asList(PieceType.FLAG, PieceType.MARSHALL, PieceType.COLONEL, 
				PieceType.COLONEL, PieceType.CAPTAIN, PieceType.CAPTAIN, PieceType.LIEUTENANT, PieceType.LIEUTENANT, 
				PieceType.LIEUTENANT, PieceType.SERGEANT, PieceType.SERGEANT, PieceType.SERGEANT);
			
		return new BoardImpl(redTeam, blueTeam);
	}
}
