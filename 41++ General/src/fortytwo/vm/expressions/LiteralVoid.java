package fortytwo.vm.expressions;

import fortytwo.compiler.Context;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.PrimitiveTypeWOC;

public class LiteralVoid extends LiteralExpression {
	public static final LiteralVoid INSTANCE = new LiteralVoid(
			Context.SYNTHETIC);
	private LiteralVoid(Context context) {
		super(context);
	}
	@Override
	public String toSourceCode() {
		return "";
	}
	@Override
	public ConcreteType resolveType() {
		return new PrimitiveType(PrimitiveTypeWOC.VOID, context());
	}
	@Override
	public boolean typedEquals(LiteralExpression other) {
		return other instanceof LiteralVoid;
	}
}
