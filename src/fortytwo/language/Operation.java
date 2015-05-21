package fortytwo.language;

import static fortytwo.language.Resources.*;

public enum Operation {
	ADD(false, ADDITION_SIGN, 2), SUBTRACT(false, SUBTRACTION_SIGN, 2),
	MULTIPLY(false, MULTIPLICATION_SIGN, 1), DIVIDE(true, DIV_SIGN, 1),
	DIVIDE_FLOOR(true, FLOORDIV_SIGN, 1), MOD(true, MOD_SIGN, 1);
	public static final int MAX_PRECDENCE = 2;
	public final boolean requiresSecondArgumentNotZero;
	public final String display;
	public final int precendence;
	Operation(boolean requiresSecondArgumentNotZero, String display,
			int precendence) {
		this.requiresSecondArgumentNotZero = requiresSecondArgumentNotZero;
		this.display = display;
		this.precendence = precendence;
	}
	public String toSourceCode() {
		return display;
	}
}