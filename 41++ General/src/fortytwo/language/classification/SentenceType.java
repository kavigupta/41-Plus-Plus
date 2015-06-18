package fortytwo.language.classification;

import static fortytwo.language.classification.SentenceKind.DECLARATION;
import static fortytwo.language.classification.SentenceKind.STATEMENT;

public enum SentenceType {
	DEFINITION("variable definition", STATEMENT), ASSIGNMENT(
			"variable assignment", STATEMENT), DECLARATION_STRUCT(
			"type definition", DECLARATION), DECLARATION_FUNCT(
			"function definition", DECLARATION), FUNCTION_CALL(
			"function call", STATEMENT), FUNCTION_OUTPUT(
			"function output statement", DECLARATION), COMPOUND(
			"compound statement", STATEMENT), PURE_EXPRESSION(
			"pure expression", STATEMENT), CONTROL_FLOW(
			"control flow statement", DECLARATION), IMPURE_EXPRESSION(
			"impure expression", STATEMENT);
	public final String description;
	public final SentenceKind kind;
	private SentenceType(String description, SentenceKind kind) {
		this.description = description;
		this.kind = kind;
	}
	public String description() {
		return description;
	}
}