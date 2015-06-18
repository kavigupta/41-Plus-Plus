package fortytwo.vm.expressions;

import fortytwo.compiler.Context;
import fortytwo.compiler.LiteralToken;
import fortytwo.language.SourceCode;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.PrimitiveTypeWithoutContext;

public class LiteralString extends LiteralExpression {
	public final LiteralToken contents;
	public static LiteralString getInstance(LiteralToken contents) {
		return new LiteralString(contents);
	}
	public LiteralString(LiteralToken contents) {
		super(contents.context);
		this.contents = contents;
	}
	@Override
	public PrimitiveType resolveType() {
		return new PrimitiveType(PrimitiveTypeWithoutContext.STRING,
				Context.SYNTHETIC);
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(this);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((contents == null) ? 0 : contents.hashCode());
		return result;
	}
	@Override
	public boolean typedEquals(LiteralExpression obj) {
		LiteralString other = (LiteralString) obj;
		if (contents == null) {
			if (other.contents != null) return false;
		} else if (!contents.equals(other.contents)) return false;
		return true;
	}
}
