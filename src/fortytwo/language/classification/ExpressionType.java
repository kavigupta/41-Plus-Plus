package fortytwo.language.classification;

public enum ExpressionType {
	LITERAL_NUMBER("literal number"), LITERAL_BOOL("literal bool"),
	LITERAL_STRING("literal string"), VARIABLE("variable"), ARITHMETIC(
			"arithmetic expression"), FUNCTION_CALL("function call");
	private ExpressionType(String description) {
		this.description = description;
	}
	public final String description;
	public String description() {
		return description;
	}
}