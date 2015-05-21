package fortytwo.compiler.parsed.sentences;

import fortytwo.language.ParsedConstruct;

public interface Sentence extends ParsedConstruct {
	public static enum SentenceType {
		DEFINITION, ASSIGNMENT, DECLARATION_STRUCT, DECLARATION_FUNCT,
		FUNCTION_CALL, FUNCTION_RETURN, COMPOUND, PURE_EXPRESSION,
		CONTROL_FLOW, IMPURE_EXPRESSION
	}
	public SentenceType type();
	@Override
	public String toSourceCode();
	public boolean isSimple();
}
