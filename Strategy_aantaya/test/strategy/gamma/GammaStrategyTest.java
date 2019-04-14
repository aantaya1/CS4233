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
package strategy.gamma;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import strategy.Board;
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
class GammaStrategyTest {
	private StrategyGame game;
	
	@BeforeEach
	private void setup() {
		game = makeGame(GAMMA, createBoard());
	}
	
	//-----Game mechanics tests-----//
	
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
		assertEquals(BLUE_WINS, game.move(1, 0, 2, 0));
	}
	
	@Test
	void redMoveDiagonally() {
		assertEquals(BLUE_WINS, game.move(1, 1, 2, 0));
	}
	
	@Test
	void blueMoveMultipleSquares() {
		game.move(1, 1, 2, 1);//red must go first
		assertEquals(RED_WINS, game.move(4, 1, 2, 1));
	}
	
	@Test
	void redMoveToSameSquare() {
		assertEquals(BLUE_WINS, game.move(1, 0, 1, 0));
	}
	
	@Test
	void blueMoveToSquareOccupiedByOwnTeam() {
		game.move(1, 1, 2, 1);//red must go first
		assertEquals(RED_WINS, game.move(5, 5, 4, 5));
	}
	
	@Test
	void redMoveToSquareOccupiedByOwnTeam() {
		assertEquals(BLUE_WINS, game.move(0, 3, 1, 3));
	}
	
	@Test
	void redMovesToSameSquare() {
		assertEquals(BLUE_WINS, game.move(1, 4, 1, 4));
	}
	
	@Test
	void repetitionRule() {
		game.move(1, 4, 2, 4);//red cap forward
		game.move(4, 5, 3, 5);//blue lie forward
		game.move(2, 4, 1, 4);//red cap backward
		game.move(4, 4, 3, 4);//blue ser forward
		assertEquals(BLUE_WINS, game.move(1, 4, 2, 4));//red cap forward
	}
	
	//-----Striking tests-----//
	
	@Test
	void gamePlay1() {
		game.move(1, 1, 2, 1);//red lie forward
		game.move(4, 1, 3, 1);//blue col forward
		assertEquals(OK, game.move(2, 1, 3, 1));//red lie strikes blue col, blue col takes red lie's square
		
		game.move(4, 4, 3, 4);//blue ser forward
		assertEquals(BLUE_WINS, game.move(1, 2, 2, 2));//red ser forward to choke point
		assertEquals(GAME_OVER, game.move(3, 4, 2, 4));//blue ser forward
	}
	
	
	/**
	 * This will create the following board

         0       1       2       3       4       5    
	  +-------+-------+-------+-------+-------+-------+
	  |       |       |       |       |       |       |
	5 |  LIE  |  LIE  |  SER  |  SER  |  COL  |  CAP  | 5
	  |       |       |       |       |       |       |
	  +-------+-------+-------+-------+-------+-------+
	  |       |       |       |       |       |       |
	4 |  FLA  |  COL  |  MAR  |  CAP  |  SER  |  LIE  | 4
	  |       |       |       |       |       |       |
	  +-------+-------+-------+-------+-------+-------+
	  |       |       |       |       |       |       |
	3 |       |       |       |       |       |       | 3
	  |       |       |       |       |       |       |
	  +-------+-------+-------+-------+-------+-------+
	  |       |       |       |       |       |       |
	2 |       |       |       |       |       |       | 2
	  |       |       |       |       |       |       |
	  +-------+-------+-------+-------+-------+-------+
	  |       |       |       |       |       |       |
	1 |  FLA  |  LIE  |  SER  |  MAR  |  CAP  |  COL  | 1
	  |       |       |       |       |       |       |
	  +-------+-------+-------+-------+-------+-------+
	  |       |       |       |       |       |       |
	0 |  LIE  |  LIE  |  SER  |  SER  |  COL  |  CAP  | 0
	  |       |       |       |       |       |       |
	  +-------+-------+-------+-------+-------+-------+
	      0       1       2       3       4       5    
	 * 
	 * 
	 */
	private Board createBoard() {
		List<PieceType> redTeam = Arrays.asList(PieceType.LIEUTENANT, PieceType.LIEUTENANT, PieceType.SERGEANT, 
				PieceType.SERGEANT, PieceType.COLONEL, PieceType.CAPTAIN, PieceType.FLAG, PieceType.LIEUTENANT, 
				PieceType.SERGEANT, PieceType.MARSHAL, PieceType.CAPTAIN, PieceType.COLONEL);
		
		List<PieceType> blueTeam = Arrays.asList(PieceType.FLAG, PieceType.COLONEL, 
				PieceType.MARSHAL, PieceType.CAPTAIN, PieceType.SERGEANT, PieceType.LIEUTENANT, PieceType.LIEUTENANT, PieceType.LIEUTENANT, PieceType.SERGEANT, 
				PieceType.SERGEANT, PieceType.COLONEL, PieceType.CAPTAIN);
			
		return new MyTestBoard(redTeam, blueTeam);
	}
}
