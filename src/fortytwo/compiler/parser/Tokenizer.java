package fortytwo.compiler.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token42;
import fortytwo.language.Language;
import fortytwo.vm.errors.SyntaxErrors;

public class Tokenizer {
	public static List<Token42> tokenize(Context parent, String input) {
		return tokenizeFully(parent, input).stream()
				.filter(Token42::isMeaningful).collect(Collectors.toList());
	}
	public static List<Token42> tokenizeFully(Context parent, String input) {
		if (input.length() == 0) return new ArrayList<>();
		// This function, unlike others, contains return statements after
		// error throw statements. This is because for syntax highlighting
		// purposes, it will be called in an error-tolerant environment.
		input += " ";
		List<Token42> tokens = new ArrayList<>();
		StringBuffer token = new StringBuffer();
		loop: for (int i = 0; i < input.length(); i++) {
			switch (input.charAt(i)) {
				case '[':
					add(parent, i, token, tokens);
					int closebracket = findCloseBracket(input, i);
					if (closebracket < 0) {
						SyntaxErrors.matchingSymbolDNE(parent, input, i);
						tokens.add(new Token42(input.substring(i), null));
						return tokens;
					}
					// don't just ignore everything between brackets, add
					// it! (It'll be filtered out for everything but syntax
					// highlighting purposes anyway)
					tokens.add(new Token42(input.substring(i,
							closebracket + 1), parent.subContext(i,
							closebracket + 1)));
					i = closebracket;
					continue loop;
				case '(':
					add(parent, i, token, tokens);
					int closeparen = findCloseParen(input, i);
					if (closeparen < 0) {
						SyntaxErrors.matchingSymbolDNE(parent, input, i);
						tokens.add(new Token42(input.substring(i), null));
						return tokens;
					}
					// dump anything between parenthesis into an single
					// token
					tokens.add(new Token42(input.substring(i,
							closeparen + 1), parent.subContext(i,
							closeparen + 1)));
					i = closeparen;
					continue loop;
				case ')':
				case ']':
					tokens.add(new Token42(input.substring(i), null));
					SyntaxErrors.matchingSymbolDNE(parent, input, i);
					return tokens;
				case '+':
				case '-':
				case '*':
				case '%':
				case ':':
				case ',':
					add(parent, i, token, tokens);
					tokens.add(new Token42(Character.toString(input
							.charAt(i)), parent.subContext(i, i + 1)));
					continue loop;
				case '.':
					if (i + 1 < input.length()
							&& !Character.isDigit(input.charAt(i + 1))) {
						add(parent, i, token, tokens);
						tokens.add(new Token42(Character.toString(input
								.charAt(i)), parent
								.subContext(i, i + 1)));
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
					tokens.add(new Token42(divOrFloorDiv, parent
							.subContext(i, i + 1)));
					continue loop;
				case '\'':
				case '\"':
					if (i - 1 < 0
							|| Language
									.isTerminator(input.charAt(i - 1))) {
						add(parent, i, token, tokens);
						int closequote = findCloseQuote(input, i);
						if (closequote < 0) {
							SyntaxErrors.matchingSymbolDNE(parent,
									input, i);
							tokens.add(new Token42(input.substring(i),
									null));
							return tokens;
						}
						tokens.add(new Token42(input.charAt(i)
								+ unescape(input.substring(i + 1,
										closequote))
								+ input.charAt(i), parent.subContext(i,
								closequote + 1)));
						i = closequote;
						continue loop;
					}
					break;
			}
			if (Character.isWhitespace(input.charAt(i))) {
				add(parent, i, token, tokens);
				tokens.add(new Token42(input.substring(i, i + 1), parent
						.subContext(i, i + 1)));
				continue loop;
			}
			if (i - 1 >= 0 && Character.isDigit(input.charAt(i - 1))
					&& (!Character.isDigit(input.charAt(i)))
					&& input.charAt(i) != '.') {
				add(parent, i, token, tokens);
			}
			token.append(input.charAt(i));
			continue loop;
		}
		if (tokens.size() == 0) return tokens;
		if (tokens.get(tokens.size() - 1).token.equals(" "))
			tokens.remove(tokens.size() - 1);
		return tokens;
	}
	private static int findCloseQuote(String input, int i) {
		char open = input.charAt(i);
		int backslashstate = 0;
		for (int j = i + 1; j < input.length(); j++) {
			if (input.charAt(j) == open && backslashstate % 2 == 0)
				return j;
			if (input.charAt(j) == '\\')
				backslashstate++;
			else backslashstate = 0;
		}
		return -1;
	}
	private static int findCloseParen(String input, int i) {
		for (int j = i + 1; j < input.length(); j++) {
			if (input.charAt(j) == ')') return j;
			if (input.charAt(j) == '\'' && input.charAt(j - 1) != '\\') {
				j = findCloseQuote(input, j);
				if (j == -1) return -1;
				continue;
			}
			if (input.charAt(j) == '(') {
				j = findCloseParen(input, j);
				if (j == -1) return -1;
				continue;
			}
			if (input.charAt(j) == '[') {
				j = findCloseBracket(input, j);
				if (j == -1) return -1;
				continue;
			}
		}
		return -1;
	}
	private static void add(Context parent, int i, StringBuffer token,
			List<Token42> tokens) {
		tokens.add(new Token42(token.toString(), parent.subContext(
				i - token.length(), i)));
		token.setLength(0);
	}
	private static int findCloseBracket(String input, int i) {
		return input.indexOf(']', i);
	}
	public static String unescape(String str) {
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '\\') {
				if (i + 1 >= str.length()) {
					buff.append('\\');
					continue;
				}
				i++;
				switch (str.charAt(i)) {
					case '\\':
						buff.append('\\');
						continue;
					case '\'':
						buff.append('\'');
						continue;
					case 'n':
						buff.append('\n');
						continue;
					case 't':
						buff.append('\t');
						continue;
					case 'u':
						if (i + 5 >= str.length()) {
							buff.append("\\u");
							continue;
						}
						String sub = str.substring(i + 1, i + 5);
						if (sub.matches("[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f]")) {
							buff.append((char) Integer.parseInt(
									sub.toUpperCase(), 16));
							i += 4;
						} else i--;
						break;
					default:
						buff.append('\\');
						i--;
						break;
				}
			} else {
				buff.append(str.charAt(i));
			}
		}
		return buff.toString();
	}
	public static String escape(String str) {
		str = str.replace("\\", "\\\\");
		str = str.replace("\b", "\\b");
		str = str.replace("\t", "\\t");
		str = str.replace("\n", "\\n");
		str = str.replace("\r", "\\r");
		str = str.replace("\f", "\\f");
		str = str.replace("\'", "\\'");
		return str;
	}
}
