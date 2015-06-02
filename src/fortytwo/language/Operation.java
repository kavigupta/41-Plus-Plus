package fortytwo.language;

import static fortytwo.language.Resources.*;

public enum Operation {
	ADD(false, ADDITION_SIGN, ADDITION, 2), SUBTRACT(false, SUBTRACTION_SIGN,
			SUBTRACTION, 2), MULTIPLY(false, MULTIPLICATION_SIGN,
			MULTIPLICATION, 1), DIVIDE(true, DIV_SIGN, DIV, 1),
	DIVIDE_FLOOR(true, FLOORDIV_SIGN, FLOORDIV, 1), MOD(true, MOD_SIGN,
			MODULUS, 1);
	public static final int MAX_PRECDENCE = 2;
	public final boolean requiresSecondArgumentNotZero;
	public final String display, noun;
	public final int precendence;
	Operation(boolean requiresSecondArgumentNotZero, String display,
			String noun, int precendence) {
		this.requiresSecondArgumentNotZero = requiresSecondArgumentNotZero;
		this.display = display;
		this.noun = noun;
		this.precendence = precendence;
	}
	public String toSourceCode() {
		return display;
	}
}