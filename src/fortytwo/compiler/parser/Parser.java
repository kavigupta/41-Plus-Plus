package fortytwo.compiler.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lib.standard.collections.Pair;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.compiler.parsed.sentences.Sentence;
import fortytwo.compiler.parsed.statements.ParsedIfElse;
import fortytwo.compiler.parsed.statements.ParsedStatement;
import fortytwo.compiler.parsed.statements.ParsedStatementSeries;
import fortytwo.compiler.parsed.statements.ParsedWhileLoop;
import fortytwo.language.Language;
import fortytwo.language.Resources;

public class Parser {
	private Parser() {}
	public static List<Sentence> parse(String text) {
		List<String> tokens = tokenize42(text);
		List<List<String>> phrases = new ArrayList<>();
		List<String> current = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			String token = tokens.get(i);
			current.add(token);
			if (token.equals(Resources.PERIOD)
					|| token.equals(Resources.COLON)) {
				phrases.add(current);
				current = new ArrayList<>();
			}
		}
		return parse(phrases);
	}
	public static List<Sentence> parse(List<List<String>> phrases) {
		List<Sentence> sentences = new ArrayList<>();
		while (phrases.size() != 0) {
			sentences.add(pop(phrases));
		}
		return sentences;
	}
	public static Sentence pop(List<List<String>> phrases) {
		if (phrases.size() == 0)
			return new ParsedStatementSeries(Arrays.asList());
		switch (phrases.get(0).get(0)) {
			case Resources.IF:
				return popIf(phrases);
			case Resources.WHILE:
				return popWhile(phrases);
			case Resources.DO:
				return popSeries(phrases);
			default:
				return popSentence(phrases);
		}
	}
	public static Sentence popSentence(List<List<String>> phrases) {
		return StatementParser.parseStatement(phrases.remove(0));
	}
	public static Sentence popIf(List<List<String>> phrases) {
		List<String> IF = phrases.remove(0);
		IF.remove(0);
		ParsedExpression condition = ExpressionParser.parseExpression(IF);
		ParsedStatementSeries ifso = popSeries(phrases);
		ParsedStatementSeries ifelse = new ParsedStatementSeries(
				Arrays.asList());
		if (phrases.size() > 0
				&& phrases.get(0).get(0).equals(Resources.OTHERWISE)) {
			phrases.remove(0); // This should just be "Otherwise:"
			ifelse = popSeries(phrases);
		}
		return ParsedIfElse.getInstance(condition, ifso, ifelse);
	}
	private static Sentence popWhile(List<List<String>> phrases) {
		List<String> WHILE = phrases.remove(0);
		WHILE.remove(0);
		ParsedStatementSeries whileTrue = popSeries(phrases);
		return new ParsedWhileLoop(ExpressionParser.parseExpression(WHILE),
				whileTrue);
	}
	private static ParsedStatementSeries popSeries(List<List<String>> phrases) {
		if (phrases.size() == 0)
			return new ParsedStatementSeries(Arrays.asList());
		if (!Language.isOpeningBrace(phrases.get(0))) {
			Sentence sent = pop(phrases);
			if (!(sent instanceof ParsedStatement))
				throw new RuntimeException(/* LOWPRI-E */);
			return ParsedStatementSeries.getInstance((ParsedStatement) sent);
		}
		phrases.remove(0); // remove brace
		List<List<String>> inBraces = new ArrayList<>();
		int braces = 1;
		while (phrases.size() > 0) {
			if (Language.isOpeningBrace(phrases.get(0))) braces++;
			if (Language.isClosingBrace(phrases.get(0))) {
				phrases.remove(0);
				braces--;
				if (braces == 0) {
					List<Sentence> sentences = parse(inBraces);
					List<ParsedStatement> statements = sentences
							.stream()
							.map(x -> {
								if (x instanceof ParsedStatement)
									return (ParsedStatement) x;
								throw new RuntimeException(/* LOWPRI-E */);
							}).collect(Collectors.toList());
					return new ParsedStatementSeries(statements);
				}
			}
			inBraces.add(phrases.remove(0));
		}
		throw new RuntimeException(/* LOWPRI-E */);
	}
	public static List<String> tokenize42(String text) {
		text = text.replaceAll(Resources.PAD_FIND, Resources.PAD_REPLACE)
				.trim() + Resources.SPACE;
		return tokenizeRecursive(text).stream()
				.filter(x -> x.trim().length() != 0)
				.collect(Collectors.toList());
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
				return tokenizeRecursive(text.substring(endb + 1));
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
		int space = text.replaceAll("\\s", " ").indexOf(' ');
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
				return Pair.getInstance('(' + text.substring(0, i) + ')',
						i + 2);
		}
		throw new RuntimeException(/* LOWPRI-E */);
	}
	private static Pair<String, Integer> popStringLiteral(String text) {
		System.out.println("Popping: " + text);
		String unes = "";
		for (int i = 0; i < text.length(); i++) {
			switch (text.charAt(i)) {
				case '\\':
					Pair<Character, Integer> pair = popEscape(text
							.substring(i + 1));
					i += pair.value;
					unes += pair.key;
					break;
				case '\'':
					return Pair.getInstance('\'' + unes.replaceAll(
							Resources.UNPAD_FIND,
							Resources.UNPAD_REPLACE) + '\'', i + 2);
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