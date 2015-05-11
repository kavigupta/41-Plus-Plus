package fortytwo.compiler.syntax;

public interface SyntacticElement {
	public static enum SEType {
		LITERAL_NUM, LITERAL_STRING, LITERAL_BOOL, VARIABLE
	}
	public SEType getType();
}
