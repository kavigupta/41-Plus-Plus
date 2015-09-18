package fortytwo.test.editor;

import static org.apache.commons.lang3.tuple.Pair.of;
import static org.fife.ui.rsyntaxtextarea.TokenTypes.*;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.text.Segment;

import org.apache.commons.lang3.tuple.Pair;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMaker;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.Before;
import org.junit.Test;

import fortytwo.ide.highlighting.Highlighter42;

public class SyntaxHighlightingTest {
	AbstractTokenMaker h;
	@Before
	public void init() {
		h = new Highlighter42();
	}
	@Test
	public void basicTest() {
		testMultiple(IDENTIFIER, "unassigned_identifier", "identifier2",
				"_underscore_start", "CAPITAL");
		testMultiple(VARIABLE, "\"x\"", "\"CAN CONTAIN SPACES\"",
				"\"can contain '\"", "\"can contain [\"", "\"can contain (\"",
				"\"can contain \\\"\"");
		testMultiple(LITERAL_STRING_DOUBLE_QUOTE, "'this is a string'",
				"'also a \\' string'");
		testMultiple(COMMENT_MARKUP, "[comment comment comment]",
				"[can contain'(\"]");
		testMultiple(RESERVED_WORD, "Define", "Set", "Exit", "Run", "While",
				"If", "Otherwise");
		testMultiple(RESERVED_WORD_2, "a", "an", "that", "takes", "outputs",
				"with");
		testMultiple(ERROR_CHAR, "[ should error out", "\" should error out",
				"\' should error out", "] should error out",
				") should error out", "( should error out");
	}
	@Test
	public void testExpress() {
		testHighlight("4//5", of("4", LITERAL_NUMBER_DECIMAL_INT),
				of("//", OPERATOR), of("5", LITERAL_NUMBER_DECIMAL_INT));
		testHighlight("(4//5)", of("(", SEPARATOR),
				of("4", LITERAL_NUMBER_DECIMAL_INT), of("//", OPERATOR),
				of("5", LITERAL_NUMBER_DECIMAL_INT), of(")", SEPARATOR));
		testHighlight("2*3+4//5-6%\"\"", of("2", LITERAL_NUMBER_DECIMAL_INT),
				of("*", OPERATOR), of("3", LITERAL_NUMBER_DECIMAL_INT),
				of("+", OPERATOR), of("4", LITERAL_NUMBER_DECIMAL_INT),
				of("//", OPERATOR), of("5", LITERAL_NUMBER_DECIMAL_INT),
				of("-", OPERATOR), of("6", LITERAL_NUMBER_DECIMAL_INT),
				of("%", OPERATOR), of("\"\"", VARIABLE));
		testHighlight("the \"length\" of \"arr\"", //
				of("the", RESERVED_WORD_2), of(" ", WHITESPACE),
				of("\"length\"", VARIABLE), of(" ", WHITESPACE),
				of("of", RESERVED_WORD_2), of(" ", WHITESPACE),
				of("\"arr\"", VARIABLE));
	}
	@Test
	public void testSentences() {
		testHighlight("Define a number called \"x\" with a value of 2.",
				of("Define", RESERVED_WORD), of(" ", WHITESPACE),
				of("a", RESERVED_WORD_2), of(" ", WHITESPACE),
				of("number", DATA_TYPE), of(" ", WHITESPACE),
				of("called", RESERVED_WORD_2), of(" ", WHITESPACE),
				of("\"x\"", VARIABLE), of(" ", WHITESPACE),
				of("with", RESERVED_WORD_2), of(" ", WHITESPACE),
				of("a", RESERVED_WORD_2), of(" ", WHITESPACE),
				of("value", VARIABLE), of(" ", WHITESPACE),
				of("of", RESERVED_WORD_2), of(" ", WHITESPACE),
				of("2", LITERAL_NUMBER_DECIMAL_INT), of(".", SEPARATOR));
		testHighlight("Define a bool called \"x\" with a value of true.",
				of("Define", RESERVED_WORD), of(" ", WHITESPACE),
				of("a", RESERVED_WORD_2), of(" ", WHITESPACE),
				of("bool", DATA_TYPE), of(" ", WHITESPACE),
				of("called", RESERVED_WORD_2), of(" ", WHITESPACE),
				of("\"x\"", VARIABLE), of(" ", WHITESPACE),
				of("with", RESERVED_WORD_2), of(" ", WHITESPACE),
				of("a", RESERVED_WORD_2), of(" ", WHITESPACE),
				of("value", VARIABLE), of(" ", WHITESPACE),
				of("of", RESERVED_WORD_2), of(" ", WHITESPACE),
				of("true", LITERAL_BOOLEAN), of(".", SEPARATOR));
		for (final String starter : new String[] { "If", "While" })
			testHighlight(starter + " \"x\" is less than 2:", //
					of(starter, RESERVED_WORD), of(" ", WHITESPACE),
					of("\"x\"", VARIABLE), of(" ", WHITESPACE),
					of("is", IDENTIFIER), of(" ", WHITESPACE),
					of("less", IDENTIFIER), of(" ", WHITESPACE),
					of("than", IDENTIFIER), of(" ", WHITESPACE),
					of("2", LITERAL_NUMBER_DECIMAL_INT), of(":", SEPARATOR));
	}
	@Test
	public void compoundTest() {
		testHighlight("2 + ())", //
				of("2", LITERAL_NUMBER_DECIMAL_INT), of(" ", WHITESPACE),
				of("+", OPERATOR), of(" ", WHITESPACE), of("(", SEPARATOR),
				of(")", SEPARATOR), of(")", ERROR_CHAR));
		testHighlight("(4 + 5 -)", //
				of("(", SEPARATOR), of("4", LITERAL_NUMBER_DECIMAL_INT),
				of(" ", WHITESPACE), of("+", OPERATOR), of(" ", WHITESPACE),
				of("5", LITERAL_NUMBER_DECIMAL_INT), of(" ", WHITESPACE),
				of("-", OPERATOR), of(")", SEPARATOR));
		testHighlight("(4 + 5 -))", //
				of("(", SEPARATOR), of("4", LITERAL_NUMBER_DECIMAL_INT),
				of(" ", WHITESPACE), of("+", OPERATOR), of(" ", WHITESPACE),
				of("5", LITERAL_NUMBER_DECIMAL_INT), of(" ", WHITESPACE),
				of("-", OPERATOR), of(")", SEPARATOR), of(")", ERROR_CHAR));
	}
	public void testMultiple(int type, String... identifiers) {
		for (final String ident : identifiers)
			testSTHighlight(ident, type);
	}
	public void testSTHighlight(String toTest, int expected) {
		testHighlight(toTest, of(toTest, expected));
	}
	@SafeVarargs
	public final void testHighlight(String toTest,
			Pair<String, Integer>... expected) {
		Token t = h.getTokenList(
				new Segment(toTest.toCharArray(), 0, toTest.length()),
				TokenTypes.IDENTIFIER, 0);
		final List<Pair<String, Integer>> actual = new ArrayList<>();
		for (; t.getType() != NULL; t = t.getNextToken()) {
			if (t.getEndOffset() - t.getOffset() == 0) continue;
			System.out.println(t.getOffset() + "\t" + t.getEndOffset());
			actual.add(of(new String(t.getTextArray(), t.getOffset(),
					t.getEndOffset() - t.getOffset()), t.getType()));
		}
		assertEquals(Arrays.asList(expected).toString(), actual.toString());
	}
}
