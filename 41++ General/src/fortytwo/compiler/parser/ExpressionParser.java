package fortytwo.compiler.parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import fortytwo.compiler.Context;
import fortytwo.compiler.LiteralToken;
import fortytwo.compiler.parsed.expressions.BinaryOperation;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.compiler.parsed.statements.ParsedFunctionCall;
import fortytwo.language.Language;
import fortytwo.language.Operation;
import fortytwo.language.Resources;
import fortytwo.language.classification.ExpressionType;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.identifier.functioncomponent.FunctionArgument;
import fortytwo.language.type.*;
import fortytwo.library.standard.StdLib42;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.errors.ParserErrors;
import fortytwo.vm.errors.SyntaxErrors;
import fortytwo.vm.errors.TypingErrors;
import fortytwo.vm.expressions.LiteralBool;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralNumber;
import fortytwo.vm.expressions.LiteralString;

public class ExpressionParser {
	public static Expression parseExpression(List<LiteralToken> list) {
		ParsedFunctionCall function = ConstructionParser.composeFunction(list);
		if (function.name.function.size() == 1
				&& function.name.function.get(0) instanceof FunctionArgument)
			return function.arguments.get(0);
		return function;
	}
	public static Expression parsePureExpression(
			List<LiteralToken> currentExpression) {
		ArrayList<Expression> originalExpressions = tokenize(currentExpression);
		ArrayList<Expression> expressions = removeUnary(originalExpressions);
		for (int precendence = 0; precendence <= Operation.MAX_PRECDENCE; precendence++) {
			expressions = removeBinary(expressions, precendence);
		}
		if (expressions.size() != 1)
			SyntaxErrors.invalidExpression(ExpressionType.ARITHMETIC,
					currentExpression);
		return expressions.get(0);
	}
	private static ArrayList<Expression> removeBinary(
			ArrayList<Expression> expressions, int precendence) {
		Stack<Expression> exp = new Stack<>();
		for (int i = 0; i < expressions.size(); i++) {
			Expression token = expressions.get(i);
			if (token instanceof UnevaluatedOperator
					&& ((UnevaluatedOperator) token).operator.precendence <= precendence) {
				if (exp.size() == 0)
					SyntaxErrors.invalidExpression(ExpressionType.ARITHMETIC,
							expressions.stream().map(Expression::toToken)
									.collect(Collectors.toList()));
				Expression first = exp.pop();
				i++;
				Expression second = expressions.get(i);
				UnevaluatedOperator op = (UnevaluatedOperator) token;
				exp.push(new BinaryOperation(first, second, op.operator,
						Context.sum(Arrays.asList(first.context(), op.context(),
								second.context()))));
			} else {
				exp.push(token);
			}
		}
		return new ArrayList<>(exp);
	}
	private static ArrayList<Expression> removeUnary(
			ArrayList<Expression> expressions) {
		ArrayList<Expression> expressionsWoUO = new ArrayList<>();
		for (int i = 0; i < expressions.size(); i++) {
			if (expressions.get(i) instanceof UnevaluatedOperator) {
				UnevaluatedOperator uneop = (UnevaluatedOperator) expressions
						.get(i);
				if (i == 0 || expressions
						.get(i - 1) instanceof UnevaluatedOperator) {
					if (uneop.operator.equals(Operation.ADD)) {
						continue;
					} else if (uneop.operator.equals(Operation.SUBTRACT)) {
						i++;
						Expression next = expressions.get(i);
						expressionsWoUO.add(BinaryOperation.getNegation(next));
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
	private static ArrayList<Expression> tokenize(List<LiteralToken> exp) {
		ArrayList<Expression> expressions = new ArrayList<>();
		for (LiteralToken token : exp) {
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
					try {
						expressions.add(LiteralNumber.getInstance(
								new BigDecimal(token.token), token.context));
						break;
					} catch (NumberFormatException e) {
						SyntaxErrors.invalidExpression(
								ExpressionType.LITERAL_NUMBER,
								Arrays.asList(token));
					}
				case '\'':
					expressions.add(LiteralString.getInstance(
							token.subToken(1, token.token.length() - 1)));
					break;
				case 't':
					expressions.add(
							LiteralBool.getInstance(true, Context.SYNTHETIC));
					break;
				case 'f':
					expressions.add(
							LiteralBool.getInstance(false, Context.SYNTHETIC));
					break;
				case '+':
					expressions.add(new UnevaluatedOperator(Operation.ADD,
							token.context));
					break;
				case '-':
					expressions.add(new UnevaluatedOperator(Operation.SUBTRACT,
							token.context));
					break;
				case '*':
					expressions.add(new UnevaluatedOperator(Operation.MULTIPLY,
							token.context));
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
					// should not happen.
					break;
				case '\"':
					expressions.add(VariableIdentifier.getInstance(token));
					break;
				case '(':
					LiteralToken depar = Language.deparenthesize(token);
					expressions.add(parseExpression(Tokenizer.tokenize(depar)));
					break;
				case '[':
					break;
			}
		}
		return expressions;
	}
	public static GenericType parseType(LiteralToken token) {
		if (token.token.startsWith(Resources.VARIABLE_BEGIN))
			return new TypeVariable(VariableIdentifier.getInstance(token));
		while (token.token.startsWith(Resources.OPEN_PAREN))
			token = Language.deparenthesize(token);
		for (PrimitiveTypeWOC type : PrimitiveTypeWOC.values()) {
			if (type.typeID().equals(token.token))
				return new PrimitiveType(type, token.context);
		}
		return parseStructType(Tokenizer.tokenize(token));
	}
	private static GenericType parseStructType(List<LiteralToken> tokens) {
		Context context = Context.sum(tokens);
		ArrayList<LiteralToken> struct = new ArrayList<>();
		int i = 0;
		for (; i < tokens.size()
				&& !tokens.get(i).token.equals(Resources.OF); i++) {
			struct.add(tokens.get(i));
		}
		List<GenericType> typeVariables = new ArrayList<>();
		Kind arguments = null;
		if (i < tokens.size() && tokens.get(i).token.equals(Resources.OF)) {
			i++;
			for (; i < tokens.size(); i++) {
				if (Language.isListElement(tokens.get(i).token)) continue;
				GenericType var = parseType(tokens.get(i));
				switch (var.kind()) {
					case CONCRETE:
						if (arguments == Kind.VARIABLE)
							ParserErrors.expectedVariableInDecl(false,
									tokens.get(i), tokens);
						arguments = Kind.CONCRETE;
						break;
					case VARIABLE:
						arguments = Kind.VARIABLE;
						break;
					case CONSTRUCTOR:
						arguments = Kind.VARIABLE;
				}
				typeVariables.add(var);
			}
		}
		if (struct.stream().map(x -> x.token).collect(Collectors.toList())
				.equals(StdLib42.STRUCT_ARRAY)) {
			if (typeVariables.size() != 1)
				TypingErrors.invalidArrayType(tokens, typeVariables);
			if (arguments == Kind.CONCRETE)
				return new ArrayType((ConcreteType) typeVariables.get(0),
						context);
			return new GenericArrayType(typeVariables.get(0), context);
		}
		if (typeVariables.size() == 0)
			return new StructureType(struct, new ArrayList<>(), context);
		if (arguments == Kind.CONCRETE)
			return new StructureType(struct, typeVariables.stream()
					.map(x -> (ConcreteType) x).collect(Collectors.toList()),
					context);
		return new GenericStructureType(struct, typeVariables, context);
	}
	private static class UnevaluatedOperator extends Expression {
		public Operation operator;
		public Context context;
		public UnevaluatedOperator(Operation operator, Context context) {
			this.operator = operator;
			this.context = context;
		}
		@Override
		public ConcreteType findType(StaticEnvironment env) {
			// should never be called
			return null;
		}
		@Override
		public SentenceType kind() {
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
		@Override
		public LiteralExpression literalValue(LocalEnvironment environment) {
			// should never be called
			return null;
		}
	}
}
