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
 * Exception that is used to indicate any error when determining whether a move may be
 * made or not.
 * @version Feb 18, 2019
 */
public class CMVException extends RuntimeException
{
	/**
	 * Sole constructor.
	 * @param message the reason for the error
	 */
	public CMVException(String message)
	{
		super(message);
	}
}
