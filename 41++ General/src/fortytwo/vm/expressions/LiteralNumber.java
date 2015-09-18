package fortytwo.vm.expressions;

import java.math.BigDecimal;

import fortytwo.compiler.Context;
import fortytwo.language.SourceCode;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.PrimitiveTypeWOC;

public class LiteralNumber extends LiteralExpression {
	public final BigDecimal contents;
	public static LiteralNumber getInstance(BigDecimal contents,
			Context context) {
		return new LiteralNumber(contents, context);
	}
	public LiteralNumber(BigDecimal contents, Context context) {
		super(context);
		this.contents = contents;
	}
	@Override
	public PrimitiveType resolveType() {
		return new PrimitiveType(PrimitiveTypeWOC.NUMBER, Context.SYNTHETIC);
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(this);
	}
	@Override
	public String toString() {
		return toSourceCode();
	}
	@Override
	public boolean typedEquals(LiteralExpression other) {
		return this.contents.compareTo(((LiteralNumber) other).contents) == 0;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (contents == null ? 0 : contents.hashCode());
		return result;
	}
}
