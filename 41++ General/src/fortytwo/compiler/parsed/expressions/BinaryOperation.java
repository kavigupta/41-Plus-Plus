package fortytwo.compiler.parsed.expressions;

import java.math.BigDecimal;

import fortytwo.compiler.Context;
import fortytwo.language.Operation;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.PrimitiveTypeWOC;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.errors.TypingErrors;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralNumber;

/**
 * A structure representing an operation between two expressions.
 */
public class BinaryOperation extends Expression {
	public static final BigDecimal PRECISION = BigDecimal.TEN.pow(100);
	/**
	 * The first element in the operation
	 */
	public final Expression first;
	/**
	 * The second element in the operation
	 */
	public final Expression second;
	/**
	 * The operation to be used
	 */
	public final Operation operation;
	private final Context context;
	/**
	 * Struct constructor.
	 */
	public BinaryOperation(Expression first, Expression second,
			Operation operation, Context context) {
		this.first = first;
		this.second = second;
		this.operation = operation;
		this.context = context;
	}
	/**
	 * @return {@code -x}
	 */
	public static BinaryOperation getNegation(Expression x) {
		return new BinaryOperation(LiteralNumber.getInstance(BigDecimal.ZERO,
				Context.SYNTHETIC), x, Operation.SUBTRACT, x.context()
				.withUnaryApplied());
	}
	@Override
	public LiteralExpression literalValue(LocalEnvironment environment) {
		isTypeChecked(environment.staticEnvironment());
		return operation.operate(first.literalValue(environment),
				second.literalValue(environment));
	}
	@Override
	public ConcreteType findType(StaticEnvironment env) {
		PrimitiveType number = PrimitiveType
				.synthetic(PrimitiveTypeWOC.NUMBER);
		if (!first.type(env).equals(number))
			TypingErrors.expectedNumberInArithmeticOperator(this, true, env);
		if (!second.type(env).equals(number))
			TypingErrors
					.expectedNumberInArithmeticOperator(this, false, env);
		return number;
	}
	@Override
	public SentenceType kind() {
		if (first.kind() == SentenceType.PURE_EXPRESSION
				&& second.kind() == SentenceType.PURE_EXPRESSION)
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
		BinaryOperation other = (BinaryOperation) obj;
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
