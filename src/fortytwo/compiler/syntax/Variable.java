package fortytwo.compiler.syntax;


public class Variable implements SyntacticElement {
	public final String contents;
	public static SyntacticElement getInstance(String contents) {
		return new Variable(contents);
	}
	public Variable(String contents) {
		this.contents = contents;
	}
	@Override
	public SEType getType() {
		return SEType.VARIABLE;
	}
}
