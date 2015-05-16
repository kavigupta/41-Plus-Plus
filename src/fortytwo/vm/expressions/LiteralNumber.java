package fortytwo.vm.expressions;

import java.math.BigDecimal;

import fortytwo.compiler.language.identifier.TypeIdentifier;

public class LiteralNumber extends LiteralExpression {
	public final BigDecimal contents;
	public static LiteralNumber getInstance(BigDecimal contents) {
		return new LiteralNumber(contents);
	}
	public LiteralNumber(BigDecimal contents) {
		this.contents = contents;
	}
	@Override
	public TypeIdentifier resolveType() {
		return TypeIdentifier.getInstance("number");
	}
}
