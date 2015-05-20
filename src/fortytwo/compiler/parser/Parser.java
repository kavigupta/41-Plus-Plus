package fortytwo.compiler.parser;

import java.util.ArrayList;
import java.util.List;

import lib.standard.collections.Pair;
import fortytwo.compiler.parsed.statements.ParsedStatement;

public class Parser {
	private Parser() {}
	public static List<ParsedStatement> parse(String text) {
		List<String> tokens = tokenize42(text);
		List<List<String>> phrases = new ArrayList<>();
		List<String> current = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			String token = tokens.get(i);
			current.add(token);
			if (token.equals(".") || token.equals(";") || token.equals(":")) {
				phrases.add(current);
				current = new ArrayList<>();
			}
		}
		List<ParsedStatement> statements = new ArrayList<>();
		List<List<String>> currentPhrases = new ArrayList<>();
		for (int i = 0; i < phrases.size(); i++) {
			currentPhrases.add(phrases.get(i));
			if (phrases.get(i).get(phrases.get(i).size() - 1).equals(".")) {
				statements.add(StatementParser
						.parseCompleteStatement(currentPhrases));
				currentPhrases.clear();
			}
		}
		return statements;
	}
	public static List<String> tokenize42(String text) {
		text = text.replaceAll("(?<op>(//)|\\+|-|\\*|/)", " ${op} ")
				.replaceAll("\\s+", " ").trim()
				+ " ";
		return tokenizeRecursive(text);
	}
	private static List<String> tokenizeRecursive(String text) {
		if (text.length() == 0) return new ArrayList<>();
		switch (text.charAt(0)) {
			case '\'':
				Pair<String, Integer> poppedString = popStringLiteral(text
						.substring(1));
				return merge(poppedString.key,
						tokenizeRecursive(text
								.substring(poppedString.value)));
			case '[':
				int endb = text.indexOf(']');
				if (endb < 0) throw new RuntimeException(/* LOWPRI-E */);
				return tokenizeRecursive(text.substring(endb));
			case '(':
				Pair<String, Integer> poppedParenthetical = popParenthetical(text
						.substring(1));
				return merge(poppedParenthetical.key,
						tokenizeRecursive(text
								.substring(poppedParenthetical.value)));
			case ']':
			case ')':
				throw new RuntimeException(/* LOWPRI-E */);
			case ':':
			case '.':
			case ';':
			case ',':
				return merge(Character.toString(text.charAt(0)),
						tokenizeRecursive(text.substring(1)));
			default:
				Pair<String, Integer> token = popToken(text);
				return merge(token.key,
						tokenizeRecursive(text.substring(token.value)));
		}
	}
	private static Pair<String, Integer> popToken(String text) {
		int space = text.indexOf(' ');
		if (space == -1) return Pair.getInstance(text, text.length());
		return Pair.getInstance(text.substring(0, space), space + 1);
	}
	private static List<String> merge(String key,
			List<String> tokenizeRecursive) {
		ArrayList<String> list = new ArrayList<>();
		list.add(key);
		list.addAll(tokenizeRecursive);
		return list;
	}
	private static Pair<String, Integer> popParenthetical(String text) {
		int paren = 1;
		for (int i = 0; i < text.length(); i++) {
			switch (text.charAt(i)) {
				case '(':
					paren++;
					break;
				case ')':
					paren--;
					break;
			}
			if (paren == 0)
				return Pair.getInstance(text.substring(0, i), i + 1);
		}
		throw new RuntimeException(/* LOWPRI-E */);
	}
	private static Pair<String, Integer> popStringLiteral(String text) {
		String unes = "";
		for (int i = 0; i < text.length(); i++) {
			switch (text.charAt(i)) {
				case '\\':
					Pair<Character, Integer> pair = popEscape(text
							.substring(i + 1));
					i += 1 + pair.value;
					unes += pair.key;
					break;
				case '\'':
					return Pair.getInstance(unes, i + 1);
				default:
					unes += text.charAt(i);
			}
		}
		throw new RuntimeException(/* LOWPRI-E */);
	}
	private static Pair<Character, Integer> popEscape(String text) {
		if (text.length() == 0) throw new RuntimeException(/* LOWPRI-E */);
		switch (text.charAt(0)) {
			case 'n':
				return Pair.getInstance('\n', 1);
			case 'r':
				return Pair.getInstance('\r', 1);
			case 'b':
				return Pair.getInstance('\b', 1);
			case 't':
				return Pair.getInstance('\t', 1);
			case 'f':
				return Pair.getInstance('\f', 1);
			case '\'':
				return Pair.getInstance('\'', 1);
			case '\\':
				return Pair.getInstance('\\', 1);
			case 'u':
				try {
					return Pair.getInstance((char) Integer.parseInt(
							text.substring(1, 5), 16), 5);
				} catch (NumberFormatException
						| ArrayIndexOutOfBoundsException e) {
					throw new RuntimeException(/* LOWPRI-E */);
				}
			default:
				throw new RuntimeException(/* LOWPRI-E */);
		}
	}
}