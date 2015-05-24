package fortytwo.compiler.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token;

public class Tokenizer {
	public static List<Token> tokenize(Context parent, String input) {
		input += " ";
		List<Token> tokens = new ArrayList<>();
		StringBuffer token = new StringBuffer();
		loop: for (int i = 0; i < input.length(); i++) {
			switch (input.charAt(i)) {
				case '[':
					add(parent, i, token, tokens);
					i = findCloseBracket(input, i);
					if (i < 0) throw new RuntimeException(/* LOWPRI-E */);
					// ignore everything between brackets.
					continue loop;
				case '(':
					add(parent, i, token, tokens);
					int closeparen = findCloseParen(input, i);
					if (closeparen < 0) throw new RuntimeException(/*
														 * LOWPRI-E
														 */);
					// dump anything between parenthesis into an single
					// token
					tokens.add(new Token(input
							.substring(i + 1, closeparen), Context
							.construct(parent, i, closeparen + 1)));
					i = closeparen;
					continue loop;
				case ')':
				case ']':
					throw new RuntimeException(/* LOWPRI-E */);
				case '+':
				case '-':
				case '*':
				case '%':
				case ':':
				case ',':
					add(parent, i, token, tokens);
					tokens.add(new Token(Character.toString(input
							.charAt(i)), Context.construct(parent, i,
							i + 1)));
					continue loop;
				case '.':
					if (i + 1 < input.length()
							&& !Character.isDigit(input.charAt(i + 1))) {
						add(parent, i, token, tokens);
						tokens.add(new Token(Character.toString(input
								.charAt(i)), Context.construct(parent,
								i, i + 1)));
						continue loop;
					}
				case '/':
					add(parent, i, token, tokens);
					String divOrFloorDiv;
					if (i + 1 < input.length()
							&& input.charAt(i + 1) == '/')
						divOrFloorDiv = "//";
					else divOrFloorDiv = "/";
					tokens.add(new Token(divOrFloorDiv, Context.construct(
							parent, i, i + 1)));
					continue loop;
				case '\'':
					add(parent, i, token, tokens);
					int closequote = findCloseQuote(input, i);
					tokens.add(new Token(unescape(input.substring(i + 1,
							closequote)), Context.construct(parent, i,
							closequote + 1)));
					i = closequote;
					continue loop;
			}
			if (Character.isWhitespace(input.charAt(i))) {
				add(parent, i, token, tokens);
				continue loop;
			}
			token.append(input.charAt(i));
			continue loop;
		}
		return tokens;
	}
	private static int findCloseQuote(String input, int i) {
		for (int j = i + 1; j < input.length(); i++) {
			if (input.charAt(j) != '\'') continue;
			if (input.charAt(j - 1) != '\\') return j;
		}
		return -1;
	}
	private static int findCloseParen(String input, int i) {
		for (int j = i + 1; j < input.length(); i++) {
			if (input.charAt(j) == ')') return j;
			if (input.charAt(j) == '\'' && input.charAt(j - 1) != '\\') {
				j = findCloseQuote(input, j);
				continue;
			}
			if (input.charAt(j) == '(') {
				j = findCloseParen(input, j);
				continue;
			}
			if (input.charAt(j) == '[') {
				j = findCloseBracket(input, j);
				continue;
			}
		}
		return -1;
	}
	private static void add(Context parent, int i, StringBuffer token,
			List<Token> tokens) {
		tokens.add(new Token(token.toString(), Context.construct(parent, i, i
				+ token.length())));
		token.setLength(0);
	}
	private static int findCloseBracket(String input, int i) {
		return input.indexOf(']', i);
	}
	private static String unescape(String str) {
		str = str.replaceAll("\\\\b", "\b");
		str = str.replaceAll("\\\\t", "\t");
		str = str.replaceAll("\\\\n", "\n");
		str = str.replaceAll("\\\\r", "\r");
		str = str.replaceAll("\\\\f", "\f");
		str = str.replaceAll("\\\\'", "\'");
		str = str.replaceAll("\\\\\\\\", "\\");
		Matcher mat = Pattern.compile(
				"\\\\u(?<hexcode>[0-9A-Fa-f][0-9A-Fa-f])").matcher(str);
		int i = 0;
		StringBuffer sbuff = new StringBuffer();
		while (mat.find()) {
			int start = mat.start();
			int end = mat.end();
			sbuff.append(str.substring(i, start)).append(
					Integer.parseInt(mat.group("hexcode").toUpperCase(),
							16));
			i = end;
		}
		return sbuff.toString();
	}
}
