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
 * Exception class for the method not implemented that is the default for some 
 * interface methods.
 * @version Jan 25, 2019
 */
public class MethodNotImplementedException extends RuntimeException
{
	/**
	 * Message is required
	 * @param message
	 */
	public MethodNotImplementedException(String message)
	{
		super(message);
	}
}
