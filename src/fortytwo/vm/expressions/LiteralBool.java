package fortytwo.vm.expressions;

import fortytwo.compiler.language.identifier.TypeIdentifier;

public class LiteralBool extends LiteralExpression {
	public static final LiteralBool TRUE = new LiteralBool(true);
	public static final LiteralBool FALSE = new LiteralBool(false);
	public final boolean contents;
	private LiteralBool(boolean contents) {
		this.contents = contents;
	}
	@Override
	public TypeIdentifier resolveType() {
		return TypeIdentifier.getInstance("bool");
	}
}
