package fortytwo.compiler.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fortytwo.compiler.LiteralToken;
import fortytwo.language.Language;
import fortytwo.vm.errors.SyntaxErrors;

public class Tokenizer {
		public static List<LiteralToken> tokenize(LiteralToken depar) {
			return tokenizeFully(depar).stream()
					.filter(LiteralToken::isMeaningful)
					.collect(Collectors.toList());
		}
		/**
		 * @param parent
		 * @param input
		 * @return
		 */
		/**
		 * @param parent
		 * @param input
		 * @return
		 */
		public static List<LiteralToken> tokenizeFully(
				LiteralToken parentToken) {
			parentToken = parentToken.padWithSpace();
			if (parentToken.token.length() == 0) return new ArrayList<>();
			// This function, unlike others, contains return statements after
			// error throw statements. This is because for syntax highlighting
			// purposes, it will be called in an error-tolerant environment.
			List<LiteralToken> tokens = new ArrayList<>();
			int toklen = 0;
			loop: for (int i = 0; i < parentToken.token.length(); i++) {
				switch (parentToken.token.charAt(i)) {
					case '[':
							add(parentToken, i, toklen, tokens);
							toklen = 0;
							int closebracket = findCloseBracket(
									parentToken.token, i);
							if (closebracket < 0) {
								SyntaxErrors.matchingSymbolDNE(
										parentToken.context, parentToken.token,
										i);
								tokens.add(LiteralToken.errorToken(
										parentToken.token.substring(i)));
								return tokens;
							}
							// don't just ignore everything between brackets,
							// add
							// it! (It'll be filtered out for everything but
							// syntax
							// highlighting purposes anyway)
							tokens.add(
									parentToken.subToken(i, closebracket + 1));
							i = closebracket;
							continue loop;
					case '(':
							add(parentToken, i, toklen, tokens);
							toklen = 0;
							int closeparen = findCloseParen(parentToken.token,
									i);
							if (closeparen < 0) {
								SyntaxErrors.matchingSymbolDNE(
										parentToken.context, parentToken.token,
										i);
								tokens.add(LiteralToken.errorToken(
										parentToken.token.substring(i)));
								return tokens;
							}
							// dump anything between parenthesis into an single
							// token
							tokens.add(parentToken.subToken(i, closeparen + 1));
							i = closeparen;
							continue loop;
					case ')':
					case ']':
							tokens.add(LiteralToken.errorToken(
									parentToken.token.substring(i)));
							SyntaxErrors.matchingSymbolDNE(parentToken.context,
									parentToken.token, i);
							return tokens;
					case '+':
					case '-':
					case '*':
					case '%':
					case ':':
					case ',':
							add(parentToken, i, toklen, tokens);
							toklen = 0;
							tokens.add(parentToken.subToken(i, i + 1));
							continue loop;
					case '.':
							if (i + 1 < parentToken.token.length() && !Character
									.isDigit(parentToken.token.charAt(i + 1))) {
								add(parentToken, i, toklen, tokens);
								toklen = 0;
								tokens.add(parentToken.subToken(i, i + 1));
								continue loop;
							}
							break;
					case '/':
							add(parentToken, i, toklen, tokens);
							toklen = 0;
							int len;
							if (i + 1 < parentToken.token.length()
									&& parentToken.token.charAt(i + 1) == '/')
								len = 2;
							else len = 1;
							tokens.add(parentToken.subToken(i, i + len));
							continue loop;
					case '\'':
					case '\"':
							if (i - 1 < 0 || Language.isTerminator(
									parentToken.token.charAt(i - 1))) {
								add(parentToken, i, toklen, tokens);
								toklen = 0;
								int closequote = findCloseQuote(
										parentToken.token, i);
								if (closequote < 0) {
									SyntaxErrors.matchingSymbolDNE(
												parentToken.context,
												parentToken.token, i);
									System.out.println(
												"WORKING???" + parentToken.token
															.substring(i));
									tokens.add(LiteralToken
												.synthetic(parentToken.token
															.substring(i)));
									System.out.println(tokens);
									return tokens;
								}
								tokens.add(
										parentToken.subToken(i, closequote + 1)
													.unescape());
								i = closequote;
								continue loop;
							}
							break;
				}
				if (Character.isWhitespace(parentToken.token.charAt(i))) {
					add(parentToken, i, toklen, tokens);
					toklen = 0;
					tokens.add(parentToken.subToken(i, i + 1));
					continue loop;
				}
				if (i - 1 >= 0
							&& Character.isDigit(
										parentToken.token.charAt(i - 1))
							&& (!Character.isDigit(parentToken.token.charAt(i)))
							&& parentToken.token.charAt(i) != '.') {
					add(parentToken, i, toklen, tokens);
					toklen = 0;
				}
				toklen++;
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
				if (Language.isQuote(input.charAt(j))
							&& input.charAt(j - 1) != '\\') {
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
		private static void add(LiteralToken parent, int i, int toklen,
				List<LiteralToken> tokens) {
			tokens.add(parent.subToken(i - toklen, i));
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
								if (sub.matches(
										"[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f]")) {
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
