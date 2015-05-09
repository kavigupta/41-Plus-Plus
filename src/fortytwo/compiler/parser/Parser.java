package fortytwo.compiler.parser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fortytwo.compiler.errors.SyntaxMatchingError;
import fortytwo.compiler.struct.Statement42;

public class Parser {
	public static final Pattern LINES = Pattern.compile("(?<line>[^\\.]+)\\.");
	private Parser() {}
	public static ArrayList<Statement42> interpret(String program)
			throws SyntaxMatchingError {
		ArrayList<Statement42> statements = new ArrayList<>();
		Matcher mat = LINES.matcher(program);
		while (mat.find()) {
			statements.add(intepretLine(collapseWhitespace(mat.group("line"))));
		}
		return statements;
	}
	public static String collapseWhitespace(String line) {
		return line.replaceAll("\\s+", " ");
	}
	public static Statement42 intepretLine(String line)
			throws SyntaxMatchingError {
		ArrayList<String> tokens = smartTokenize(line);
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
	private static Statement42 parseDefinition(ArrayList<String> tokens) {
		// TODO Auto-generated method stub
		return null;
	}
	private static Statement42 parseAssignment(ArrayList<String> tokens) {
		// TODO Auto-generated method stub
		return null;
	}
	private static ArrayList<String> smartTokenize(String line) {
		// TODO Auto-generated method stub
		return null;
	}
}
