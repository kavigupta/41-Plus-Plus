package fortytwo.compiler.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fortytwo.compiler.language.*;

public class Parser {
	private Parser() {}
	public static List<Statement> parse(String text) {
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
		List<Statement> statements = new ArrayList<>();
		List<List<String>> currentPhrases = new ArrayList<>();
		for (int i = 0; i < phrases.size(); i++) {
			currentPhrases.add(phrases.get(i));
			if (phrases.get(i).get(phrases.get(i).size() - 1).equals(".")) {
				statements.add(parseCompleteStatement(currentPhrases));
				currentPhrases.clear();
			}
		}
		return statements;
	}
	public static Statement parseCompleteStatement(
			List<List<String>> currentPhrases) {
		Expression condition;
		switch (currentPhrases.get(0).get(0)) {
			case "While":
				currentPhrases.get(0).remove(0);
				condition = parseExpression(currentPhrases.get(0));
				List<Statement> statements = new ArrayList<>();
				for (int i = 1; i < currentPhrases.size(); i++) {
					statements.add(parseStatement(currentPhrases.get(i)));
				}
				return WhileLoop.getInstance(condition,
						StatementSeries.getInstance(statements));
			case "If":
				currentPhrases.get(0).remove(0);
				condition = parseExpression(currentPhrases.get(0));
				List<Statement> ifso = new ArrayList<>();
				int i = 1;
				for (; i < currentPhrases.size()
						&& !currentPhrases.get(i).get(0)
								.equals("Otherwise"); i++) {
					ifso.add(parseStatement(currentPhrases.get(i)));
				}
				List<Statement> ifelse = new ArrayList<>();
				for (i++; i < currentPhrases.size(); i++) {
					ifelse.add(parseStatement(currentPhrases.get(i)));
				}
				return IfElse.getInstance(condition,
						StatementSeries.getInstance(ifso),
						StatementSeries.getInstance(ifelse));
		}
		List<Statement> statements = new ArrayList<>();
		for (int i = 0; i < currentPhrases.size(); i++) {
			statements.add(parseStatement(currentPhrases.get(i)));
		}
		return StatementSeries.getInstance(statements);
	}
	private static Statement parseStatement(List<String> line) {
		switch (line.get(0)) {
			case "Run":
				line.remove(0);
				return parseNonVoidFunctionCall(line);
			case "Define":
				return parseDefinition(line);
			case "Set":
				return parseAssignment(line);
			default:
				return parseVoidFunctionCall(line);
		}
	}
	private static Expression parseExpression(List<String> list) {
		// TODO Auto-generated method sstub
		return null;
	}
	private static Expression parseNonVoidFunctionCall(List<String> line) {
		// TODO Auto-generated method stub
		return null;
	}
	private static Statement parseVoidFunctionCall(List<String> line) {
		// TODO Auto-generated method stub
		return null;
	}
	private static Statement parseAssignment(List<String> line) {
		// TODO Auto-generated method stub
		return null;
	}
	private static Statement parseDefinition(List<String> line) {
		// TODO Auto-generated method stub
		return null;
	}
	public static List<String> tokenize42(String text) {
		text = text
				.replaceAll("(?<op>(//)|((>|<)=?)|/?=|\\+|-|\\*|/)",
						" ${op} ").replaceAll("\\s+", " ").trim()
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
