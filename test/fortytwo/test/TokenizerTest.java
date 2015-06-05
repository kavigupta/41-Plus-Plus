package fortytwo.test;

import static fortytwo.test.Utilities.assertCorrectTokenization;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fortytwo.compiler.parser.Tokenizer;

public class TokenizerTest {
	@Test
	public void unescapeTest() {
		assertEquals("\r\n\b\t\f", Tokenizer.unescape("\\r\\n\\b\\t\\f"));
		assertEquals("\r\n\b\t\f\u0045",
				Tokenizer.unescape("\\r\\n\\b\\t\\f\\u0045"));
		assertEquals(
				"\t\t\t\tArrays.asList(\"If\", \"4.0\", \"+\", \"4.0\", \"=\", \"2\", \":\",",
				Tokenizer.unescape("\\t\\t\\t\\tArrays.asList(\"If\", \"4.0\", \"+\", \"4.0\", \"=\", \"2\", \":\","));
	}
	@Test
	public void stringParseTest() {
		assertCorrectTokenization("'abc def ghi'", "'abc def ghi'");
		assertCorrectTokenization("'( [)] \\'unmatched'",
				"'( [)] 'unmatched'");
	}
	@Test
	public void groupingTest() {
		assertCorrectTokenization("(([(])) (')[][')", "(([(]))", "(')[][')");
		assertCorrectTokenization("['(]'[03)'", "'[03)'");
	}
	@Test
	public void statementTests() {
		assertCorrectTokenization("Define a number called _x.", "Define",
				"a", "number", "called", "_x", ".");
		assertCorrectTokenization(
				"If 4.0 + 4.0 = 2: Dothis. Otherwise: Dothat.", "If",
				"4.0", "+", "4.0", "=", "2", ":", "Dothis", ".",
				"Otherwise", ":", "Dothat", ".");
		assertCorrectTokenization(
				"Define a string called _x with a value of '()[]\\'\\r\\n'.",
				"Define", "a", "string", "called", "_x", "with", "a",
				"value", "of", "'()[]'\r\n'", ".");
		assertCorrectTokenization(
				"Define a [COMPILER ERROR HERE ''''''''''''] number called _x.",
				"Define", "a", "number", "called", "_x", ".");
		assertCorrectTokenization(
				"Set the value of _x to ((1%2)+(2+3)*(3//4)).", "Set",
				"the", "value", "of", "_x", "to", "((1%2)+(2+3)*(3//4))",
				".");
		assertCorrectTokenization(
				"_technically_this_is_a_valid_var''iable_name'",
				"_technically_this_is_a_valid_var''iable_name'");
	}
}
