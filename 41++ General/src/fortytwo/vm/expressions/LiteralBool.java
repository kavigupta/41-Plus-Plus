package fortytwo.vm.expressions;

import fortytwo.compiler.Context;
import fortytwo.language.SourceCode;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.PrimitiveTypeWOC;

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
		return new PrimitiveType(PrimitiveTypeWOC.BOOL, Context.SYNTHETIC);
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(this);
	}
	@Override
	public boolean typedEquals(LiteralExpression other) {
		return this.contents == ((LiteralBool) other).contents;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (contents ? 1231 : 1237);
		return result;
	}
}
