package fortytwo.vm.expressions;

import java.math.BigDecimal;

import fortytwo.language.SourceCode;
import fortytwo.language.type.PrimitiveType;

public class LiteralNumber extends LiteralExpression {
	public final BigDecimal contents;
	public static LiteralNumber getInstance(BigDecimal contents) {
		return new LiteralNumber(contents);
	}
	public LiteralNumber(BigDecimal contents) {
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
}
