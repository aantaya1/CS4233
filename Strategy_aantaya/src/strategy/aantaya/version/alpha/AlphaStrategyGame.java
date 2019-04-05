/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016-2017 Gary F. Pollice
 *******************************************************************************/
package strategy.aantaya.version.alpha;

import strategy.StrategyGame;
import static strategy.StrategyGame.MoveResult.*;

/**
 * Description
 * @version Mar 18, 2019
 */
public class AlphaStrategyGame implements StrategyGame
{
	public MoveResult move(int fr, int fc, int tr, int tc)
	{
		return
			(fr == 0 && fc == 0 && tr == 1 && tc == 0) ? RED_WINS : BLUE_WINS;
	}
}
