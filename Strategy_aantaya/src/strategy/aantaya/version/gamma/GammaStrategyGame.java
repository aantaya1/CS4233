/**
 * 
 */
package strategy.aantaya.version.gamma;

import strategy.Board;
import strategy.StrategyGame;
import strategy.aantaya.version.beta.BoardImpl;

/**
 * @author Owner
 *
 */
public class GammaStrategyGame implements StrategyGame {

	private BoardImpl board;
	private int numTurns;
	private boolean isRedTurn;
	private boolean gameIsOver;
	
	public GammaStrategyGame(Board b) {
		this.board = new BoardImpl(b);
		this.isRedTurn = true;
		this.gameIsOver = false;
	}
	
	
}
