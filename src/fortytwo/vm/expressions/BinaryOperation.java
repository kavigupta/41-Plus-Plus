package fortytwo.vm.expressions;

import java.math.BigDecimal;
import java.math.RoundingMode;

import fortytwo.language.Operation;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.errors.RuntimeErrors;
import fortytwo.vm.errors.TypingErrors;

public class BinaryOperation implements Expression {
	public static final BigDecimal PRECISION = BigDecimal.TEN.pow(100);
	public final Expression first, second;
	public final Operation operation;
	public BinaryOperation(Expression firstE, Expression secondE,
			Operation operation) {
		this.first = firstE;
		this.second = secondE;
		this.operation = operation;
	}
	@Override
	public void execute(LocalEnvironment environment) {
		// executes both branches.
		first.execute(environment);
		second.execute(environment);
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
			RuntimeErrors.divideByZero(this, null); // TODO fix
		switch (operation) {
			case ADD:
				return LiteralNumber.getInstance(bdfirst.add(bdsecond));
			case SUBTRACT:
				return LiteralNumber
						.getInstance(bdfirst.subtract(bdsecond));
			case MULTIPLY:
				return LiteralNumber
						.getInstance(bdfirst.multiply(bdsecond));
			case DIVIDE:
				return LiteralNumber.getInstance(bdfirst
						.multiply(PRECISION)
						.divide(bdsecond, RoundingMode.HALF_EVEN)
						.divide(PRECISION));
			case DIVIDE_FLOOR:
				return LiteralNumber.getInstance(bdfirst
						.divideToIntegralValue(bdsecond));
			case MOD:
				return LiteralNumber.getInstance(bdfirst
						.remainder(bdsecond));
		}
		// This should never happen.
		RuntimeErrors.unrecognizedOperator(this, null);// TODO FIX
		return null;
	}
	@Override
	public boolean typeCheck() {
		if (!first.resolveType().equals(PrimitiveType.NUMBER))
			TypingErrors.nonNumberInArithmeticOperator(this, 1, null);// TODO
															// FIX
		if (!second.resolveType().equals(PrimitiveType.NUMBER))
			TypingErrors.nonNumberInArithmeticOperator(this, 2, null);// TODO
															// FIX
		return true;
	}
	@Override
	public ConcreteType resolveType() {
		typeCheck();
		return PrimitiveType.NUMBER;
	}
}
