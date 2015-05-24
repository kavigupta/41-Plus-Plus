package fortytwo.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import fortytwo.compiler.parser.Parser;
import fortytwo.compiler.parser.Tokenizer;

public class TokenizerTest {
	@Test
	public void unescape() {
		assertEquals("\r\n\b\t\f", Tokenizer.unescape("\\r\\n\\b\\t\\f"));
		assertEquals("\r\n\b\t\f\u0045",
				Tokenizer.unescape("\\r\\n\\b\\t\\f\\u0045"));
		assertEquals(
				"\t\t\t\tArrays.asList(\"If\", \"4.0\", \"+\", \"4.0\", \"=\", \"2\", \":\",",
				Tokenizer.unescape("\\t\\t\\t\\tArrays.asList(\"If\", \"4.0\", \"+\", \"4.0\", \"=\", \"2\", \":\","));
	}
	@Test
	public void stringTest() {
		assertEquals(Arrays.asList("'abc def ghi'"),
				Parser.tokenize42("'abc def ghi'"));
		assertEquals(Arrays.asList("'( [)] 'unmatched'"),
				Parser.tokenize42("'( [)] \\'unmatched'"));
	}
	@Test
	public void groupingTest() {
		assertEquals(Arrays.asList("(([(]))", "(')[][')"),
				Parser.tokenize42("(([(])) (')[][')"));
	}
	@Test
	public void statementTests() {
		assertEquals(
				Arrays.asList("Define", "a", "number", "called", "_x", "."),
				Parser.tokenize42("Define a number called _x."));
		assertEquals(
				Arrays.asList("If", "4.0", "+", "4.0", "=", "2", ":",
						"Dothis", ".", "Otherwise", ":", "Dothat", "."),
				Parser.tokenize42("If 4.0 + 4.0 = 2: Dothis. Otherwise: Dothat."));
		assertEquals(
				Arrays.asList("Define", "a", "string", "called", "_x",
						"with", "a", "value", "of", "'()[]'\r\n'", "."),
				Parser.tokenize42("Define a string called _x with a value of '()[]\\'\\r\\n'."));
		assertEquals(
				Arrays.asList("Define", "a", "number", "called", "_x", "."),
				Parser.tokenize42("Define a [COMPILER ERROR HERE ''''''''''''] number called _x."));
		assertEquals(
				Arrays.asList("Set", "the", "value", "of", "_x", "to",
						"((1%2)+(2+3)*(3//4))", "."),
				Parser.tokenize42("Set the value of _x to ((1%2)+(2+3)*(3//4))."));
		assertEquals(
				Arrays.asList("_technically_this_is_a_valid_var''iable_name'"),
				Parser.tokenize42("_technically_this_is_a_valid_var''iable_name'"));
	}
}
