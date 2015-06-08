package fortytwo.compiler.parsed.expressions;

import java.math.BigDecimal;

import fortytwo.compiler.Context;
import fortytwo.language.Operation;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.expressions.BinaryOperation;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralNumber;

public class ParsedBinaryOperation implements UntypedExpression {
	public final UntypedExpression first, second;
	public final Operation operation;
	private final Context context;
	public ParsedBinaryOperation(UntypedExpression first,
			UntypedExpression second, Operation operation, Context context) {
		this.first = first;
		this.second = second;
		this.operation = operation;
		this.context = context;
	}
	public static ParsedBinaryOperation getNegation(UntypedExpression contents) {
		return new ParsedBinaryOperation(LiteralNumber.getInstance(
				BigDecimal.ZERO, Context.synthetic()), contents,
				Operation.SUBTRACT, contents.context().withUnaryApplied());
	}
	@Override
	public Expression contextualize(StaticEnvironment env) {
		Expression firstE = first.contextualize(env), secondE = second
				.contextualize(env);
		return new BinaryOperation(firstE, secondE, operation, context);
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
		return SourceCode.display(first, operation, second);
	}
	@Override
	public Context context() {
		return context;
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
