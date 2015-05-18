package fortytwo.vm.expressions;

import fortytwo.language.type.PrimitiveType;

public class LiteralBool extends LiteralExpression {
	public static final LiteralBool TRUE = new LiteralBool(true);
	public static final LiteralBool FALSE = new LiteralBool(false);
	public final boolean contents;
	private LiteralBool(boolean contents) {
		this.contents = contents;
	}
	@Override
	public PrimitiveType resolveType() {
		return PrimitiveType.BOOL;
	}
}
