package fortytwo.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import fortytwo.compiler.parser.Parser;

public class ParserTest {
	@Test
	public void tokenizerTest() {
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
		assertEquals(
				Arrays.asList("Set", "the", "value", "of", "_x", "to",
						"((1 % 2) + (2 + 3) * (3 // 4))", "."),
				Parser.tokenize42("Set the value of _x to ((1%2)+(2+3)*(3//4))."));
		assertEquals(
				Arrays.asList("_technically_this_is_a_valid_var''iable_name'"),
				Parser.tokenize42("_technically_this_is_a_valid_var''iable_name'"));
	}
	public void lineParserTest() {}
}
