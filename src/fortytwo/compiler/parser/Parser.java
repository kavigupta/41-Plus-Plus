package fortytwo.compiler.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
								throw new RuntimeException(/* LOWPRI-E */);
							}
							continue;
						default:
							throw new RuntimeException(/* LOWPRI-E */);
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
						throw new RuntimeException(/* LOWPRI-E */);
					int index = text.indexOf(')', i);
					if (index < 0)
						throw new RuntimeException(/* LOWPRI-E */);
					tokens.add(text.substring(i, index + 1));
					i = index;
					break;
				case ')':
					throw new RuntimeException(/* LOWPRI-E */);
				case '[':
					index = text.indexOf(']', i);
					if (index < 0)
						throw new RuntimeException(/* LOWPRI-E */);
					i = index;
					break;
				case ']':
					throw new RuntimeException(/* LOWPRI-E */);
				case ':':
				case '.':
				case ';':
				case ',':
					if (text.charAt(i + 1) == ' ') {
						tokens.add(sbuff.toString());
						sbuff.setLength(0);
						sbuff.append(text.charAt(i));
					} else {
						sbuff.append(text.charAt(i));
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
		if (inString) throw new RuntimeException(/* LOWPRI-E */);
		return tokens.stream().filter(s -> s.length() != 0)
				.collect(Collectors.toList());
	}
}