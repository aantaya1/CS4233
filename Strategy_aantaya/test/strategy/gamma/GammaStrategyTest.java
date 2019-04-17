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
	void redMovesPieceFromSquareWithNoPiece() {//#1
		assertEquals(BLUE_WINS, game.move(0, 2, 0, 3));
	}
	
	@Test
	void redMovesOutOfBounds() {//#2
		assertEquals(BLUE_WINS, game.move(0, 1, 0, -1));
	}
	
	@Test
	void redMoveFlag() {//#3
		assertEquals(BLUE_WINS, game.move(1, 0, 2, 0));
	}
	
	@Test
	void redMoveTwoSquares() {//#4
		assertEquals(BLUE_WINS, game.move(1, 1, 3, 1));
	}
	
	@Test
	void redMovesFromSquareWithNoPiece() {//#1
		assertEquals(BLUE_WINS, game.move(2, 1, 3, 1));
	}
	
	@Test
	void blueMovesFromSquareWithNoPiece() {//#1
		game.move(1, 1, 2, 1);
		assertEquals(RED_WINS, game.move(4, 4, 2, 4));
	}
	
	@Test
	void redMoveDiagonally() {//#5
		assertEquals(BLUE_WINS, game.move(1, 1, 2, 0));
	}
	
	@Test
	void blueMoveMultipleSquares() {//#4
		game.move(1, 1, 2, 1);//red must go first
		assertEquals(RED_WINS, game.move(4, 1, 2, 1));
	}
	
	@Test
	void redMoveToSameSquare() {//#6
		assertEquals(BLUE_WINS, game.move(1, 0, 1, 0));
	}
	
	@Test
	void blueMoveToSquareOccupiedByOwnTeam() {//#7
		game.move(1, 1, 2, 1);//red must go first
		assertEquals(RED_WINS, game.move(5, 5, 4, 5));
	}
	
	@Test
	void redMoveToSquareOccupiedByOwnTeam() {//#7
		assertEquals(BLUE_WINS, game.move(0, 3, 1, 3));
	}
	
	@Test
	void redMovesToSameSquare() {//#6
		assertEquals(BLUE_WINS, game.move(1, 4, 1, 4));
	}
	
	@Test
	void redMovesToChokeOne() {//#8
		assertEquals(BLUE_WINS, game.move(1, 2, 2, 2));
	}
	
	@Test
	void redMovesToChokeTwo() {//#8
		assertEquals(BLUE_WINS, game.move(1, 3, 2, 3));
	}
	
	@Test
	void blueMovesToChokeThree() {//#8
		assertEquals(RED_WINS, game.move(4, 2, 3, 2));
	}
	
	@Test
	void blueMovesToChokeFour() {//#8
		assertEquals(RED_WINS, game.move(4, 3, 3, 3));
	}
	
	@Test
	void repetitionRuleBlueWins() {//#9
		game.move(1, 4, 2, 4);//red cap forward
		game.move(4, 5, 3, 5);//blue lie forward
		game.move(2, 4, 1, 4);//red cap backward
		game.move(4, 4, 3, 4);//blue ser forward
		assertEquals(BLUE_WINS, game.move(1, 4, 2, 4));//red cap forward
		assertEquals(GAME_OVER, game.move(3, 4, 2, 4));//blue ser forward
	}
	
	@Test
	void repetitionRuleRedWins() {//#9
		game.move(1, 4, 2, 4);//red cap forward
		game.move(4, 5, 3, 5);//blue lie forward
		game.move(2, 4, 1, 4);//red cap backward
		game.move(3, 5, 4, 5);//blue lie backward
		game.move(1, 1, 2, 1);//red lie forward
		assertEquals(RED_WINS, game.move(4, 5, 3, 5));//blue lie forward
		assertEquals(GAME_OVER, game.move(3, 4, 2, 4));//blue ser forward
	}
	
	//-----Striking tests-----//
	
	@Test
	void gamePlay1() {//#10
		game.move(1, 1, 2, 1);//red lie forward
		game.move(4, 1, 3, 1);//blue col forward
		assertEquals(OK, game.move(2, 1, 3, 1));//red lie strikes blue col, blue col takes red lie's square
		
		game.move(4, 4, 3, 4);//blue ser forward
		assertEquals(BLUE_WINS, game.move(1, 2, 2, 2));//red ser forward to choke point
		assertEquals(GAME_OVER, game.move(3, 4, 2, 4));//blue ser forward
	}
	
	@Test
	void gamePlay2() {//#10
		game.move(1, 1, 2, 1);//move red lie forward
		game.move(4, 1, 3, 1);//move blue col forward
		assertEquals(OK, game.move(2, 1, 3, 1));//red lie strikes blue col...col wins and moves to (2,1)
		game.move(2, 1, 1, 1);//move blue col forward
		assertEquals(OK, game.move(0, 1, 1, 1));//red lie strikes blue col...col wins and moves to (0,1)
		assertEquals(OK, game.move(0, 1, 0, 0));//blue col moves left and strikes red lie...col wins and moves to (0,0)
		game.move(1, 4, 2, 4);//move red cap forward
		game.move(4, 4, 3, 4);//move blue ser forward
		assertEquals(OK, game.move(2, 4, 3, 4));//red cap strikes blue ser...red cap wins and moves to (3,4)
		game.move(4, 5, 3, 5);//move blue lie forward
		game.move(1, 5, 2, 5);//move red col forward
		assertEquals(OK, game.move(3, 5, 2, 5));//blue lie strikes red col..red col wins and moves to (3,5)
		game.move(3, 4, 4, 4);//move red cap forward
		assertEquals(OK, game.move(5, 4, 4, 4));//blue col strikes red cap..blue col wins and moves to (4,4)
		game.move(3, 5, 4, 5);//move red col forward
		assertEquals(OK, game.move(5, 5, 4, 5));//blue cap strikes red col...red col wins and moves to (5,5)
		
		//completed 16 moves, 8 full turns...game continues in GAMMA
		
		game.move(5, 5, 5, 4);//move red col left
		assertEquals(OK, game.move(5, 3, 5, 4));//blue ser strikes red col..red col wins and moves to (5,3)
		assertEquals(OK, game.move(5, 3, 5, 2));//red col strikes blue ser..red col wins and moves to (5,2)
		assertEquals(OK, game.move(5, 1, 5, 2));//blue lie strikes red col..red col wins and moves to (5,1)
		assertEquals(OK, game.move(5, 1, 5, 0));//red col strikes blue lie..red col wins and moves to (5,0)
		game.move(4, 4, 3, 4);//move blue col forward
		assertEquals(RED_WINS, game.move(5, 0, 4, 0));//red col strikes blue flag, RED_WINS
		game.move(3, 4, 2, 4);//move blue col forward
	}
	
	@Test
	void gamePlay3() {//#10
		game.move(1, 1, 2, 1);//red lie forward
		game.move(4, 1, 3, 1);//blue col forward
		assertEquals(OK, game.move(2, 1, 3, 1));//red lie strikes blue col...blue col wins and moves to (2,1)
		game.move(2, 1, 2, 0);//blue col moves left
		game.move(0, 1, 1, 1);//red lie forward
		game.move(5, 1, 4, 1);//blue lie forward
		game.move(1, 1, 2, 1);//red lie forward
		game.move(4, 1, 3, 1);//blue lie forward
		assertEquals(OK, game.move(2, 1, 3, 1));//red lie strikes blue lie...both taken off board
		assertEquals(BLUE_WINS, game.move(2, 0, 1, 0));//blue col strikes red flag...BLUE_WINS
		assertEquals(GAME_OVER, game.move(1, 4, 2, 4));//red cap forward...GAME_OVER
	}
	
	@Test
	void blueTeamLosesAllMoveablePieces() {
		game.move(1, 1, 2, 1);//move red lie forward
		game.move(4, 1, 3, 1);//move blue col forward
		assertEquals(OK, game.move(2, 1, 3, 1));//red lie strikes blue col...col wins and moves to (2,1)
		
		game.move(2, 1, 1, 1);//move blue col forward
		assertEquals(OK, game.move(0, 1, 1, 1));//red lie strikes blue col...col wins and moves to (0,1)
		assertEquals(OK, game.move(0, 1, 0, 0));//blue col moves left and strikes red lie...col wins and moves to (0,0)
		
		game.move(1, 4, 2, 4);//move red cap forward
		game.move(4, 4, 3, 4);//move blue ser forward
		assertEquals(OK, game.move(2, 4, 3, 4));//red cap strikes blue ser...red cap wins and moves to (3,4)
		
		game.move(4, 5, 3, 5);//move blue lie forward
		game.move(1, 5, 2, 5);//move red col forward
		assertEquals(OK, game.move(3, 5, 2, 5));//blue lie strikes red col..red col wins and moves to (3,5)
		
		game.move(3, 4, 4, 4);//move red cap forward
		assertEquals(OK, game.move(5, 4, 4, 4));//blue col strikes red cap..blue col wins and moves to (4,4)
		
		game.move(3, 5, 4, 5);//move red col forward
		assertEquals(OK, game.move(5, 5, 4, 5));//blue cap strikes red col...red col wins and moves to (5,5)
		
		//completed 16 moves, 8 full turns...game continues in GAMMA
		
		game.move(5, 5, 5, 4);//move red col left
		assertEquals(OK, game.move(5, 3, 5, 4));//blue ser strikes red col..red col wins and moves to (5,3)
		assertEquals(OK, game.move(5, 3, 5, 2));//red col strikes blue ser..red col wins and moves to (5,2)
		assertEquals(OK, game.move(5, 1, 5, 2));//blue lie strikes red col..red col wins and moves to (5,1)
		assertEquals(OK, game.move(5, 1, 5, 0));//red col strikes blue lie..red col wins and moves to (5,0)
		
		game.move(4, 4, 3, 4);//blue col forward
		game.move(0, 4, 1, 4);//red col forward
		game.move(3, 4, 2, 4);//blue col forward
		assertEquals(OK, game.move(1, 4, 2, 4));//red col strikes blue col...both taken off board
		
		game.move(0, 0, 0, 1);//blue col right
		game.move(1, 3, 1, 4);//red mar right
		game.move(0, 1, 1, 1);//blue col forward
		assertEquals(OK, game.move(1, 2, 1, 1));//red ser strikes blue col...blue col wins and moves to (1,2)
		
		game.move(1, 2, 1, 3);//blue col right
		game.move(0, 5, 1, 5);//red cap forward
		assertEquals(OK, game.move(1, 3, 1, 4));//blue col strikes red mar...red mar wins and moves to (1,3)
		
		game.move(1, 3, 1, 4);//red mar right
		game.move(4, 3, 4, 4);//blue cap right
		game.move(1, 4, 2, 4);//red mar forward
		game.move(4, 4, 3, 4);//blue cap forward
		assertEquals(OK, game.move(2, 4, 3, 4));//red mar strikes blue cap...red mar wins and moves to (3,4)
		
		game.move(4, 2, 4, 3);//blue mar right
		game.move(3, 4, 4, 4);//red mar forward
		
		//blue mar strikes red mar...both are lost...blue doesn't have anymore movable pieces so RED_WINS
		assertEquals(RED_WINS, game.move(4, 3, 4, 4));
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
