package fortytwo.language;

import static fortytwo.language.Resources.*;

import java.util.List;
import java.util.function.Function;

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
	public static String deparenthesize(String word) {
		if (word.startsWith(OPEN_PAREN)) {
			if (!word.endsWith(CLOSE_PAREN))
				throw new RuntimeException(/*
									 * LOWPRI-E
									 */);
			return word.substring(1, word.length() - 1);
		}
		return word;
	}
	public static boolean isExpression(String token) {
		if (token.equals(TRUE) || token.equals(FALSE)) return true;
		char start = token.charAt(0);
		return start == '(' || start == '+' || start == '-' || start == '*'
				|| start == '/' || Character.isDigit(start)
				|| start == '\'' || start == '_';
	}
	public static boolean isValidVariableIdentifier(String name) {
		if (name.contains(SPACE)) return false;
		return name.startsWith(VARIABLE_START);
	}
	public static boolean isFunctionToken(String token) {
		if (!Character.isAlphabetic(token.charAt(0))) return false;
		// LOWPRI rest of this: it should be fine for now
		return true;
	}
	public static String parenthesize(List<String> line) {
		if (line.size() == 0) return new String();
		if (line.size() == 1) return line.get(0);
		StringBuilder s = new StringBuilder(OPEN_PAREN);
		for (String l : line) {
			s.append(l).append(SPACE);
		}
		return s.append(CLOSE_PAREN).toString();
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
}
