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
package strategy.alpha;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import strategy.StrategyGame;
import static strategy.StrategyGame.Version.*;
import static strategy.required.StrategyGameFactory.*;
import static strategy.StrategyGame.MoveResult.*;

/**
 * Test cases for Alpha Strategy.
 * @version Mar 18, 2019
 */
class AlphaStrategyTest
{
	private StrategyGame game;
	
	@BeforeEach
	private void setup()
	{
		game = makeGame(ALPHA, null);
	}
	
	@Test
	void validGame()
	{
		assertEquals(RED_WINS, game.move(0, 0, 1, 0));
	}

	@Test
	void wrongMove()
	{
		assertEquals(BLUE_WINS, game.move(0, 0, 0, 1));
	}
}
