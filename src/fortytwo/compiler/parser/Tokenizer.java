package fortytwo.compiler.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token;
import fortytwo.vm.errors.ParserErrors;

public class Tokenizer {
	public static List<Token> tokenize(Context parent, String input) {
		input += " ";
		List<Token> tokens = new ArrayList<>();
		StringBuffer token = new StringBuffer();
		loop: for (int i = 0; i < input.length(); i++) {
			switch (input.charAt(i)) {
				case '[':
					add(parent, i, token, tokens);
					int closebracket = findCloseBracket(input, i);
					if (closebracket < 0)
						ParserErrors.closingBracketNotFound(parent,
								input, i);
					// ignore everything between brackets.
					i = closebracket;
					continue loop;
				case '(':
					add(parent, i, token, tokens);
					int closeparen = findCloseParen(input, i);
					if (closeparen < 0)
						ParserErrors.closingParenNotFound(parent, input,
								i);
					// dump anything between parenthesis into an single
					// token
					tokens.add(new Token(input
							.substring(i, closeparen + 1), Context
							.construct(parent, i, closeparen + 1)));
					i = closeparen;
					continue loop;
				case ')':
				case ']':
					ParserErrors.closeMarkerWithNoOpenMarker(parent,
							input, i);
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
					break;
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
					if (i - 1 < 0
							|| Character.isWhitespace(input
									.charAt(i - 1))) {
						add(parent, i, token, tokens);
						int closequote = findCloseQuote(input, i);
						if (closequote < 0)
							ParserErrors.closingQuoteNotFound(parent,
									input, i);
						tokens.add(new Token("'"
								+ unescape(input.substring(i + 1,
										closequote)) + "'" + "",
								Context.construct(parent, i,
										closequote + 1)));
						i = closequote;
						continue loop;
					}
					break;
			}
			if (Character.isWhitespace(input.charAt(i))) {
				add(parent, i, token, tokens);
				continue loop;
			}
			token.append(input.charAt(i));
			continue loop;
		}
		return tokens.stream().filter(t -> t.token.trim().length() != 0)
				.collect(Collectors.toList());
	}
	private static int findCloseQuote(String input, int i) {
		for (int j = i + 1; j < input.length(); j++) {
			if (input.charAt(j) != '\'') continue;
			if (input.charAt(j - 1) != '\\') return j;
		}
		return -1;
	}
	private static int findCloseParen(String input, int i) {
		for (int j = i + 1; j < input.length(); j++) {
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
	public static String unescape(String str) {
		str = str.replace("\\b", "\b");
		str = str.replace("\\t", "\t");
		str = str.replace("\\n", "\n");
		str = str.replace("\\r", "\r");
		str = str.replace("\\f", "\f");
		str = str.replace("\\'", "\'");
		str = str.replace("\\\\", "\\");
		Matcher mat = Pattern
				.compile("\\\\u(?<hexcode>[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f])")
				.matcher(str);
		int i = 0;
		StringBuffer sbuff = new StringBuffer();
		while (mat.find()) {
			int start = mat.start();
			int end = mat.end();
			int c = Integer.parseInt(mat.group("hexcode").toUpperCase(), 16);
			sbuff.append(str.substring(i, start)).append(
					Character.toString((char) c));
			i = end;
		}
		sbuff.append(str.substring(i));
		return sbuff.toString();
	}
}
