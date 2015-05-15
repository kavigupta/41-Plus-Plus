package fortytwo.compiler.language.sentences;

public interface Sentence {
	public static enum SentenceType {
		DEFINITION, ASSIGNMENT, DECLARATION_STRUCT, DECLARATION_FUNCT,
		FUNCTION_CALL, FUNCTION_RETURN, COMPOUND, PURE_EXPRESSION,
		CONTROL_FLOW, IMPURE_EXPRESSION
	}
	public SentenceType type();
}
