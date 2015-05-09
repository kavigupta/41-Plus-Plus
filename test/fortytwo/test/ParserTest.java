package fortytwo.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fortytwo.compiler.errors.SyntaxMatchingError;
import fortytwo.compiler.parser.Parser;

public class ParserTest {
	@Test
	public void lineDefinitionTest() throws SyntaxMatchingError {
		checkParse("Define a number called x");
		checkParse("Define a number called x with a value of 2");
		checkParse("Set the value of x to 2");
		checkParse("Define an array called data with a length of (the value of x)");
	}
	private void checkParse(String toParse) throws SyntaxMatchingError {
		assertEquals(toParse, Parser.intepretLine(toParse).toString());
	}
}
