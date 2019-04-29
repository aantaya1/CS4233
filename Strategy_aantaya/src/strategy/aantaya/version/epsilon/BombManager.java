/**
 * 
 */
package strategy.aantaya.version.epsilon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import strategy.aantaya.Square;

/**
 * @author Owner
 *
 */
public class BombManager {
	private Map<Square, Integer> bombs;
	private int NUM_CHARGES = 2;
	
	BombManager(List<Square> _bombs){
		bombs = new HashMap<Square, Integer>();
		
		for(Square s : _bombs) bombs.put(s, NUM_CHARGES);
	}
	
	/*
	 * Decrement the 'remaining charges' by one
	 * 
	 * If the bomb only has one charge left, the remove it from the hashmap and return true (which means remove the bomb from the board)
	 * Else, decrement the charges by one and return false
	 */
	public boolean explode(Square bomb) {
		if(bombs.containsKey(bomb)) {
			if(bombs.get(bomb) == 1) {
				bombs.remove(bomb);
				return true;
			}else {
				bombs.put(bomb, bombs.get(bomb) - 1);
				return false;
			}
		}
		return true;
	}
	
	public boolean isBomb(Square s) {
		return (bombs.containsKey(s)) ? true : false;
	}
}
