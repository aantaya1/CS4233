/**
 * 
 */
package strategy.aantaya;

import strategy.StrategyGame;
import strategy.StrategyGame.MoveResult;

/**
 * @author Owner
 *
 */
public abstract class StrategyGameTemplate implements StrategyGame {
	public abstract MoveResult move(int fr, int fc, int tr, int tc);
	
	public abstract boolean isValidMove(Square squareFrom, Square squareTo);
	
	public abstract MoveResult strike(Square squareFrom, Square squareTo);
}
