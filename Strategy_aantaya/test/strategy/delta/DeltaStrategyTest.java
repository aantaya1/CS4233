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
		assertEquals(RED_WINS, game.move(6, 5, 4, 5));//blue lie forward
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
	
	@Test
	void marshalStrikesSpy() {
		game.move(3, 0, 4, 0);//red mar forward
		game.move(6, 1, 5, 1);//blue spy forward
		game.move(4, 0, 4, 1);//red mar right
		game.move(6, 5, 5, 5);//blue lie forward
		assertEquals(OK, game.move(4, 1, 5, 1));//red mar strikes blue spy...mar wins and moves to (5,1)
	}
	
	@Test
	void spyStrikesMarshal() {
		game.move(3, 0, 4, 0);//red mar forward
		game.move(6, 1, 5, 1);//blue spy forward
		game.move(4, 0, 4, 1);//red mar right
		assertEquals(OK, game.move(5, 1, 4, 1));//blue spy strikes red mar...spy wins and moves to (4,1)
		game.move(3, 4, 4, 4);//move red cap forward
		game.move(4, 1, 4, 0);//move blue spy left
		game.move(4, 4, 5, 4);//move red cap forward
		game.move(4, 0, 3, 0);//blue spy forward (in front of red flag)
		game.move(3, 5, 4, 5);//red lie forward
		assertEquals(BLUE_WINS, game.move(3, 0, 2, 0));//blue spy strikes red flag...BLUE_WINS
	}
	
	@Test
	void scoutStrikesScout() {
		game.move(3, 8, 5, 8);//red scout forward two
		assertEquals(OK, game.move(6, 8, 5, 8));//blue scout strikes red scout..both taken off
	}
	
	@Test
	void generalStrikesBomb() {
		game.move(3, 8, 5, 8);//red scout forward two
		assertEquals(OK, game.move(6, 8, 5, 8));//blue scout strikes red scout..both taken off
		game.move(3, 9, 3, 8);//red gen left
		game.move(6, 9, 5, 9);//blue gen forward
		game.move(3, 8, 4, 8);//red gen forward
		game.move(6, 5, 5, 5);//blue lie forward
		game.move(4, 8, 5, 8);//red gen forward
		game.move(6, 4, 5, 4);//blue cap forward
		game.move(5, 8, 6, 8);//red gen forward (in front of bomb)
		game.move(6, 0, 5, 0);//blue mar forward
		assertEquals(OK, game.move(6, 8, 7, 8));//red gen strikes bomb...red gen taken off board
		game.move(5, 4, 4, 4);//blue cap forward
		assertEquals(BLUE_WINS, game.move(7, 8, 6, 8));//red gen backward...but gen was taken off board by bomb...BLUE_WINS
	}
	
	@Test
	void minerStrikesBomb() {
		game.move(3, 8, 5, 8);//red scout forward two
		assertEquals(OK, game.move(6, 8, 5, 8));//blue scout strikes red scout..both taken off
		game.move(3, 7, 3, 8);//red miner left
		game.move(6, 9, 5, 9);//blue gen forward
		game.move(3, 8, 4, 8);//red miner forward
		game.move(6, 5, 5, 5);//blue lie forward
		game.move(4, 8, 5, 8);//red miner forward
		game.move(6, 4, 5, 4);//blue cap forward
		game.move(5, 8, 6, 8);//red miner forward (in front of bomb)
		game.move(6, 0, 5, 0);//blue mar forward
		assertEquals(OK, game.move(6, 8, 7, 8));//red miner strikes bomb...blue bomb taken off board
		game.move(5, 4, 4, 4);//blue cap forward
		assertEquals(OK, game.move(7, 8, 6, 8));//red miner backward
	}
	
	@Test
	void gamePlayOne() {
		game.move(3, 0, 4, 0);//red mar forward
		game.move(6, 0, 5, 0);//blue mar forward
		assertEquals(OK, game.move(4, 0, 5, 0));//red mar strikes blue mar...both taken off board

		game.move(6, 1, 5, 1);//blue spy forward
		game.move(3, 1, 4, 1);//red spy forward
		assertEquals(OK, game.move(5, 1, 4, 1));//blue spy strikes red spy...both taken off board

		game.move(3, 4, 4, 4);//red cap forward
		game.move(6, 4, 5, 4);//blue cap forward
		assertEquals(OK, game.move(4, 4, 5, 4));//red cap strikes blue cap...both taken off board
		
		game.move(6, 5, 5, 5);//blue lie forward
		game.move(3, 5, 4, 5);//red lie forward
		assertEquals(OK, game.move(5, 5, 4, 5));//blue lie strikes red lie...both taken off board
		
		game.move(3, 8, 5, 8);//red scout forward two squares
		assertEquals(OK, game.move(6, 8, 5, 8));//blue scout strikes red scout...both taken off board
		
		game.move(3, 9, 4, 9);//red gen forward
		game.move(6, 9, 5, 9);//blue gen forward
		assertEquals(OK, game.move(4, 9, 5, 9));//red gen strikes blue gen...both taken off board
		
		game.move(6, 2, 6, 1);//blue col left
		game.move(3, 2, 3, 1);//red col left
		game.move(6, 1, 5, 1);//blue col forward
		game.move(3, 1, 4, 1);//red col forward
		assertEquals(OK, game.move(5, 1, 4, 1));//blue col strikes red col...both taken off board
		
		game.move(3, 3, 3, 4);//red maj right
		game.move(6, 3, 6, 4);//blue maj right
		game.move(3, 4, 4, 4);//red maj forward
		game.move(6, 4, 5, 4);//blue maj forward
		assertEquals(OK, game.move(4, 4, 5, 4));//red maj strikes blue maj...both taken off board
		
		game.move(6, 6, 6, 5);//blue ser left
		game.move(3, 6, 3, 5);//red ser left
		game.move(6, 5, 5, 5);//blue ser forward
		game.move(3, 5, 4, 5);//red ser forward
		assertEquals(OK, game.move(5, 5, 4, 5));//blue ser strikes red ser...both taken off board
	}
	
	
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
				PieceType.COLONEL, PieceType.MAJOR, PieceType.MAJOR, PieceType.CAPTAIN, PieceType.CAPTAIN, //row 0
				PieceType.CAPTAIN, PieceType.LIEUTENANT, PieceType.LIEUTENANT, PieceType.LIEUTENANT, PieceType.SERGEANT, 
				
				PieceType.SERGEANT, PieceType.SERGEANT, PieceType.MINER, PieceType.MINER, PieceType.MINER,//row 1
				PieceType.MINER, PieceType.SCOUT, PieceType.SCOUT, PieceType.SCOUT, PieceType.SCOUT,
				
				PieceType.FLAG, PieceType.SCOUT, PieceType.SCOUT, PieceType.SCOUT, PieceType.BOMB,//row 2
				PieceType.BOMB, PieceType.BOMB, PieceType.BOMB, PieceType.BOMB, PieceType.BOMB,
				
				PieceType.MARSHAL, PieceType.SPY, PieceType.COLONEL, PieceType.MAJOR, PieceType.CAPTAIN,//row 3
				PieceType.LIEUTENANT, PieceType.SERGEANT, PieceType.MINER, PieceType.SCOUT, PieceType.GENERAL);
		
		List<PieceType> blueTeam = Arrays.asList(
				PieceType.MARSHAL, PieceType.SPY, PieceType.COLONEL, PieceType.MAJOR, PieceType.CAPTAIN,//row 6
				PieceType.LIEUTENANT, PieceType.SERGEANT, PieceType.MINER, PieceType.SCOUT, PieceType.GENERAL,

				PieceType.FLAG, PieceType.SCOUT, PieceType.SCOUT, PieceType.SCOUT, PieceType.BOMB,//row 7
				PieceType.BOMB, PieceType.BOMB, PieceType.BOMB, PieceType.BOMB, PieceType.BOMB,

				PieceType.SERGEANT, PieceType.SERGEANT, PieceType.MINER, PieceType.MINER, PieceType.MINER,//row 8
				PieceType.MINER, PieceType.SCOUT, PieceType.SCOUT, PieceType.SCOUT, PieceType.SCOUT,
				 
				PieceType.COLONEL, PieceType.MAJOR, PieceType.MAJOR, PieceType.CAPTAIN, PieceType.CAPTAIN,//row 9
				PieceType.CAPTAIN, PieceType.LIEUTENANT, PieceType.LIEUTENANT, PieceType.LIEUTENANT, PieceType.SERGEANT);
		 
		return new MyTestBoard(redTeam, blueTeam);
	}
}
