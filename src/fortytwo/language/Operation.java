package fortytwo.language;

public enum Operation {
	ADD(false), SUBTRACT(false), MULTIPLY(false), DIVIDE(true), DIVIDE_FLOOR(
			true), MOD(true);
	public final boolean requiresSecondArgumentNotZero;
	Operation(boolean requiresSecondArgumentNotZero) {
		this.requiresSecondArgumentNotZero = requiresSecondArgumentNotZero;
	}
}