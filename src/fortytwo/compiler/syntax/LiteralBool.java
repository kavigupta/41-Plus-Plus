package fortytwo.compiler.syntax;

public class LiteralBool implements SyntacticElement {
	public static final LiteralBool TRUE = new LiteralBool(true);
	public static final LiteralBool FALSE = new LiteralBool(false);
	public final boolean contents;
	private LiteralBool(boolean contents) {
		this.contents = contents;
	}
	@Override
	public SEType getType() {
		return SEType.LITERAL_BOOL;
	}
}
