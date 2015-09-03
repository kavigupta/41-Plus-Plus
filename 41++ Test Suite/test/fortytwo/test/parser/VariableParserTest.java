package fortytwo.test.parser;

import org.junit.Test;

import fortytwo.compiler.LiteralToken;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.test.Utilities;

public class VariableParserTest {
	@Test
	public void simpleTest() {
		assertVariableParse("abc");
		assertVariableParse("abc123");
		assertVariableParse("123");
		assertVariableParse("``");
		assertVariableParse("_-2=");
		assertVariableParse("(");
		assertVariableParse(" ");
	}
	public static void assertVariableParse(String name) {
		Utilities.assertParse(
				VariableIdentifier.getInstance(
						LiteralToken.synthetic("\"" + name + "\"")),
				"\"" + name + "\"");
	}
}
