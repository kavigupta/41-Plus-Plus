package fortytwo.compiler.language.expressions;

import java.math.BigDecimal;

import fortytwo.vm.environment.Environment;

public class LiteralNumber extends LiteralExpression {
	public final BigDecimal contents;
	public static LiteralNumber getInstance(BigDecimal contents) {
		return new LiteralNumber(contents);
	}
	public LiteralNumber(BigDecimal contents) {
		this.contents = contents;
	}
	@Override
	public String type(Environment environment) {
		return "number";
	}
}
