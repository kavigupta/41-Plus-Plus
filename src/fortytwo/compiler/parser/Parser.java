package fortytwo.compiler.parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import lib.standard.collections.Pair;
import fortytwo.compiler.language.Language;
import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.expressions.Variable;
import fortytwo.compiler.language.expressions.calc.Negation;
import fortytwo.compiler.language.expressions.calc.ParsedBinaryOperation;
import fortytwo.compiler.language.functioncall.FunctionArgument;
import fortytwo.compiler.language.functioncall.FunctionCall;
import fortytwo.compiler.language.functioncall.FunctionComponent;
import fortytwo.compiler.language.functioncall.FunctionToken;
import fortytwo.compiler.language.statements.*;
import fortytwo.compiler.language.statements.functions.FunctionReturn;
import fortytwo.compiler.language.statements.functions.StructureDefinition;
import fortytwo.vm.environment.Environment;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralBool;
import fortytwo.vm.expressions.LiteralNumber;
import fortytwo.vm.expressions.LiteralString;

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
				statements.add(parseCompleteStatement(currentPhrases));
				currentPhrases.clear();
			}
		}
		return statements;
	}
	public static ParsedStatement parseCompleteStatement(
			List<List<String>> currentPhrases) {
		ParsedExpression condition;
		switch (currentPhrases.get(0).get(0)) {
			case "While":
				currentPhrases.get(0).remove(0);
				condition = parseExpression(currentPhrases.get(0));
				List<ParsedStatement> statements = new ArrayList<>();
				for (int i = 1; i < currentPhrases.size(); i++) {
					statements.add(parseStatement(currentPhrases.get(i)));
				}
				return new WhileLoop(condition, new StatementSeries(
						statements));
			case "If":
				currentPhrases.get(0).remove(0);
				condition = parseExpression(currentPhrases.get(0));
				List<ParsedStatement> ifso = new ArrayList<>();
				int i = 1;
				for (; i < currentPhrases.size()
						&& !currentPhrases.get(i).get(0)
								.equals("otherwise"); i++) {
					ifso.add(parseStatement(currentPhrases.get(i)));
				}
				List<ParsedStatement> ifelse = new ArrayList<>();
				for (i++; i < currentPhrases.size(); i++) {
					ifelse.add(parseStatement(currentPhrases.get(i)));
				}
				return IfElse.getInstance(condition, new StatementSeries(
						ifso), new StatementSeries(ifelse));
		}
		List<ParsedStatement> statements = new ArrayList<>();
		for (int i = 0; i < currentPhrases.size(); i++) {
			statements.add(parseStatement(currentPhrases.get(i)));
		}
		return new StatementSeries(statements);
	}
	private static ParsedStatement parseStatement(List<String> line) {
		switch (line.get(0)) {
			case "Run":
				line.remove(0);
				return parseExpression(line);
			case "Define":
				return parseDefinition(line);
			case "Set":
				return parseAssignment(line);
			case "Exit":
				return parseReturn(line);
			default:
				return parseVoidFunctionCall(line);
		}
	}
	private static ParsedStatement parseReturn(List<String> line) {
		/* Exit the function( and output <output>)?. */
		if (!line.get(1).equals("the") || !line.get(2).equals("function"))
			throw new RuntimeException(/* LOWPRI-E */);
		if (line.size() == 3) return new FunctionReturn(null);
		if (!line.get(3).equals("and") || !line.get(4).equals("output"))
			throw new RuntimeException(/* LOWPRI-E */);
		line.subList(0, 5).clear();
		return new FunctionReturn(parseExpression(line));
	}
	private static ParsedExpression parseExpression(List<String> list) {
		List<FunctionComponent> function = composeFunction(list);
		if (function.size() == 1
				&& function.get(0) instanceof FunctionArgument)
			return ((FunctionArgument) function.get(0)).value;
		return FunctionCall.getInstance(function);
	}
	private static ParsedExpression parsePureExpression(List<String> list) {
		boolean expectsValue = true;
		class UnevaluatedOperator implements ParsedExpression {
			public ParsedBinaryOperation.Operation operator;
			public UnevaluatedOperator(
					ParsedBinaryOperation.Operation operator) {
				this.operator = operator;
			}
			@Override
			public Expression contextualize(Environment env) {
				return null;
			}
		}
		List<ParsedExpression> expressions = new ArrayList<>();
		for (String token : list) {
			switch (token.charAt(0)) {
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
					if (!expectsValue) throw new RuntimeException(/*
														 * LOWPRI-E
														 */);
					try {
						expressions.add(LiteralNumber
								.getInstance(new BigDecimal(token)));
						break;
					} catch (NumberFormatException e) {
						throw new RuntimeException(/* LOWPRI-E */);
					}
				case '\'':
					expressions.add(LiteralString.getInstance(token
							.substring(1, token.length() - 1)));
					break;
				case 't':
					expressions.add(LiteralBool.TRUE);
					break;
				case 'f':
					expressions.add(LiteralBool.FALSE);
					break;
				case '+':
					expressions.add(new UnevaluatedOperator(
							ParsedBinaryOperation.Operation.ADD));
					break;
				case '-':
					expressions.add(new UnevaluatedOperator(
							ParsedBinaryOperation.Operation.SUBTRACT));
					break;
				case '*':
					expressions.add(new UnevaluatedOperator(
							ParsedBinaryOperation.Operation.MULTIPLY));
					break;
				case '%':
					expressions.add(new UnevaluatedOperator(
							ParsedBinaryOperation.Operation.MOD));
					break;
				case '/':
					if (token.equals("//"))
						expressions
								.add(new UnevaluatedOperator(
										ParsedBinaryOperation.Operation.DIVIDE_FLOOR));
					else if (token.equals("/"))
						expressions
								.add(new UnevaluatedOperator(
										ParsedBinaryOperation.Operation.DIVIDE));
					else throw new RuntimeException(/* TODO */);
					break;
				case '_':
					expressions.add(new Variable(token));
			}
		}
		ArrayList<ParsedExpression> expressionsWoUO = new ArrayList<>();
		for (int i = 0; i < expressions.size(); i++) {
			if (expressions.get(i) instanceof UnevaluatedOperator) {
				UnevaluatedOperator uneop = (UnevaluatedOperator) expressions
						.get(i);
				if (i == 0
						|| expressions.get(i - 1) instanceof UnevaluatedOperator) {
					if (uneop.operator.equals("+")) {
						i++;
						continue;
					} else if (uneop.operator.equals("-")) {
						i++;
						ParsedExpression next = expressions.get(i);
						expressionsWoUO.add(Negation.getInstance(next));
					}
				}
			}
		}
		Stack<ParsedExpression> exp = new Stack<>();
		for (int i = 0; i < expressionsWoUO.size(); i++) {
			ParsedExpression token = expressionsWoUO.get(i);
			if (token instanceof UnevaluatedOperator) {
				if (exp.size() == 0)
					throw new RuntimeException(/* LOWPRI-E */);
				ParsedExpression first = exp.pop();
				i++;
				ParsedExpression second = expressionsWoUO.get(i);
				UnevaluatedOperator op = (UnevaluatedOperator) token;
				exp.push(new ParsedBinaryOperation(first, second,
						op.operator));
			} else {
				exp.push(token);
			}
		}
		ArrayList<ParsedExpression> expressionsApartfromPM = new ArrayList<>(
				exp);
		exp = new Stack<>();
		for (int i = 0; i < expressionsApartfromPM.size(); i++) {
			ParsedExpression token = expressionsApartfromPM.get(i);
			if (token instanceof UnevaluatedOperator) {
				if (exp.size() == 0)
					throw new RuntimeException(/* LOWPRI-E */);
				ParsedExpression first = exp.pop();
				i++;
				ParsedExpression second = expressionsApartfromPM.get(i);
				UnevaluatedOperator op = (UnevaluatedOperator) token;
				exp.push(new ParsedBinaryOperation(first, second,
						op.operator));
			} else {
				exp.push(token);
			}
		}
		if (exp.size() != 1) throw new RuntimeException(/* LOWPRI-E */);
		return exp.pop();
	}
	private static ParsedStatement parseVoidFunctionCall(List<String> list) {
		List<FunctionComponent> function = composeFunction(list);
		if (function.size() == 1
				&& function.get(0) instanceof FunctionArgument)
			throw new RuntimeException(/* LOWPRI-E non-void function call */);
		return FunctionCall.getInstance(function);
		// TODO check that return type of function is void
	}
	private static List<FunctionComponent> composeFunction(List<String> list) {
		List<FunctionComponent> function = new ArrayList<>();
		List<String> currentExpression = new ArrayList<>();
		for (String token : list) {
			switch (token.charAt(0)) {
				case 't':
				case '(':
				case '+':
				case '-':
				case '*':
				case '/':
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
				case '\'':
				case '_':
					currentExpression.add(token);
				default:
					if (token.equals("true") || token.equals("false"))
						currentExpression.add(token);
					else {
						if (currentExpression.size() != 0) {
							function.add(new FunctionArgument(
									parsePureExpression(currentExpression)));
							currentExpression.clear();
						}
						function.add(new FunctionToken(token));
					}
			}
		}
		if (currentExpression.size() != 0) {
			function.add(new FunctionArgument(
					parsePureExpression(currentExpression)));
			currentExpression.clear();
		}
		return function;
	}
	private static ParsedStatement parseAssignment(List<String> line) {
		/*
		 * Set the <field> of <name> to <value>.
		 */
		if (!line.get(1).equals("the") || !line.get(3).equals("of")
				|| !line.get(5).equals("to"))
			throw new RuntimeException(/* LOWPRI-E */);
		String field = line.get(2), name = line.get(4);
		line.subList(0, 6).clear();
		return new Assignment(name, field, parseExpression(line));
	}
	private static ParsedStatement parseDefinition(List<String> line) {
		/*
		 * Define a[n] <type> called <name>( with a <field1> of <value1>, a
		 * <field2> of <value2>, ...)?.
		 */
		if (!Language.isArticle(line.get(1)) || line.get(3).equals("called"))
			throw new RuntimeException(/* LOWPRI-E */);
		String type = Language.deparenthesize(line.get(2));
		if (type.equals("function")) return parseFunctionDefinition(line);
		if (type.equals("type")) return parseStructDefinition(line);
		String name = line.get(4);
		ArrayList<Pair<String, ParsedExpression>> fields = new ArrayList<>();
		for (int i = 5; i < line.size(); i++) {
			if (!line.get(i).equals("of")) continue;
			String field = line.get(i - 1);
			ArrayList<String> tokens = new ArrayList<>();
			i++;
			while (!line.get(i).equals(",")) {
				tokens.add(line.get(i));
				i++;
			}
			fields.add(Pair.getInstance(field, parseExpression(tokens)));
		}
		return new Definition(type, name, fields);
	}
	private static ParsedStatement parseStructDefinition(List<String> line) {
		/*
		 * Define a structure called <structure expression> ; which contains
		 * a[n] <type> called <field> , a[n] <type> called <field>, ...
		 */
		if (!line.get(1).equals("a") || !line.get(3).equals("called"))
			throw new RuntimeException(/* TODO */);
		int i = 4;
		ArrayList<String> structExpression = new ArrayList<>();
		for (; i < line.size() && !line.get(i).equals(";"); i++) {
			structExpression.add(line.get(i));
		}
		ArrayList<Pair<ParsedExpression, String>> fields = new ArrayList<>();
		for (; i < line.size(); i++) {
			if (!line.get(i).equals("called")) continue;
			fields.add(Pair.getInstance(
					parseExpression(Arrays.asList(line.get(i - 1))),
					line.get(i + 1)));
		}
		// TODO parse struct expression.
		return StructureDefinition.getInstance(null, fields);
	}
	private static ParsedStatement parseFunctionDefinition(List<String> line) {
		// TODO Auto-generated method stub
		return null;
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
		if (inString) throw new RuntimeException(/* LOWPRI-E */);
		return tokens.stream().filter(s -> s.length() != 0)
				.collect(Collectors.toList());
	}
}
