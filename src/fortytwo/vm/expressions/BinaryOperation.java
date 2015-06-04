package fortytwo.vm.expressions;

import java.math.BigDecimal;
import java.math.RoundingMode;

import fortytwo.compiler.Context;
import fortytwo.language.Operation;
import fortytwo.language.SourceCode;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.PrimitiveTypeWithoutContext;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.errors.RuntimeErrors;
import fortytwo.vm.errors.TypingErrors;

public class BinaryOperation implements Expression {
	public static final BigDecimal PRECISION = BigDecimal.TEN.pow(100);
	public final Expression first, second;
	public final Operation operation;
	private final Context context;
	public BinaryOperation(Expression firstE, Expression secondE,
			Operation operation, Context context) {
		this.first = firstE;
		this.second = secondE;
		this.operation = operation;
		this.context = context;
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
	public boolean typeCheck() {
		if (!first.resolveType().equals(
				new PrimitiveType(PrimitiveTypeWithoutContext.NUMBER, Context
						.synthetic())))
			TypingErrors.expectedNumberInArithmeticOperator(this, true);
		if (!second.resolveType().equals(
				new PrimitiveType(PrimitiveTypeWithoutContext.NUMBER, Context
						.synthetic())))
			TypingErrors.expectedNumberInArithmeticOperator(this, false);
		return true;
	}
	@Override
	public ConcreteType resolveType() {
		typeCheck();
		return new PrimitiveType(PrimitiveTypeWithoutContext.NUMBER, Context.synthetic());
	}
	@Override
	public Context context() {
		return context;
	}
	@Override
	public boolean isSimple() {
		return true;
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(first, operation, second);
	}
}
