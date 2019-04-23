/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016 Gary F. Pollice
 *******************************************************************************/

package strategy.master;

import java.util.*;

/**
 * Description
 * @version Apr 17, 2019
 */
public class Move
{
	public int fRow, fCol, tRow, tCol;
	
	public Move(int fr, int fc, int tr, int tc)
	{
		this.fRow = fr;
		this.fCol = fc;
		this.tRow = tr;
		this.tCol = tc;
	}	
	
	public static List<Move> makeMoves(int... x)
	{
		List<Move> moves = new LinkedList<Move>();
		int i = 0;
		while (i < x.length) {
			moves.add(new Move(x[i++], x[i++], x[i++], x[i++]));
		}
		return moves;
	}
}
