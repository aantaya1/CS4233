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
package cmv;

/**
 * This class is a factory that creates Square instances. It has only one
 * static method that creates the instance of the Square or Square subclass.
 * Students must implement this method as appropriate for their solution.
 * 
 * The master tests used for grading will invoke this method to get the 
 * Square instances used in tests.
 * 
 * NOTE: Students must implement the single static method. You may NOT
 * change the signature of the method.
 * 
 * @version Mar 8, 2019
 */
public final class SquareFactory
{
	/**
	 * Return the instance of the Square used in the project.
	 * @param column
	 * @param row
	 * @return the Square instance
	 */
	public static Square makeSquare(char column, int row)
	{
		return new Square(column, row);
	}
}
