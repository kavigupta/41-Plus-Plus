package fortytwo.compiler.parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import fortytwo.compiler.language.expressions.*;
import fortytwo.compiler.language.expressions.calc.*;
import fortytwo.compiler.language.functioncall.FunctionArgument;
import fortytwo.compiler.language.functioncall.FunctionCall;
import fortytwo.compiler.language.functioncall.FunctionComponent;
import fortytwo.compiler.language.functioncall.FunctionToken;
import fortytwo.compiler.language.statements.IfElse;
import fortytwo.compiler.language.statements.Statement;
import fortytwo.compiler.language.statements.StatementSeries;
import fortytwo.compiler.language.statements.WhileLoop;

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
				return parseExpression(line);
			case "Define":
				return parseDefinition(line);
			case "Set":
				return parseAssignment(line);
			default:
				return parseVoidFunctionCall(line);
		}
	}
	private static Expression parseExpression(List<String> list) {
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
		if (function.size() == 1
				&& function.get(0) instanceof FunctionArgument)
			return ((FunctionArgument) function.get(0)).value;
		return FunctionCall.getInstance(function);
	}
	private static Expression parsePureExpression(List<String> list) {
		boolean expectsValue = true;
		class UnevaluatedOperator implements Expression {
			public String operator;
			public UnevaluatedOperator(String operator) {
				this.operator = operator;
			}
		}
		List<Expression> expressions = new ArrayList<>();
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
					if (!expectsValue)
						throw new RuntimeException(/* TODO */);
					try {
						expressions.add(LiteralNumber
								.getInstance(new BigDecimal(token)));
						break;
					} catch (NumberFormatException e) {
						throw new RuntimeException(/* TODO */);
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
				case '-':
				case '*':
				case '/':
					expressions.add(new UnevaluatedOperator(token));
					break;
				case '_':
					expressions.add(Variable.getInstance(token));
			}
		}
		ArrayList<Expression> expressionsWoUO = new ArrayList<>();
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
						Expression next = expressions.get(i);
						expressionsWoUO.add(new Negation(next));
					}
				}
			}
		}
		Stack<Expression> exp = new Stack<>();
		for (int i = 0; i < expressionsWoUO.size(); i++) {
			Expression token = expressionsWoUO.get(i);
			if (token instanceof UnevaluatedOperator) {
				if (exp.size() == 0) throw new RuntimeException(/* TODO */);
				Expression first = exp.pop();
				i++;
				Expression second = expressionsWoUO.get(i);
				UnevaluatedOperator op = (UnevaluatedOperator) token;
				if (op.operator.equals("*")) {
					exp.push(new Multiplication(first, second));
				} else if (op.operator.equals("/")) {
					exp.push(new Division(first, second));
				} else if (op.operator.equals("//")) {
					exp.push(new FloorDivision(first, second));
				} else if (op.operator.equals("%")) {
					exp.push(new Modulus(first, second));
				}
			} else {
				exp.push(token);
			}
		}
		ArrayList<Expression> expressionsApartfromPM = new ArrayList<>(exp);
		exp = new Stack<>();
		for (int i = 0; i < expressionsApartfromPM.size(); i++) {
			Expression token = expressionsApartfromPM.get(i);
			if (token instanceof UnevaluatedOperator) {
				if (exp.size() == 0) throw new RuntimeException(/* TODO */);
				Expression first = exp.pop();
				i++;
				Expression second = expressionsApartfromPM.get(i);
				UnevaluatedOperator op = (UnevaluatedOperator) token;
				if (op.operator.equals("+")) {
					exp.push(new Addition(first, second));
				} else if (op.operator.equals("-")) {
					exp.push(new Subtraction(first, second));
				}
			} else {
				exp.push(token);
			}
		}
		if (exp.size() != 1) throw new RuntimeException(/* TODO */);
		return exp.pop();
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
