package fortytwo.compiler.language.expressions;

import java.math.BigDecimal;

import fortytwo.compiler.language.Operation;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.expressions.BinaryOperation;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralNumber;

public class ParsedBinaryOperation implements ParsedExpression {
	public final ParsedExpression first, second;
	public final Operation operation;
	public ParsedBinaryOperation(ParsedExpression first,
			ParsedExpression second, Operation operation) {
		this.first = first;
		this.second = second;
		this.operation = operation;
	}
	public static ParsedBinaryOperation getNegation(ParsedExpression contents) {
		return new ParsedBinaryOperation(
				LiteralNumber.getInstance(BigDecimal.ZERO), contents,
				Operation.SUBTRACT);
	}
	@Override
	public Expression contextualize(LocalEnvironment env) {
		Expression firstE = first.contextualize(env), secondE = second
				.contextualize(env);
		if (firstE == null || secondE == null)
			throw new RuntimeException(/* LOWPRI-E */);
		return new BinaryOperation(firstE, secondE, operation);
	}
	@Override
	public SentenceType type() {
		if (first.type() == SentenceType.PURE_EXPRESSION
				&& second.type() == SentenceType.PURE_EXPRESSION)
			return SentenceType.PURE_EXPRESSION;
		return SentenceType.IMPURE_EXPRESSION;
	}
}
