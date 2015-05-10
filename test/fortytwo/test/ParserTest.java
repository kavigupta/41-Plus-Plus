package fortytwo.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import fortytwo.compiler.parser.Parser;

public class ParserTest {
	@Test
	public void tokenizerTest() {
		assertEquals(new ArrayList<String>(Arrays.asList()),
				Parser.tokenize42(""));
		assertEquals(
				Arrays.asList("Define", "a", "number", "called", "_x", "."),
				Parser.tokenize42("Define a number called _x."));
		assertEquals(
				Arrays.asList("If", "4.0", "+", "4.0", "=", "2", ":",
						"Dothis", ";", "otherwise", ":", "Dothat", "."),
				Parser.tokenize42("If 4.0 + 4.0 = 2: Dothis; otherwise: Dothat."));
		assertEquals(
				Arrays.asList("Define", "a", "string", "called", "_x",
						"with", "a", "value", "of", "'()[]'\r\n'", "."),
				Parser.tokenize42("Define a string called _x with a value of '()[]\\'\\r\\n'."));
		assertEquals(
				Arrays.asList("Define", "a", "number", "called", "_x", "."),
				Parser.tokenize42("Define a [COMPILER ERROR HERE ''''''''''''] number called _x."));
		assertEquals(Arrays.asList("Set", "the", "value", "of", "_x", "to",
				"(1 + 2 * 3)", "."),
				Parser.tokenize42("Set the value of _x to (1+2*3)."));
		assertEquals(
				Arrays.asList("_technically_this_is_a_valid_variable_name'"),
				Parser.tokenize42("_technically_this_is_a_valid_variable_name'"));
		assertEquals(Arrays.asList("1", ">", "2", "+", "3", "<", "4", "-",
				"5", ">=", "6", "*", "7", "<=", "8", "/", "9", "=", "10",
				"//", "11", "/=", "12"),
				Parser.tokenize42("1>2+3<4-5>=6*7<=8/9=10//11/=12"));
	}
	// @Test
	// public void lineDefinitionTest() throws SyntaxMatchingError {
	// checkParse("Define a number called x");
	// checkParse("Define a number called x with a value of 2");
	// checkParse("Set the value of x to 2");
	// checkParse("Define an array called data with a length of (the value of x)");
	// }
	// private void checkParse(String toParse) throws SyntaxMatchingError {
	// assertEquals(toParse, Parser.intepretLine(toParse).toString());
	// }
}
