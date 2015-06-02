package fortytwo.vm.expressions;

import fortytwo.compiler.Context;
import fortytwo.language.SourceCode;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.PrimitiveTypes;

public class LiteralBool extends LiteralExpression {
	public final boolean contents;
	public static LiteralBool getInstance(boolean contents, Context context) {
		return new LiteralBool(contents, context);
	}
	private LiteralBool(boolean contents, Context context) {
		super(context);
		this.contents = contents;
	}
	@Override
	public PrimitiveType resolveType() {
		return new PrimitiveType(PrimitiveTypes.BOOL, Context.synthetic());
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(this);
	}
	@Override
	public boolean typedEquals(LiteralExpression other) {
		return this.contents == ((LiteralBool) other).contents;
	}
}
