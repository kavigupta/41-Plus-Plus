package fortytwo.compiler.language.expressions;

import fortytwo.compiler.language.Operation;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.expressions.BinaryOperation;
import fortytwo.vm.expressions.Expression;

public class ParsedBinaryOperation implements ParsedExpression {
	public final ParsedExpression first, second;
	public final Operation operation;
	public ParsedBinaryOperation(ParsedExpression first,
			ParsedExpression second, Operation operation) {
		this.first = first;
		this.second = second;
		this.operation = operation;
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
		// TODO Auto-generated method stub
		return null;
	}
	// @Override
	// public String type(Environment environment) {
	// return "number";
	// }
	// @Override
	// public LiteralExpression evaluate(Environment environment) {
	// BigDecimal bdfirst = ((LiteralNumber)
	// first.evaluate(environment)).contents;
	// BigDecimal bdsecond = ((LiteralNumber)
	// second.evaluate(environment)).contents;
	// switch (operation) {
	// case ADD:
	// return LiteralNumber.getInstance(bdfirst.add(bdsecond));
	// case SUBTRACT:
	// return LiteralNumber
	// .getInstance(bdfirst.subtract(bdsecond));
	// case MULTIPLY:
	// return LiteralNumber
	// .getInstance(bdfirst.multiply(bdsecond));
	// case DIVIDE:
	// return LiteralNumber.getInstance(bdfirst
	// .multiply(PRECISION)
	// .divide(bdsecond, RoundingMode.HALF_EVEN)
	// .divide(PRECISION));
	// case DIVIDE_FLOOR:
	// return LiteralNumber.getInstance(bdfirst
	// .divideToIntegralValue(bdsecond));
	// case MOD:
	// return LiteralNumber.getInstance(bdfirst
	// .remainder(bdsecond));
	// }
	// throw new RuntimeException(/* LOWPRI-E */);
	// }
}
