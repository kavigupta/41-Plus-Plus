package fortytwo.compiler.parsed.expressions;

import java.math.BigDecimal;

import fortytwo.language.Operation;
import fortytwo.language.SourceCode;
import fortytwo.vm.environment.StaticEnvironment;
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
	public Expression contextualize(StaticEnvironment env) {
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
	@Override
	public String toSourceCode() {
		return SourceCode.display(this);
	}
	@Override
	public String toString() {
		return toSourceCode();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result
				+ ((operation == null) ? 0 : operation.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ParsedBinaryOperation other = (ParsedBinaryOperation) obj;
		if (first == null) {
			if (other.first != null) return false;
		} else if (!first.equals(other.first)) return false;
		if (operation != other.operation) return false;
		if (second == null) {
			if (other.second != null) return false;
		} else if (!second.equals(other.second)) return false;
		return true;
	}
}
