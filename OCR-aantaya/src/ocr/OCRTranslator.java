/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2019 Alexander Antaya
 *******************************************************************************/
package ocr;

/**
 * This class has a single method that will translate OCR digits to a string of
 * text digits that correspond to the OCR digits.
 * 
 * @version Mar 13, 2019
 */
public class OCRTranslator
{
	/**
	 * Default constructor. You may not add parameters to this. This is
	 * the only constructor for this class and is what the master tests will use.
	 */
	public OCRTranslator()
	{
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Translate a string of OCR digits to a corresponding string of text
	 * digits. OCR digits are represented as three rows of characters (|, _, and space).
	 * 
	 * Basically what the algorithm does is it will index through all strings starting from the left, together and one at a time, until 
	 * it finds a "|" in the middle and bottom strings at the same index. This means we have found a possible digit. It then determines
	 * whether or not it is at the beginning or end of a digit. If at the beginning, concatenate the current char with the next two and 
	 * pass those three strings (top, middle, and bottom) to a helper method to determine what digit they make (if any). If at the end, 
	 * it will do the same thing except go back two chars first. 
	 * The reason why we need to check if we are at the beginning or end is because of digits like "1" which only have segments at the 
	 * end of the digit.
	 * 
	 * I used the digit definition found here for seven segment digits (https://www.electronics-tutorials.ws/blog/7-segment-display-tutorial.html).
	 * Basically, the only not-so-intuitive part of it is that for the digit "1", the two "|" segments are all the way to the right, rather than the 
	 * middle or left.
	 * 
	 * @param s1 the top row of the OCR input
	 * @param s2 the middle row of the OCR input
	 * @param s3 the third row of the OCR input
	 * @return a String containing the digits corresponding
	 */
	public String translate(String s1, String s2, String s3)
	{
		//Make sure we don't get any null input
		if(s1 == null || s2 == null || s3 == null) throw new OCRException("An input string was null");
		
		//If the three strings are not of the same length throw exception
		if(!((s1.length() == s2.length()) && (s2.length() == s3.length()))) throw new OCRException("Length of input strings are not equal");
		
		//If any of the input strings are just all white space or empty, return ""
		if(s1.trim().isEmpty() && s2.trim().isEmpty() && s3.trim().isEmpty()) return "";
		
		StringBuilder result = new StringBuilder();
		
		//Loop to iterate over every char in the strings
		for(int i=0; i<s1.length(); i++) {	
			char charTop=s1.charAt(i), charMiddle=s2.charAt(i), charBottom=s3.charAt(i);
			
			//Check to make sure we are using valid characters
			if(!(charTop == ' ' || charTop == '|' || charTop == '_')) throw new OCRException("Found Invalid Character");
			if(!(charMiddle == ' ' || charMiddle == '|' || charMiddle == '_')) throw new OCRException("Found Invalid Character");
			if(!(charBottom == ' ' || charBottom == '|' || charBottom == '_')) throw new OCRException("Found Invalid Character");
			
			//Meaning we have found a digit and we are not in the middle of it
			if(charMiddle == '|' || charBottom == '|') {
				StringBuilder top = new StringBuilder();
				StringBuilder middle = new StringBuilder();
				StringBuilder bottom = new StringBuilder();
				
				//If this is true, we are at the beginning of a digit so make digit by moving right and index i
				if((i < s1.length() - 2) && (s1.charAt(i+2) != ' ' || s2.charAt(i+2) != ' ' || s3.charAt(i+2) != ' ')) {
					for(int j=0; j<3; j++) {
						charTop=s1.charAt(i); 
						charMiddle=s2.charAt(i); 
						charBottom=s3.charAt(i);
						
						top.append(charTop);
						middle.append(charMiddle);
						bottom.append(charBottom);
						
						i++;
					}
				}else {
					//We are at the end of a digit so make the digit by going back two spaces and moving to the right this will only 
					//	be reached for the digits 1, 3, 7 (possibly invalid digits too) because they do not have any "|" segments at 
					//	the beginning
					for(int j=2; j>=0; j--) {
						charTop=s1.charAt(i-j); 
						charMiddle=s2.charAt(i-j); 
						charBottom=s3.charAt(i-j);
						
						top.append(charTop);
						middle.append(charMiddle);
						bottom.append(charBottom);
					}
				}
				
				//Once we have created the three segments try and convert them to a digit, if it is a valid digit we will append 
				//	it to the result, if not valid then convertDigit will throw an exception
				result.append(convertDigit(top.toString(), middle.toString(), bottom.toString()));
			}
		}
		return result.toString();
	}
	
	//Simple helper method to convert the three strings to a digit, if it is not a valid 0-9 digit, throw exception
	private String convertDigit(String top, String middle, String bottom) {
		if((top.equals(" _ ")) && (middle.equals("| |")) && (bottom.equals("|_|"))) return "0";
		else if((top.equals("   ")) && (middle.equals("  |")) && (bottom.equals("  |"))) return "1";
		else if((top.equals(" _ ")) && (middle.equals(" _|")) && (bottom.equals("|_ "))) return "2";
		else if((top.equals(" _ ")) && (middle.equals(" _|")) && (bottom.equals(" _|"))) return "3";
		else if((top.equals("   ")) && (middle.equals("|_|")) && (bottom.equals("  |"))) return "4";
		else if((top.equals(" _ ")) && (middle.equals("|_ ")) && (bottom.equals(" _|"))) return "5";
		else if((top.equals(" _ ")) && (middle.equals("|_ ")) && (bottom.equals("|_|"))) return "6";
		else if((top.equals(" _ ")) && (middle.equals("  |")) && (bottom.equals("  |"))) return "7";
		else if((top.equals(" _ ")) && (middle.equals("|_|")) && (bottom.equals("|_|"))) return "8";
		else if((top.equals(" _ ")) && (middle.equals("|_|")) && (bottom.equals("  |"))) return "9";
		else throw new OCRException("Found Invalid Character");
	}
}












