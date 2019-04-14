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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import junit.framework.TestCase;

/**
 * Unit tests for the OCR translator.
 * @version Mar 13, 2019
 */
class OCRTranslatorTest extends TestCase
{	
	@Test
	void nullInput() { // #0
		OCRTranslator t = new OCRTranslator();
		assertThrows(OCRException.class,() -> t.translate(null, "| |", "|_|"));
	}
	
	@Test
	void unequalStrings() { // #1
		OCRTranslator t = new OCRTranslator();
		assertThrows(OCRException.class,() -> t.translate("_", "| |", "|_|"));
	}
	
	@Test
	void emptyStrings() { // #2
		OCRTranslator t = new OCRTranslator();
		assertEquals("",t.translate("", "", ""));
	}
	
	@Test
	void stringsWithJustSpaces() { //#3
		OCRTranslator t = new OCRTranslator();
		assertEquals("",t.translate("   ", "   ", "   "));
	}
	
	@Test
	void one() { //#4
		OCRTranslator t = new OCRTranslator();
		assertEquals("1",t.translate("   ", "  |", "  |"));
	}
	
	@Test
	void two() { //#5
		OCRTranslator t = new OCRTranslator();
		assertEquals("2",t.translate(" _ ", " _|", "|_ "));
	}
	
	@Test
	void three() { //#6
		OCRTranslator t = new OCRTranslator();
		assertEquals("3",t.translate(" _ ", " _|", " _|"));
	}
	
	@Test
	void four() { //#7
		OCRTranslator t = new OCRTranslator();
		assertEquals("4",t.translate("   ", "|_|", "  |"));
	}
	
	@Test
	void five() { //#8
		OCRTranslator t = new OCRTranslator();
		assertEquals("5",t.translate(" _ ", "|_ ", " _|"));
	}
	
	@Test
	void six() { //#9
		OCRTranslator t = new OCRTranslator();
		assertEquals("6",t.translate(" _ ", "|_ ", "|_|"));
	}
	
	@Test
	void seven() { //#10
		OCRTranslator t = new OCRTranslator();
		assertEquals("7",t.translate(" _ ", "  |", "  |"));
	}
	
	@Test
	void eight() { //#11
		OCRTranslator t = new OCRTranslator();
		assertEquals("8",t.translate(" _ ", "|_|", "|_|"));
	}
	
	@Test
	void nine() { //#12
		OCRTranslator t = new OCRTranslator();
		assertEquals("9",t.translate(" _ ", "|_|", "  |"));
	}
	
	@Test
	void invalidDigit() { //#13
		OCRTranslator t = new OCRTranslator();
		assertThrows(OCRException.class,() -> t.translate(" __", "|_|", "  |"));
	}
	
	@Test
	void doubleDigit() { //#14
		OCRTranslator t = new OCRTranslator();
		assertEquals("91",t.translate(" _       ", "|_|     |", "  |     |"));
	}
	
	@Test
	void allDigitsNoExtraSpace() { //#15
		OCRTranslator t = new OCRTranslator();
		assertEquals("0123456789",t.translate(
				" _           _     _           _     _     _     _     _ ", 
				"| |     |    _|    _|   |_|   |_    |_      |   |_|   |_|", 
				"|_|     |   |_     _|     |    _|   |_|     |   |_|     |"));
	}
	
	@Test
	void allDigitsWithExtraSpace() { //#16
		OCRTranslator t = new OCRTranslator();
		assertEquals("0123456789",t.translate(
				" _             _          _           _     _     _             _     _ ", 
				"| |       |    _|         _|   |_|   |_    |_      |           |_|   |_|", 
				"|_|       |   |_          _|     |    _|   |_|     |           |_|     |"));
	}
	
	@Test
	void allDigitsWithExtraSpaceInvalidCharactor() { //#17
		OCRTranslator t = new OCRTranslator();
		assertThrows(OCRException.class,() -> t.translate(
				" _             _          _           _     _     _             _     _    |", 
				"| |       |    _|         _|   |_|   |_    |_      |           |_|   |_|   |", 
				"|_|       |   |_          _|     |    _|   |_|     |           |_|     |   |"));
	}
	
	@Test
	void notEnoughSpaceBetweenDigits() { //#18
		OCRTranslator t = new OCRTranslator();
		assertThrows(OCRException.class,() -> t.translate(
				" _       _     _           _     _     _     _     _ ", 
				"| | |    _|    _|   |_|   |_    |_      |   |_|   |_|", 
				"|_| |   |_     _|     |    _|   |_|     |   |_|     |"));
	}
	
	@Test
	void invalidCharacterOne() { //#19
		OCRTranslator t = new OCRTranslator();
		assertThrows(OCRException.class,() -> t.translate("a", "|_|", "  |"));
	}
	
	@Test
	void allInvalidCharactersOne() { //#20
		OCRTranslator t = new OCRTranslator();
		assertThrows(OCRException.class,() -> t.translate("---", "---", "---"));
	}
	
	@Test
	void allInvalidCharactersTwo() { //#21
		OCRTranslator t = new OCRTranslator();
		assertThrows(OCRException.class,() -> t.translate("AAA", "BBB", "CCC"));
	}
}







