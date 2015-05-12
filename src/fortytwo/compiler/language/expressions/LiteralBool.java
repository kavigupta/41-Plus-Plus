package fortytwo.compiler.language.expressions;

import fortytwo.vm.environment.Environment;

public class LiteralBool implements Expression {
	public static final LiteralBool TRUE = new LiteralBool(true);
	public static final LiteralBool FALSE = new LiteralBool(false);
	public final boolean contents;
	private LiteralBool(boolean contents) {
		this.contents = contents;
	}
	@Override
	public String type(Environment environment) {
		return "bool";
	}
}
