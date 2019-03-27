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
 * A data structure that contains the row (a-h) and column (1-8) of a square on
 * the chess board.
 * 
 * NOTE: Students may modify this class through subclassing or adding protected, private,
 * and package protected (no modifiers), but not by adding public methods.
 * 
 * @version Feb 15, 2019
 */
public class Square
{
	private final char column;
	private final int row;
	
	/**
	 * Default constructor
	 */
	public Square(char column, int row)
	{
		this.row = row;
		this.column = column;
	}

	/**
	 * @return the column
	 */
	public char getColumn()
	{
		return column;
	}

	/**
	 * @return the row
	 */
	public int getRow()
	{
		return row;
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
		return result;
	}

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Square)) {
			return false;
		}
		Square other = (Square) obj;
		if (column != other.column) {
			return false;
		}
		if (row != other.row) {
			return false;
		}
		return true;
	}
}
