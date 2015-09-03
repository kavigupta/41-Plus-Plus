package fortytwo.test.parser;

import static fortytwo.test.Utilities.assertCorrectTokenization;

import org.junit.Test;

/* TODO complete all test cases properly */
public class TokenizerTest {
	@Test
	public void statementTests() {
		assertCorrectTokenization("Define a number called _x.", "Define", "a",
				"number", "called", "_x", ".");
		assertCorrectTokenization(
				"If 4.0 + 4.0 = 2: Dothis. Otherwise: Dothat.", "If", "4.0",
				"+", "4.0", "=", "2", ":", "Dothis", ".", "Otherwise", ":",
				"Dothat", ".");
		assertCorrectTokenization(
				"Define a string called _x with a value of '()[]\\'\\n'.",
				"Define", "a", "string", "called", "_x", "with", "a", "value",
				"of", "'()[]'\n'", ".");
		assertCorrectTokenization(
				"Define a [COMPILER ERROR HERE ''''''''''''] number called _x.",
				"Define", "a", "number", "called", "_x", ".");
		assertCorrectTokenization(
				"Set the value of _x to ((1%2)+(2+3)*(3//4)).", "Set", "the",
				"value", "of", "_x", "to", "((1%2)+(2+3)*(3//4))", ".");
		assertCorrectTokenization(
				"_technically_this_is_a_valid_var''iable_name'",
				"_technically_this_is_a_valid_var''iable_name'");
	}
}
