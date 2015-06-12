package fortytwo.test.parser;

import org.junit.Test;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token42;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.test.Utilities;

public class VariableParserTest {
	@Test
	public void simpleTest() {
		Utilities.assertParse(VariableIdentifier.getInstance(new Token42(
				"\"abc\"", Context.SYNTHETIC)), "\"abc\"");
		Utilities.assertParse(VariableIdentifier.getInstance(new Token42(
				"\"abc123\"", Context.SYNTHETIC)), "\"abc123\"");
		Utilities.assertParse(VariableIdentifier.getInstance(new Token42(
				"\"`\"", Context.SYNTHETIC)), "\"`\"");
	}
	public static void assertVariableParse() {
		Utilities.assertParse(
				VariableIdentifier.getInstance(new Token42("\"" + name
						+ "\"", Context.SYNTHETIC)), "\"" + name + "\"");
	}
}
