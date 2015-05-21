package fortytwo.compiler.parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import fortytwo.compiler.parsed.expressions.ParsedBinaryOperation;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.compiler.parsed.statements.ParsedFunctionCall;
import fortytwo.language.Language;
import fortytwo.language.Operation;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.identifier.functioncomponent.FunctionArgument;
import fortytwo.language.type.*;
import fortytwo.library.standard.StdLib42;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralBool;
import fortytwo.vm.expressions.LiteralNumber;
import fortytwo.vm.expressions.LiteralString;

public class ExpressionParser {
	public static ParsedExpression parseExpression(List<String> list) {
		ParsedFunctionCall function = ConstructionParser
				.composeFunction(list);
		System.out.println("Function Name: " + function.name);
		if (function.name.function.size() == 1
				&& function.name.function.get(0) instanceof FunctionArgument)
			return function.arguments.get(0);
		return function;
	}
	public static ParsedExpression parsePureExpression(List<String> list) {
		ArrayList<ParsedExpression> expressions = tokenize(list);
		System.out.println("Original Expression List: " + expressions);
		expressions = removeUnary(expressions);
		System.out.println("Expression List without unary +/-: "
				+ expressions);
		for (int precendence = 0; precendence <= Operation.MAX_PRECDENCE; precendence++) {
			expressions = removeBinary(expressions, precendence);
			System.out.println("With next pass: " + expressions);
		}
		if (expressions.size() != 1)
			throw new RuntimeException(/* LOWPRI-E */);
		return expressions.get(0);
	}
	private static ArrayList<ParsedExpression> removeBinary(
			ArrayList<ParsedExpression> expressions, int precendence) {
		Stack<ParsedExpression> exp = new Stack<>();
		for (int i = 0; i < expressions.size(); i++) {
			ParsedExpression token = expressions.get(i);
			if (token instanceof UnevaluatedOperator
					&& ((UnevaluatedOperator) token).operator.precendence <= precendence) {
				if (exp.size() == 0)
					throw new RuntimeException(/* LOWPRI-E */);
				System.out.println("Unevaluated Operator: " + token);
				ParsedExpression first = exp.pop();
				i++;
				ParsedExpression second = expressions.get(i);
				UnevaluatedOperator op = (UnevaluatedOperator) token;
				exp.push(new ParsedBinaryOperation(first, second,
						op.operator));
			} else {
				exp.push(token);
			}
		}
		return new ArrayList<>(exp);
	}
	private static ArrayList<ParsedExpression> removeUnary(
			ArrayList<ParsedExpression> expressions) {
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
						expressionsWoUO.add(ParsedBinaryOperation
								.getNegation(next));
					}
				} else {
					expressionsWoUO.add(expressions.get(i));
				}
			} else {
				expressionsWoUO.add(expressions.get(i));
			}
		}
		return expressionsWoUO;
	}
	private static ArrayList<ParsedExpression> tokenize(List<String> list) {
		boolean expectsValue = true;
		ArrayList<ParsedExpression> expressions = new ArrayList<>();
		System.out.println("Parsing " + list);
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
					break;
				case '(':
					expressions
							.add(parseExpression(Parser
									.tokenize42(Language
											.deparenthesize(token))));
					break;
			}
		}
		return expressions;
	}
	public static GenericType parseType(String name) {
		if (name.startsWith("_"))
			return new TypeVariable(VariableIdentifier.getInstance(name));
		while (name.startsWith("("))
			name = Language.deparenthesize(name);
		for (PrimitiveType type : PrimitiveType.values()) {
			if (type.typeID().equals(name)) return type;
		}
		return parseStructType(Parser.tokenize42(name));
	}
	private static GenericType parseStructType(List<String> line) {
		System.out.println("Parsing struct type = " + line);
		ArrayList<String> structExpression = new ArrayList<>();
		int i = 0;
		for (; i < line.size() && !line.get(i).equals(";")
				&& !line.get(i).equals("of"); i++) {
			structExpression.add(line.get(i));
		}
		List<GenericType> typeVariables = new ArrayList<>();
		// Type constructors are not allowed in the post-of clause. (e.g.,
		// what would a structure that takes a (array of _k) as a type
		// variable be?)
		Kind arguments = null;
		if (i < line.size() && line.get(i).equals("of")) {
			i++;
			for (; i < line.size() && !line.get(i).equals(";"); i++) {
				if (Language.isListElement(line.get(i))) continue;
				GenericType var = parseType(line.get(i));
				switch (var.kind()) {
					case CONCRETE:
						if (arguments == Kind.VARIABLE)
							throw new RuntimeException(/* LOWPRI-E */);
						arguments = Kind.CONCRETE;
						break;
					case VARIABLE:
						if (arguments == Kind.CONCRETE)
							throw new RuntimeException(/* LOWPRI-E */);
						break;
					case CONSTRUCTOR:
						throw new RuntimeException(/* LOWPRI-E */);
				}
				typeVariables.add(var);
			}
		}
		if (structExpression.equals(StdLib42.STRUCT_ARRAY)) {
			if (typeVariables.size() != 1) throw new RuntimeException(/*
														 * LOWPRI-E
														 */);
			if (arguments == Kind.CONCRETE)
				return new ArrayType((ConcreteType) typeVariables.get(0));
			return new GenericArrayType(typeVariables.get(0));
		}
		if (typeVariables.size() == 0)
			return new StructureType(structExpression, new ArrayList<>());
		if (arguments == Kind.CONCRETE)
			return new StructureType(structExpression, typeVariables
					.stream().map(x -> (ConcreteType) x)
					.collect(Collectors.toList()));
		return new GenericStructureType(structExpression, typeVariables
				.stream().map(x -> (TypeVariable) x)
				.collect(Collectors.toList()));
	}
	private static class UnevaluatedOperator implements ParsedExpression {
		public Operation operator;
		public UnevaluatedOperator(Operation operator) {
			this.operator = operator;
		}
		@Override
		public Expression contextualize(StaticEnvironment env) {
			return null;
			// Should never be called
		}
		@Override
		public SentenceType type() {
			// should never be called
			return null;
		}
		@Override
		public String toSourceCode() {
			// should never be called
			return operator.toSourceCode();
		}
		@Override
		public String toString() {
			return toSourceCode();
		}
	}
}
