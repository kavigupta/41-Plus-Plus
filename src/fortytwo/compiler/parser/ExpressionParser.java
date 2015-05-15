package fortytwo.compiler.parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import fortytwo.compiler.language.Operation;
import fortytwo.compiler.language.expressions.ParsedBinaryOperation;
import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.expressions.ParsedNegation;
import fortytwo.compiler.language.identifier.VariableIdentifier;
import fortytwo.compiler.language.identifier.functioncomponent.FunctionArgument;
import fortytwo.compiler.language.statements.ParsedFunctionCall;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralBool;
import fortytwo.vm.expressions.LiteralNumber;
import fortytwo.vm.expressions.LiteralString;

public class ExpressionParser {
	public static ParsedExpression parseExpression(List<String> list) {
		ParsedFunctionCall function = ConstructionParser
				.composeFunction(list);
		if (function.signature.function.size() == 1
				&& function.signature.function.get(0) instanceof FunctionArgument)
			return function.arguments.get(0).value;
		return function;
	}
	public static ParsedExpression parsePureExpression(List<String> list) {
		boolean expectsValue = true;
		class UnevaluatedOperator implements ParsedExpression {
			public Operation operator;
			public UnevaluatedOperator(Operation operator) {
				this.operator = operator;
			}
			@Override
			public Expression contextualize(LocalEnvironment env) {
				return null;
				// Should never be called
			}
			@Override
			public SentenceType type() {
				// should never be called
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
					expressions
							.add(new UnevaluatedOperator(Operation.ADD));
					break;
				case '-':
					expressions.add(new UnevaluatedOperator(
							Operation.SUBTRACT));
					break;
				case '*':
					expressions.add(new UnevaluatedOperator(
							Operation.MULTIPLY));
					break;
				case '%':
					expressions
							.add(new UnevaluatedOperator(Operation.MOD));
					break;
				case '/':
					if (token.equals("//"))
						expressions.add(new UnevaluatedOperator(
								Operation.DIVIDE_FLOOR));
					else if (token.equals("/"))
						expressions.add(new UnevaluatedOperator(
								Operation.DIVIDE));
					else throw new RuntimeException(/* LOWPRI-E */);
					break;
				case '_':
					expressions.add(VariableIdentifier.getInstance(token));
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
						expressionsWoUO.add(ParsedNegation
								.getInstance(next));
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
}
