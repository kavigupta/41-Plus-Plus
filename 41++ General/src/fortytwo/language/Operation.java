package fortytwo.language;

import static fortytwo.language.Resources.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

import fortytwo.compiler.Context;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralNumber;

public enum Operation {
	ADD(false, ADDITION_SIGN, ADDITION, 2, (a, b) -> a.add(b)),
	SUBTRACT(false, SUBTRACTION_SIGN, SUBTRACTION, 2, (a, b) -> a.subtract(b)),
	MULTIPLY(false, MULTIPLICATION_SIGN, MULTIPLICATION, 1,
			(a, b) -> a.multiply(b)),
	DIVIDE(true, DIV_SIGN, DIV, 1,
			(a, b) -> a.multiply(BOP.PRECISION)
					.divide(b, RoundingMode.HALF_EVEN).divide(BOP.PRECISION)),
	DIVIDE_FLOOR(true, FLOORDIV_SIGN, FLOORDIV, 1,
			(a, b) -> a.divideToIntegralValue(b)),
	MOD(true, MOD_SIGN, MODULUS, 1, (a, b) -> a.remainder(b));
	public static final int MAX_PRECDENCE = 2;
	public final boolean requiresSecondArgumentNotZero;
	public final String display, noun;
	public final int precendence;
	public final BOP op;
	Operation(boolean requiresSecondArgumentNotZero, String display,
			String noun, int precendence, BOP op) {
		this.requiresSecondArgumentNotZero = requiresSecondArgumentNotZero;
		this.display = display;
		this.noun = noun;
		this.precendence = precendence;
		this.op = op;
	}
	public String toSourceCode() {
		return display;
	}
	public LiteralExpression operate(LiteralExpression a, LiteralExpression b) {
		return new LiteralNumber(
				op.operate(((LiteralNumber) a).contents,
						((LiteralNumber) b).contents),
				Context.sum(Arrays.asList(a, b)));
	}
	private interface BOP {
		public static final BigDecimal PRECISION = BigDecimal.TEN.pow(100);
		public BigDecimal operate(BigDecimal a, BigDecimal b);
	}
}