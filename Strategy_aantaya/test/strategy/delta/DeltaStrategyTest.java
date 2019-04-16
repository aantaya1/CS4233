/**
 * 
 */
package strategy.delta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static strategy.StrategyGame.MoveResult.BLUE_WINS;
import static strategy.StrategyGame.MoveResult.GAME_OVER;
import static strategy.StrategyGame.MoveResult.OK;
import static strategy.StrategyGame.MoveResult.RED_WINS;
import static strategy.StrategyGame.Version.DELTA;
import static strategy.required.StrategyGameFactory.makeGame;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import strategy.Board;
import strategy.StrategyGame;
import strategy.Piece.PieceType;
import strategy.delta.MyTestBoard;

/**
 * @author Owner
 *
 */
public class DeltaStrategyTest {

	private StrategyGame game;
	
	@BeforeEach
	private void setup() {
		game = makeGame(DELTA, createBoard());
	}
	
	//-----Game mechanics tests-----//
	
	@Test
	void redMovesOutOfBounds() {//#2
		assertEquals(BLUE_WINS, game.move(3, 0, 3, -1));
	}
	
	@Test
	void redMoveFlag() {//#3
		assertEquals(BLUE_WINS, game.move(1, 6, 2, 6));
	}
	
	@Test
	void redMoveTwoSquares() {//#4
		assertEquals(BLUE_WINS, game.move(3, 5, 5, 5));
	}
	
	@Test
	void redMovesFromSquareWithNoPiece() {//#1
		assertEquals(BLUE_WINS, game.move(4, 4, 5, 4));
	}
	
	@Test
	void blueMovesFromSquareWithNoPiece() {//#1
		game.move(3, 0, 4, 0);//red must go first
		assertEquals(RED_WINS, game.move(5, 5, 4, 5));
	}
	
	@Test
	void redMoveDiagonally() {//#5
		assertEquals(BLUE_WINS, game.move(3, 4, 4, 5));
	}
	
	@Test
	void blueMoveMultipleSquaresNonScout() {//#4
		game.move(3, 0, 4, 0);//red must go first
		assertEquals(RED_WINS, game.move(6, 5, 4, 5));
	}
	
	@Test
	void redMoveScoutMultipleSquares() {//#10
		assertEquals(OK, game.move(3, 8, 5, 8));//move red scout two squares forward
		assertEquals(RED_WINS, game.move(6, 5, 4, 5));//move blue lie forward two squares
		assertEquals(GAME_OVER, game.move(3, 0, 4, 0));//move red mar forward
	}
	
	@Test
	void redMoveScoutMultipleSquaresNotClearPath() {//#10
		assertEquals(BLUE_WINS, game.move(3, 8, 6, 8));//move red scout three squares forward
		assertEquals(GAME_OVER, game.move(6, 0, 5, 0));//move blue mar forward
	}
	
	@Test
	void redMoveToSameSquare() {//#6
		assertEquals(BLUE_WINS, game.move(1, 0, 1, 0));
	}
	
	@Test
	void blueMoveToSquareOccupiedByOwnTeam() {//#7
		game.move(3, 5, 4, 5);//red must go first
		assertEquals(RED_WINS, game.move(6, 5, 6, 5));
	}
	
	@Test
	void redMoveToSquareOccupiedByOwnTeam() {//#7
		assertEquals(BLUE_WINS, game.move(2, 0, 3, 0));
	}
	
	@Test
	void redMovesToChokeOne() {//#8
		assertEquals(BLUE_WINS, game.move(3, 2, 4, 2));
	}
	
	@Test
	void redMovesToChokeTwo() {//#8
		assertEquals(BLUE_WINS, game.move(3, 3, 4, 3));
	}
	
	@Test
	void blueMovesToChokeThree() {//#8
		game.move(3, 5, 4, 5);//red must go first
		assertEquals(RED_WINS, game.move(6, 2, 5, 2));
	}
	
	@Test
	void blueMovesToChokeFour() {//#8
		game.move(3, 5, 4, 5);//red must go first
		assertEquals(RED_WINS, game.move(6, 3, 5, 3));
	}
	
	@Test
	void redMovesToChokeFive() {//#8
		assertEquals(BLUE_WINS, game.move(3, 6, 4, 6));
	}
	
	@Test
	void redMovesToChokeSix() {//#8
		assertEquals(BLUE_WINS, game.move(3, 7, 4, 7));
	}
	
	@Test
	void blueMovesToChokeSeven() {//#8
		game.move(3, 5, 4, 5);//red must go first
		assertEquals(RED_WINS, game.move(6, 6, 5, 6));
	}
	
	@Test
	void blueMovesToChokeEight() {//#8
		game.move(3, 5, 4, 5);//red must go first
		assertEquals(RED_WINS, game.move(6, 7, 5, 7));
	}
	
	@Test
	void repetitionRuleBlueWins() {//#9
		game.move(3, 4, 4, 4);//red cap forward
		game.move(6, 5, 5, 5);//blue lie forward
		game.move(4, 4, 3, 4);//red cap backward
		game.move(6, 0, 5, 0);//blue mar forward
		assertEquals(BLUE_WINS, game.move(3, 4, 4, 4));//red cap forward
		assertEquals(GAME_OVER, game.move(6, 1, 5, 1));//blue ser forward
	}
	
	@Test
	void repetitionRuleRedWins() {//#9
		game.move(3, 4, 4, 4);//red cap forward
		game.move(6, 5, 5, 5);//blue lie forward
		game.move(4, 4, 3, 4);//red cap backward
		game.move(5, 5, 6, 5);//blue lie backward
		game.move(3, 0, 4, 0);//red mar forward
		assertEquals(RED_WINS, game.move(6, 5, 5, 5));//blue lie forward
		assertEquals(GAME_OVER, game.move(3, 1, 4, 1));//blue ser forward
	}
	
	//-----Striking Tests-----//
	
	
	
	
	/**
	 * This will create the following board

         0       1       2       3       4       5        6       7       8       9
	  +-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
	  |       |       |       |       |       |       |       |       |       |       |
	9 |  FLA  |  LIE  |  SER  |  MAR  |  CAP  |  COL  |  FLA  |  LIE  |  SER  |  MAR  |  9
	  |       |       |       |       |       |       |       |       |       |       |
	  +-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
	  |       |       |       |       |       |       |       |       |       |       |
	8 |  FLA  |  LIE  |  SER  |  MAR  |  CAP  |  COL  |  FLA  |  LIE  |  SER  |  MAR  |  8
	  |       |       |       |       |       |       |       |       |       |       |
	  +-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
	  |       |       |       |       |       |       |       |       |       |       |
	7 |  FLA  |  LIE  |  SER  |  MAR  |  CAP  |  COL  |  FLA  |  LIE  |  SER  |  MAR  |  7
	  |       |       |       |       |       |       |       |       |       |       |
	  +-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
	  |       |       |       |       |       |       |       |       |       |       |
	6 |  FLA  |  LIE  |  SER  |  MAR  |  CAP  |  COL  |  FLA  |  LIE  |  SER  |  MAR  |  6
	  |       |       |       |       |       |       |       |       |       |       |
	  +-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
	  |       |       |XXXXXXX|XXXXXXX|       |       |XXXXXXX|XXXXXXX|       |       |
	5 |       |       |XXXXXXX|XXXXXXX|       |       |XXXXXXX|XXXXXXX|       |       |  5
	  |       |       |XXXXXXX|XXXXXXX|       |       |XXXXXXX|XXXXXXX|       |       |
	  +-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
	  |       |       |XXXXXXX|XXXXXXX|       |       |XXXXXXX|XXXXXXX|       |       |
	4 |       |       |XXXXXXX|XXXXXXX|       |       |XXXXXXX|XXXXXXX|       |       |  4
	  |       |       |XXXXXXX|XXXXXXX|       |       |XXXXXXX|XXXXXXX|       |       |
	  +-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
	  |       |       |       |       |       |       |       |       |       |       |
	3 |  FLA  |  LIE  |  SER  |  MAR  |  CAP  |  COL  |  FLA  |  LIE  |  SER  |  MAR  |  3
	  |       |       |       |       |       |       |       |       |       |       |
	  +-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
	  |       |       |       |       |       |       |       |       |       |       |
	2 |  FLA  |  LIE  |  SER  |  MAR  |  CAP  |  COL  |  FLA  |  LIE  |  SER  |  MAR  |  2
	  |       |       |       |       |       |       |       |       |       |       |
	  +-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
	  |       |       |       |       |       |       |       |       |       |       |
	1 |  FLA  |  LIE  |  SER  |  MAR  |  CAP  |  COL  |  FLA  |  LIE  |  SER  |  MAR  |  1
	  |       |       |       |       |       |       |       |       |       |       |
	  +-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
	  |       |       |       |       |       |       |       |       |       |       |
	0 |  LIE  |  LIE  |  SER  |  SER  |  COL  |  CAP  |  FLA  |  LIE  |  SER  |  MAR  |  0
	  |       |       |       |       |       |       |       |       |       |       |
	  +-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
	 *    0       1       2      3       4       5       6       7        8       9
	 * 
	 */
	private Board createBoard() {
		List<PieceType> redTeam = Arrays.asList(
				PieceType.COLONEL, PieceType.MAJOR, PieceType.MAJOR, PieceType.CAPTAIN, PieceType.CAPTAIN, 
				PieceType.CAPTAIN, PieceType.LIEUTENANT, PieceType.LIEUTENANT, PieceType.LIEUTENANT, PieceType.SERGEANT, 
				PieceType.SERGEANT, PieceType.SERGEANT, PieceType.MINER, PieceType.MINER, PieceType.MINER,
				PieceType.MINER, PieceType.FLAG, PieceType.SCOUT, PieceType.SCOUT, PieceType.SCOUT,
				PieceType.SCOUT, PieceType.SCOUT, PieceType.SCOUT, PieceType.SCOUT, PieceType.BOMB,
				PieceType.BOMB, PieceType.BOMB, PieceType.BOMB, PieceType.BOMB, PieceType.BOMB,
				PieceType.MARSHAL, PieceType.GENERAL, PieceType.COLONEL, PieceType.MAJOR, PieceType.CAPTAIN,
				PieceType.LIEUTENANT, PieceType.SERGEANT, PieceType.MINER, PieceType.SCOUT, PieceType.SPY);
		
		List<PieceType> blueTeam = Arrays.asList(
				PieceType.LIEUTENANT, PieceType.SERGEANT, PieceType.MINER, PieceType.SCOUT, PieceType.SPY,
				PieceType.MARSHAL, PieceType.GENERAL, PieceType.COLONEL, PieceType.MAJOR, PieceType.CAPTAIN,
				PieceType.BOMB, PieceType.BOMB, PieceType.BOMB, PieceType.BOMB, PieceType.BOMB,
				PieceType.SCOUT, PieceType.SCOUT, PieceType.SCOUT, PieceType.SCOUT, PieceType.BOMB,
				PieceType.MINER, PieceType.FLAG, PieceType.SCOUT, PieceType.SCOUT, PieceType.SCOUT,
				PieceType.SERGEANT, PieceType.SERGEANT, PieceType.MINER, PieceType.MINER, PieceType.MINER,
				PieceType.CAPTAIN, PieceType.LIEUTENANT, PieceType.LIEUTENANT, PieceType.LIEUTENANT, PieceType.SERGEANT, 
				PieceType.COLONEL, PieceType.MAJOR, PieceType.MAJOR, PieceType.CAPTAIN, PieceType.CAPTAIN);
		 
		return new MyTestBoard(redTeam, blueTeam);
	}
}
