package fortytwo.language;

import static fortytwo.language.Resources.*;

import java.util.List;

import fortytwo.compiler.LiteralToken;

/**
 * Utility class containing methods regarding the 41++ Language.
 */
public class Language {
	/**
	 * @param word
	 *        a phrase, as a String.
	 * @return {@code an <word>} if the first letter in {@code word}} is a
	 *         vowel.
	 *         {@code a <word>} if the first letter in {@code word} is a
	 *         consonant.
	 */
	public static String articleized(String word) {
		if (startsWithVowel(word)) return AN + STD_SEP + word;
		return A + STD_SEP + word;
	}
	/**
	 * Returns {@code true} if the first letter in the given word is a vowel.
	 * 
	 * Note that this will return {@code true} for things like
	 * {@code (array of number)}.
	 */
	private static boolean startsWithVowel(String word) {
		for (int i = 0; i < word.length(); i++)
			if (isVowel(word.charAt(i)))
				return true;
			else if (Character.isAlphabetic(word.charAt(i))) return false;
		return false;
	}
	/**
	 * Returns {@code true} if {@code c} is in the set {@code "AEIOUaeiou"}.
	 */
	public static boolean isVowel(char c) {
		c = Character.toLowerCase(c);
		return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
	}
	/**
	 * Returns {@code true} if the given word is an indefinete article.
	 */
	public static boolean isArticle(String word) {
		return word.equals(A) || word.equals(AN);
	}
	/**
	 * Returns true if {@code token} is equal to either a comma or the word
	 * {@code and}.
	 */
	public static boolean isListElement(String token) {
		return token.equals(COMMA) || token.equals(AND);
	}
	/**
	 * Returns the matching symbol to a given character that is relevant for the
	 * 41++ language.
	 */
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
			case '\"':
				return '\"';
		}
		return 0;
	}
	/**
	 * Returns the ordinal expression for this number, based on it's last digit.
	 * e.g., {@code 1} should become {@code "1st"}.
	 */
	public static String ordinal(int argument) {
		String suffix;
		switch (Math.abs(argument) % 10) {
			case 1:
				suffix = "st";
				break;
			case 2:
				suffix = "nd";
				break;
			case 3:
				suffix = "rd";
				break;
			default:
				suffix = "th";
		}
		return argument + suffix;
	}
	/**
	 * Makes the first character in the given sentence uppercase.
	 */
	public static String uppercase(String sent) {
		if (sent.length() == 0) return "";
		return Character.toUpperCase(sent.charAt(0)) + sent.substring(1);
	}
	/**
	 * Returns true if the given string is an operator defined by 41++.
	 */
	public static boolean isOperator(String c) {
		if (ADDITION_SIGN.equals(c)) return true;
		if (SUBTRACTION_SIGN.equals(c)) return true;
		if (MULTIPLICATION_SIGN.equals(c)) return true;
		if (DIV_SIGN.equals(c)) return true;
		if (FLOORDIV_SIGN.equals(c)) return true;
		if (MOD_SIGN.equals(c)) return true;
		return false;
	}
	/**
	 * Returns {@code true} if it leads to the automatic end of a token.
	 */
	public static boolean isTerminator(char c) {
		return isOperator(Character.toString(c)) || Character.isWhitespace(c)
				|| isPunctuation(c) || c == ']' || c == ')';
	}
	/**
	 * Returns true if it's input is punctuation.
	 */
	private static boolean isPunctuation(char c) {
		return c == ',' || c == '.';
	}
	/**
	 * TODO move to an encapsulated function of the new LiteralTokenList
	 * function
	 */
	public static int indexOf(List<LiteralToken> tokens, String s) {
		for (int i = 0; i < tokens.size(); i++)
			if (tokens.get(i).token.equals(s)) return i;
		return -1;
	}
	/**
	 * Returns whether or not the given character is a quote.
	 */
	public static boolean isQuote(char c) {
		return c == '\'' || c == '"';
	}
}
