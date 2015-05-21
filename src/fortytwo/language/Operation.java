package fortytwo.language;

public enum Operation {
	ADD(false, "+", 2), SUBTRACT(false, "-", 2), MULTIPLY(false, "*", 1),
	DIVIDE(true, "/", 1), DIVIDE_FLOOR(true, "//", 1), MOD(true, "%", 1);
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