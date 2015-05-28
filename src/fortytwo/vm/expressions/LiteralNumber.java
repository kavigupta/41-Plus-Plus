package fortytwo.vm.expressions;

import java.math.BigDecimal;

import fortytwo.compiler.Context;
import fortytwo.language.SourceCode;
import fortytwo.language.type.PrimitiveType;

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
		return PrimitiveType.NUMBER;
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
}
