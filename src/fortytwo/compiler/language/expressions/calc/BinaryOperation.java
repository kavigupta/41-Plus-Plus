package fortytwo.compiler.language.expressions.calc;

import java.math.BigDecimal;
import java.math.RoundingMode;

import fortytwo.compiler.language.expressions.Expression;
import fortytwo.compiler.language.expressions.LiteralExpression;
import fortytwo.compiler.language.expressions.LiteralNumber;
import fortytwo.vm.environment.Environment;

public class BinaryOperation implements Expression {
	public static enum Operation {
		ADD, SUBTRACT, MULTIPLY, DIVIDE, DIVIDE_FLOOR, MOD;
	}
	public static final BigDecimal PRECISION = BigDecimal.TEN.pow(100);
	public final Expression first, second;
	public final Operation operation;
	public BinaryOperation(Expression first, Expression second,
			Operation operation, Environment environment) {
		this.first = first;
		this.second = second;
		if (!first.type(environment).equals("number")
				|| !second.type(environment).equals("number"))
			throw new RuntimeException(/* LOWPRI-E */);
		this.operation = operation;
	}
	@Override
	public String type(Environment environment) {
		return "number";
	}
	@Override
	public LiteralExpression evaluate(Environment environment) {
		BigDecimal bdfirst = ((LiteralNumber) first.evaluate(environment)).contents;
		BigDecimal bdsecond = ((LiteralNumber) second.evaluate(environment)).contents;
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
		throw new RuntimeException(/* LOWPRI-E */);
	}
}
