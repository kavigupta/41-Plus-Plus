package fortytwo.compiler.parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token;
import fortytwo.compiler.parsed.expressions.ParsedBinaryOperation;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.compiler.parsed.statements.ParsedFunctionCall;
import fortytwo.language.Language;
import fortytwo.language.Operation;
import fortytwo.language.Resources;
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
	public static ParsedExpression parseExpression(List<Token> list) {
		ParsedFunctionCall function = ConstructionParser
				.composeFunction(list);
		if (function.name.function.size() == 1
				&& function.name.function.get(0) instanceof FunctionArgument)
			return function.arguments.get(0);
		return function;
	}
	public static ParsedExpression parsePureExpression(
			List<Token> currentExpression) {
		ArrayList<ParsedExpression> expressions = tokenize(currentExpression);
		expressions = removeUnary(expressions);
		for (int precendence = 0; precendence <= Operation.MAX_PRECDENCE; precendence++) {
			expressions = removeBinary(expressions, precendence);
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
				ParsedExpression first = exp.pop();
				i++;
				ParsedExpression second = expressions.get(i);
				UnevaluatedOperator op = (UnevaluatedOperator) token;
				exp.push(new ParsedBinaryOperation(first, second,
						op.operator, Context.sum(Arrays.asList(
								first.context(), op.context(),
								second.context()))));
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
					if (uneop.operator.equals(Operation.ADD)) {
						continue;
					} else if (uneop.operator.equals(Operation.SUBTRACT)) {
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
	private static ArrayList<ParsedExpression> tokenize(List<Token> exp) {
		boolean expectsValue = true;
		ArrayList<ParsedExpression> expressions = new ArrayList<>();
		for (Token token : exp) {
			switch (token.token.charAt(0)) {
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
						expressions.add(LiteralNumber.getInstance(
								new BigDecimal(token.token),
								Context.synthetic()));
						break;
					} catch (NumberFormatException e) {
						throw new RuntimeException(
						/* LOWPRI-E */token.token);
					}
				case '\'':
					expressions.add(LiteralString.getInstance(token
							.subToken(1, token.token.length() - 1)));
					break;
				case 't':
					expressions.add(LiteralBool.getInstance(true,
							Context.synthetic()));
					break;
				case 'f':
					expressions.add(LiteralBool.getInstance(false,
							Context.synthetic()));
					break;
				case '+':
					expressions.add(new UnevaluatedOperator(Operation.ADD,
							token.context));
					break;
				case '-':
					expressions.add(new UnevaluatedOperator(
							Operation.SUBTRACT, token.context));
					break;
				case '*':
					expressions.add(new UnevaluatedOperator(
							Operation.MULTIPLY, token.context));
					break;
				case '%':
					expressions.add(new UnevaluatedOperator(Operation.MOD,
							token.context));
					break;
				case '/':
					if (token.token.equals(Resources.FLOORDIV_SIGN))
						expressions.add(new UnevaluatedOperator(
								Operation.DIVIDE_FLOOR, token.context));
					else if (token.token.equals(Resources.DIV_SIGN))
						expressions.add(new UnevaluatedOperator(
								Operation.DIVIDE, token.context));
					else throw new RuntimeException(/* LOWPRI-E */);
					break;
				case '_':
					expressions.add(VariableIdentifier.getInstance(token));
					break;
				case '(':
					Token depar = Language.deparenthesize(token);
					expressions.add(parseExpression(Tokenizer.tokenize(
							depar.context, depar.token)));
					break;
				case '[':
					break;
			}
		}
		return expressions;
	}
	public static GenericType parseType(Token token) {
		if (token.token.startsWith(Resources.VARIABLE_START))
			return new TypeVariable(VariableIdentifier.getInstance(token));
		while (token.token.startsWith(Resources.OPEN_PAREN))
			token = Language.deparenthesize(token);
		for (PrimitiveType type : PrimitiveType.values()) {
			if (type.typeID().equals(token.token)) return type;
		}
		return parseStructType(Tokenizer.tokenize(token.context, token.token));
	}
	private static GenericType parseStructType(List<Token> tokens) {
		ArrayList<Token> struct = new ArrayList<>();
		int i = 0;
		for (; i < tokens.size() && !tokens.get(i).token.equals(Resources.OF); i++) {
			struct.add(tokens.get(i));
		}
		List<GenericType> typeVariables = new ArrayList<>();
		// Type constructors are not allowed in the post-of clause. (e.g.,
		// what would a structure that takes a (array of _k) as a type
		// variable be?)
		Kind arguments = null;
		if (i < tokens.size() && tokens.get(i).token.equals(Resources.OF)) {
			i++;
			for (; i < tokens.size(); i++) {
				if (Language.isListElement(tokens.get(i).token)) continue;
				GenericType var = parseType(tokens.get(i));
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
		if (struct.stream().map(x -> x.token).collect(Collectors.toList())
				.equals(StdLib42.STRUCT_ARRAY)) {
			if (typeVariables.size() != 1) throw new RuntimeException(/*
														 * LOWPRI-E
														 */);
			if (arguments == Kind.CONCRETE)
				return new ArrayType((ConcreteType) typeVariables.get(0));
			return new GenericArrayType(typeVariables.get(0));
		}
		if (typeVariables.size() == 0)
			return new StructureType(struct, new ArrayList<>());
		if (arguments == Kind.CONCRETE)
			return new StructureType(struct, typeVariables.stream()
					.map(x -> (ConcreteType) x)
					.collect(Collectors.toList()));
		return new GenericStructureType(struct, typeVariables.stream()
				.map(x -> (TypeVariable) x).collect(Collectors.toList()));
	}
	private static class UnevaluatedOperator implements ParsedExpression {
		public Operation operator;
		public Context context;
		public UnevaluatedOperator(Operation operator, Context context) {
			this.operator = operator;
			this.context = context;
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
		@Override
		public Context context() {
			return context;
		}
	}
}
