package fortytwo.vm.expressions;

import fortytwo.language.type.PrimitiveType;

public class LiteralString extends LiteralExpression {
	public final String contents;
	public static LiteralString getInstance(String contents) {
		return new LiteralString(contents);
	}
	public LiteralString(String contents) {
		this.contents = contents;
	}
	@Override
	public PrimitiveType resolveType() {
		return PrimitiveType.STRING;
	}
}
