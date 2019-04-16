/**
 * 
 */
package strategy.aantaya.version.delta;

import strategy.Board;
import strategy.StrategyGame;
import strategy.aantaya.Square;
import strategy.aantaya.StrategyGameTemplate;
import strategy.aantaya.version.gamma.BoardImpl;

/**
 * @author Owner
 *
 */
public class DeltaStrategyGame extends StrategyGameTemplate implements StrategyGame {

	private BoardImpl board;
	private boolean isRedTurn;
	private boolean gameIsOver;
	
	
	public DeltaStrategyGame(Board b) {
		this.board = new BoardImpl(b);
		this.isRedTurn = true;
		this.gameIsOver = false;
	}
	
	
	/* (non-Javadoc)
	 * @see strategy.aantaya.StrategyGameTemplate#move(int, int, int, int)
	 */
	@Override
	public MoveResult move(int fr, int fc, int tr, int tc) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see strategy.aantaya.StrategyGameTemplate#isValidMove(strategy.aantaya.Square, strategy.aantaya.Square)
	 */
	@Override
	public boolean isValidMove(Square squareFrom, Square squareTo) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see strategy.aantaya.StrategyGameTemplate#strike(strategy.aantaya.Square, strategy.aantaya.Square)
	 */
	@Override
	public MoveResult strike(Square squareFrom, Square squareTo) {
		// TODO Auto-generated method stub
		return null;
	}
}
