package fortytwo.compiler.language.expressions;

public class LiteralString implements Expression {
	public final String contents;
	public static LiteralString getInstance(String contents) {
		return new LiteralString(contents);
	}
	public LiteralString(String contents) {
		this.contents = contents;
	}
}
