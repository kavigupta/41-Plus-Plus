package fortytwo.language.classification;

public enum SentenceType {
	DEFINITION("variable definition"), ASSIGNMENT("variable assignment"),
	DECLARATION_STRUCT("type definition"), DECLARATION_FUNCT(
			"function definition"), FUNCTION_CALL("function call"),
	FUNCTION_RETURN("function output statement"), COMPOUND(
			"compound statement"), PURE_EXPRESSION("pure expression"),
	CONTROL_FLOW("control flow statement"), IMPURE_EXPRESSION(
			"impure expression");
	public final String description;
	private SentenceType(String description) {
		this.description = description;
	}
	public String description() {
		return description;
	}
}