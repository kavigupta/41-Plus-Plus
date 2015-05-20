package fortytwo.language;

public enum Operation {
	ADD(false, "+"), SUBTRACT(false, "-"), MULTIPLY(false, "*"), DIVIDE(true,
			"/"), DIVIDE_FLOOR(true, "//"), MOD(true, "%");
	public final boolean requiresSecondArgumentNotZero;
	public final String display;
	Operation(boolean requiresSecondArgumentNotZero, String display) {
		this.requiresSecondArgumentNotZero = requiresSecondArgumentNotZero;
		this.display = display;
	}
	public String toSourceCode() {
		return display;
	}
}