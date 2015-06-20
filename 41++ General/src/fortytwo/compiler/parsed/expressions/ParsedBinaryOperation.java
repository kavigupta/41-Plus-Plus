package fortytwo.compiler.parsed.expressions;

import java.math.BigDecimal;
import java.math.RoundingMode;

import fortytwo.compiler.Context;
import fortytwo.language.Operation;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.PrimitiveTypeWithoutContext;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.errors.RuntimeErrors;
import fortytwo.vm.errors.TypingErrors;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralNumber;

/**
 * A structure representing an operation between two parsed expressions.
 */
public class ParsedBinaryOperation implements ParsedExpression {
	public static final BigDecimal PRECISION = BigDecimal.TEN.pow(100);
	/**
	 * The first element in the operation
	 */
	public final ParsedExpression first;
	/**
	 * The second element in the operation
	 */
	public final ParsedExpression second;
	/**
	 * The operation to be used
	 */
	public final Operation operation;
	private final Context context;
	/**
	 * Struct constructor.
	 */
	public ParsedBinaryOperation(ParsedExpression first,
			ParsedExpression second, Operation operation, Context context) {
		this.first = first;
		this.second = second;
		this.operation = operation;
		this.context = context;
	}
	/**
	 * @return {@code -x}
	 */
	public static ParsedBinaryOperation getNegation(ParsedExpression x) {
		return new ParsedBinaryOperation(LiteralNumber.getInstance(
				BigDecimal.ZERO, Context.SYNTHETIC), x, Operation.SUBTRACT,
				x.context().withUnaryApplied());
	}
	@Override
	public boolean typeCheck(StaticEnvironment env) {
		if (!first.resolveType(env).equals(
				new PrimitiveType(PrimitiveTypeWithoutContext.NUMBER,
						Context.SYNTHETIC)))
			TypingErrors.expectedNumberInArithmeticOperator(this, true, env);
		if (!second.resolveType(env).equals(
				new PrimitiveType(PrimitiveTypeWithoutContext.NUMBER,
						Context.SYNTHETIC)))
			TypingErrors
					.expectedNumberInArithmeticOperator(this, false, env);
		return true;
	}
	@Override
	public LiteralExpression literalValue(LocalEnvironment environment) {
		LiteralExpression f = first.literalValue(environment);
		LiteralExpression s = second.literalValue(environment);
		// has already been typechecked.
		BigDecimal bdfirst = ((LiteralNumber) f).contents;
		BigDecimal bdsecond = ((LiteralNumber) s).contents;
		if (operation.requiresSecondArgumentNotZero
				&& bdsecond.compareTo(BigDecimal.ZERO) == 0)
			RuntimeErrors.divideByZero(this, context);
		switch (operation) {
			case ADD:
				return LiteralNumber.getInstance(bdfirst.add(bdsecond),
						context);
			case SUBTRACT:
				return LiteralNumber.getInstance(
						bdfirst.subtract(bdsecond), context);
			case MULTIPLY:
				return LiteralNumber.getInstance(
						bdfirst.multiply(bdsecond), context);
			case DIVIDE:
				return LiteralNumber.getInstance(
						bdfirst.multiply(PRECISION)
								.divide(bdsecond,
										RoundingMode.HALF_EVEN)
								.divide(PRECISION), context);
			case DIVIDE_FLOOR:
				return LiteralNumber.getInstance(
						bdfirst.divideToIntegralValue(bdsecond), context);
			case MOD:
				return LiteralNumber.getInstance(
						bdfirst.remainder(bdsecond), context);
		}
		// This should never happen.
		return null;
	}
	@Override
	public void execute(LocalEnvironment environment) {
		literalValue(environment);
	}
	@Override
	public ConcreteType resolveType(StaticEnvironment env) {
		typeCheck(env);
		return new PrimitiveType(PrimitiveTypeWithoutContext.NUMBER,
				Context.SYNTHETIC);
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
