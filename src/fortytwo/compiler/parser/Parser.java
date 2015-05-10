package fortytwo.compiler.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fortytwo.compiler.errors.SyntaxMatchingError;
import fortytwo.compiler.struct.Statement42;

public class Parser {
	private Parser() {}
	public static String collapseWhitespace(String line) {
		return line.replaceAll("\\s+", " ");
	}
	public static Statement42 intepretLine(String line)
			throws SyntaxMatchingError {
		List<String> tokens = tokenize42(line);
		switch (tokens.get(0)) {
			case "Define":
				return parseDefinition(tokens);
			case "Set":
				return parseAssignment(tokens);
			default:
				throw new SyntaxMatchingError(line);
		}
		// TODO more types of statements
	}
	private static Statement42 parseDefinition(List<String> tokens) {
		// TODO Auto-generated method stub
		return null;
	}
	private static Statement42 parseAssignment(List<String> tokens) {
		// TODO Auto-generated method stub
		return null;
	}
	public static List<String> tokenize42(String text) {
		text = collapseWhitespace(text).trim() + " ";;
		boolean inString = false;
		ArrayList<String> tokens = new ArrayList<>();
		StringBuffer sbuff = new StringBuffer();
		for (int i = 0; i < text.length(); i++) {
			if (inString) {
				if (text.charAt(i) == '\\') {
					i++;
					switch (text.charAt(i)) {
						case '\\':
							sbuff.append('\\');
							continue;
						case 'n':
							sbuff.append('\n');
							continue;
						case 'r':
							sbuff.append('\r');
							continue;
						case 'b':
							sbuff.append('\b');
							continue;
						case 't':
							sbuff.append('\t');
							continue;
						case 'f':
							sbuff.append('\f');
							continue;
						case '\'':
							sbuff.append('\'');
							continue;
						case 'u':
							try {
								int u = Integer.parseInt(
										text.substring(i + 1, i + 5),
										16);
								sbuff.append((char) (u));
								i = i + 4;
							} catch (NumberFormatException e) {
								throw new RuntimeException(/* TODO */);
							}
							continue;
						default:
							throw new RuntimeException(/* TODO */);
					}
				}
				sbuff.append(text.charAt(i));
				if (text.charAt(i) == '\'') inString = false;
				continue;
			}
			switch (text.charAt(i)) {
				case '\'':
					inString = sbuff.length() == 0;
					sbuff.append(text.charAt(i));
					break;
				case '(':
					if (sbuff.length() != 0)
						throw new RuntimeException(/* TODO */);
					int index = text.indexOf(')', i);
					if (index < 0) throw new RuntimeException(/* TODO */);
					tokens.add(text.substring(i, index + 1));
					i = index;
					break;
				case ')':
					throw new RuntimeException(/* TODO */);
				case '[':
					index = text.indexOf(']', i);
					if (index < 0) throw new RuntimeException(/* TODO */);
					i = index;
					break;
				case ']':
					throw new RuntimeException(/* TODO */);
				case ':':
				case '.':
				case ';':
				case ',':
					if (text.charAt(i + 1) == ' ') {
						tokens.add(sbuff.toString());
						sbuff.setLength(0);
						sbuff.append(text.charAt(i));
					} else {
						sbuff.append(text.charAt(i) + "");
					}
					break;
				case ' ':
					tokens.add(sbuff.toString());
					sbuff.setLength(0);
					break;
				default:
					sbuff.append(text.charAt(i));
			}
		}
		if (inString) throw new RuntimeException(/* TODO */);
		return tokens.stream().filter(s -> s.length() != 0)
				.collect(Collectors.toList());
	}
}
