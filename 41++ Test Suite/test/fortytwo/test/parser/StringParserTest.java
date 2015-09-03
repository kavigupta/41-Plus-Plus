package fortytwo.test.parser;

import static fortytwo.test.Utilities.assertCorrectTokenization;

import org.junit.Test;

/**
 * While short, this should be viewed as a complete test of the string parser
 * and therefore a specification.
 */
public class StringParserTest {
	@Test
	public void includeSpaces() {
		assertCorrectTokenization("'abc def ghi'", "'abc def ghi'");
		assertCorrectTokenization("'abc def   ghi'", "'abc def   ghi'");
		assertCorrectTokenization("'abc def   		ghi'",
				"'abc def   		ghi'");
		assertCorrectTokenization("'abc def   	\r\n\f	ghi'",
				"'abc def   	\r\n\f	ghi'");
	}
	@Test
	public void controlChars() {
		assertCorrectTokenization("'( [)] \\'unmatched'", "'( [)] 'unmatched'");
		assertCorrectTokenization("'Hello, World!'", "'Hello, World!'");
		assertCorrectTokenization(
				"'~!@#$%^&*)_+{}|:\"<>?`1234567890-=[\\\\;\\',./'",
				"'~!@#$%^&*)_+{}|:\"<>?`1234567890-=[\\;',./'");
		assertCorrectTokenization("'\\\\'", "'\\'");
		assertCorrectTokenization("'\\\\\\\\'", "'\\\\'");
		assertCorrectTokenization("'\\\\\\''", "'\\''");
	}
}
