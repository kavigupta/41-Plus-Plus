package fortytwo.vm.expressions;

import java.math.BigDecimal;
import java.math.RoundingMode;

import fortytwo.compiler.language.Operation;
import fortytwo.vm.environment.LocalEnvironment;

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
		if (!(f instanceof LiteralNumber) || !(s instanceof LiteralNumber))
			throw new RuntimeException(/* LOWPRI-E */);
		BigDecimal bdfirst = ((LiteralNumber) f).contents;
		BigDecimal bdsecond = ((LiteralNumber) s).contents;
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
		throw new RuntimeException(/* LOWPRI-E */);
	}
}
