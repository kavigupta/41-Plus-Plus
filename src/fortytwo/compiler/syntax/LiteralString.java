package fortytwo.compiler.syntax;

public class LiteralString implements SyntacticElement {
	public final String contents;
	public static SyntacticElement getInstance(String contents) {
		return new LiteralString(contents);
	}
	public LiteralString(String contents) {
		this.contents = contents;
	}
	@Override
	public SEType getType() {
		return SEType.LITERAL_STRING;
	}
}
