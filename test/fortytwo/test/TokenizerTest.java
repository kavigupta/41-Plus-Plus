package fortytwo.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token;
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
		assertListEquals(Arrays.asList("'abc def ghi'"),
				Tokenizer.tokenize(Context.synthetic(), "'abc def ghi'"));
		assertListEquals(Arrays.asList("'( [)] 'unmatched'"),
				Tokenizer.tokenize(Context.synthetic(),
						"'( [)] \\'unmatched'"));
	}
	@Test
	public void groupingTest() {
		assertListEquals(Arrays.asList("(([(]))", "(')[][')"),
				Tokenizer.tokenize(Context.synthetic(), "(([(])) (')[][')"));
	}
	@Test
	public void statementTests() {
		assertListEquals(Arrays.asList("Define", "a", "number", "called",
				"_x", "."), Tokenizer.tokenize(Context.synthetic(),
				"Define a number called _x."));
		assertListEquals(Arrays.asList("If", "4.0", "+", "4.0", "=", "2",
				":", "Dothis", ".", "Otherwise", ":", "Dothat", "."),
				Tokenizer.tokenize(Context.synthetic(),
						"If 4.0 + 4.0 = 2: Dothis. Otherwise: Dothat."));
		assertListEquals(
				Arrays.asList("Define", "a", "string", "called", "_x",
						"with", "a", "value", "of", "'()[]'\r\n'", "."),
				Tokenizer.tokenize(Context.synthetic(),
						"Define a string called _x with a value of '()[]\\'\\r\\n'."));
		assertListEquals(
				Arrays.asList("Define", "a", "number", "called", "_x", "."),
				Tokenizer.tokenize(Context.synthetic(),
						"Define a [COMPILER ERROR HERE ''''''''''''] number called _x."));
		assertListEquals(Arrays.asList("Set", "the", "value", "of", "_x",
				"to", "((1%2)+(2+3)*(3//4))", "."), Tokenizer.tokenize(
				Context.synthetic(),
				"Set the value of _x to ((1%2)+(2+3)*(3//4))."));
		assertListEquals(
				Arrays.asList("_technically_this_is_a_valid_var''iable_name'"),
				Tokenizer.tokenize(Context.synthetic(),
						"_technically_this_is_a_valid_var''iable_name'"));
	}
	private void assertListEquals(List<String> expected, List<Token> actual) {
		assertEquals(
				expected,
				actual.stream().map(x -> x.token)
						.collect(Collectors.toList()));
	}
}
