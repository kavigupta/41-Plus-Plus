package fortytwo.test.parser;

import static fortytwo.test.Utilities.assertCorrectTokenization;
import static fortytwo.test.Utilities.assertErrorInTokenization;

import org.junit.Test;

import fortytwo.vm.errors.ErrorType;

/**
 * A complete test of the grouping parser. This should be viewed as a
 * specification.
 */
@SuppressWarnings("static-method")
public class GroupingTest {
	@Test
	public void parenthesisTest() {
		assertCorrectTokenization("(([(])) (')[][')", "(([(]))", "(')[][')");
		assertCorrectTokenization(
				"(Whatever is put () [']']' between matching parenthesis should be in a single token)",
				"(Whatever is put () [']']' between matching parenthesis should be in a single token)");
	}
	@Test
	public void commentTest() {
		assertCorrectTokenization("[Comments][do][nothing]");
		assertCorrectTokenization(
				"[Comments can conta(in anything 'ps[dfs[[[[[[[[[ sdfgs }Dfghfg.\f but a close bracket (there is no escape)\\]");
	}
	@Test
	public void stringCommentTest() {
		assertCorrectTokenization(
				"['this should be a comment, not a quote...] '[and this should be a quote not a comment' ']' [']",
				"'[and this should be a quote not a comment'", "']'");
		assertCorrectTokenization("['(]'[03)'", "'[03)'");
	}
	@Test
	public void invalidBracketTest() {
		assertErrorInTokenization(ErrorType.SYNTAX,
				"This ~[~ has no corresponding ~]~", 0, 1, "[ asdf");
		assertErrorInTokenization(ErrorType.SYNTAX,
				"This ~[~ has no corresponding ~]~", 1, 2, " [ asdf [");
		assertErrorInTokenization(ErrorType.SYNTAX,
				"This ~]~ has no corresponding ~[~", 20, 21,
				" [this should work ]] <-- except for this!!!");
	}
	@Test
	public void invalidParenTest() {
		assertErrorInTokenization(ErrorType.SYNTAX,
				"This ~)~ has no corresponding ~(~", 0, 1, ")");
		assertErrorInTokenization(ErrorType.SYNTAX,
				"This ~)~ has no corresponding ~(~", 5, 6, "([(]))");
		assertErrorInTokenization(ErrorType.SYNTAX,
				"This ~(~ has no corresponding ~)~", 0, 1, "(([)])");
	}
	@Test
	public void invalidQuoteTest() {
		assertErrorInTokenization(ErrorType.SYNTAX,
				"This ~'~ has no corresponding ~'~", 0, 1, "'\\'\\\\");
		assertErrorInTokenization(ErrorType.SYNTAX,
				"This ~'~ has no corresponding ~'~", 0, 1, "'\\'\\\\");
		assertErrorInTokenization(ErrorType.SYNTAX,
				"This ~'~ has no corresponding ~'~", 2, 3, "\\ '\\\\");
	}
	@Test
	public void invalidNesting() {
		assertErrorInTokenization(ErrorType.SYNTAX,
				"This ~]~ has no corresponding ~[~", 3, 4, "'[']");
		assertErrorInTokenization(ErrorType.SYNTAX,
				"This ~)~ has no corresponding ~(~", 6, 7, "[(([)])");
	}
}
