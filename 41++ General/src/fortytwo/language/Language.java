package fortytwo.language;

import static fortytwo.language.Resources.*;

import java.util.List;
import java.util.function.Function;

import fortytwo.compiler.LiteralToken;
import fortytwo.vm.errors.SyntaxErrors;

public class Language {
	public static String articleized(String word) {
		if (startsWithVowel(word))
			return AN + SPACE + word;
		else return A + SPACE + word;
	}
	private static boolean startsWithVowel(String word) {
		for (int i = 0; i < word.length(); i++) {
			if (isVowel(word.charAt(i)))
				return true;
			else if (Character.isAlphabetic(word.charAt(i))) return false;
		}
		return false;
	}
	public static boolean isVowel(char c) {
		c = Character.toLowerCase(c);
		return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
	}
	public static <T> String sayList(List<T> fields,
			Function<T, String> howToSay) {
		if (fields.size() == 0) return new String();
		if (fields.size() == 1) return howToSay.apply(fields.get(0));
		StringBuffer sbuff = new StringBuffer();
		for (int i = 0; i < fields.size() - 1; i++)
			sbuff.append(howToSay.apply(fields.get(i)))
					.append(COMMA + SPACE);
		return sbuff.append(AND + SPACE)
				.append(howToSay.apply(fields.get(fields.size() - 1)))
				.toString();
	}
	public static boolean isArticle(String word) {
		return word.equals(A) || word.equals(AN);
	}
	public static LiteralToken deparenthesize(LiteralToken token) {
		if (token.token.startsWith(OPEN_PAREN)) {
			if (!token.token.endsWith(CLOSE_PAREN)) {
				SyntaxErrors.matchingSymbolDNE(token.context, token.token,
						0);
				return token.subToken(1, token.token.length());
			}
			return token.subToken(1, token.token.length() - 1);
		}
		return token;
	}
	public static boolean isExpression(String token) {
		if (token.equals(TRUE) || token.equals(FALSE)) return true;
		char start = token.charAt(0);
		return start == '(' || start == '+' || start == '-' || start == '*'
				|| start == '/' || start == '%' || Character.isDigit(start)
				|| start == '\'' || start == '\"';
	}
	public static boolean isValidVariableIdentifier(String token) {
		if (token == null) return false;
		if (token.equals(VALUE)) return true;
		return token.startsWith(VARIABLE_BEGIN)
				&& token.endsWith(VARIABLE_END);
	}
	public static boolean isValidVariableIdentifier(LiteralToken literalToken) {
		return isValidVariableIdentifier(literalToken.token);
	}
	public static boolean isFunctionToken(String token) {
		if (isExpression(token)) return false;
		// just assume anything that isn't an expression is a function
		// LOWPRI fix this function
		return true;
	}
	public static boolean isListElement(String token) {
		return token.equals(COMMA) || token.equals(AND);
	}
	public static boolean isOpeningBrace(List<String> list) {
		return list.get(0).equals(DO) && list.get(1).equals(THE)
				&& list.get(2).equals(FOLLOWING);
	}
	public static boolean isClosingBrace(List<String> list) {
		return list.get(0).equals(THATS) && list.get(1).equals(ALL);
	}
	public static char matchingSymbol(char start) {
		switch (start) {
			case '[':
				return ']';
			case ']':
				return '[';
			case '(':
				return ')';
			case ')':
				return '(';
			case '\'':
				return '\'';
		}
		return 0;
	}
	public static String ordinal(int argument) {
		switch (argument % 10) {
			case 1:
				return argument + "st";
			case 2:
				return argument + "nd";
			case 3:
				return argument + "rd";
		}
		return argument + "th";
	}
	public static String uppercase(String sent) {
		if (sent.length() == 0) return "";
		return Character.toUpperCase(sent.charAt(0)) + sent.substring(1);
	}
	public static boolean isOperator(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/' || c == '%';
	}
	public static boolean isTerminator(char c) {
		return isOperator(c) || Character.isWhitespace(c) || isPunctuation(c)
				|| c == ']' || c == ')';
	}
	private static boolean isPunctuation(char c) {
		return c == ',' || c == '.';
	}
	public static int indexOf(List<LiteralToken> tokens, String s) {
		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i).token.equals(s)) return i;
		}
		return -1;
	}
}