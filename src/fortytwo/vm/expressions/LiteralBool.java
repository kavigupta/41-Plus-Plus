package fortytwo.vm.expressions;

import fortytwo.language.SourceCode;
import fortytwo.language.type.PrimitiveType;

public class LiteralBool extends LiteralExpression {
	public static final LiteralBool TRUE = new LiteralBool(true);
	public static final LiteralBool FALSE = new LiteralBool(false);
	public final boolean contents;
	public static LiteralBool getInstance(boolean contents) {
		return contents ? TRUE : FALSE;
	}
	private LiteralBool(boolean contents) {
		this.contents = contents;
	}
	@Override
	public PrimitiveType resolveType() {
		return PrimitiveType.BOOL;
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(this);
	}
}
