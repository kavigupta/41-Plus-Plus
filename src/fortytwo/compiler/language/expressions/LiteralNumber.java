package fortytwo.compiler.language.expressions;

import java.math.BigDecimal;

import fortytwo.vm.environment.Environment;

public class LiteralNumber implements Expression {
	public final BigDecimal contents;
	public static Expression getInstance(BigDecimal contents) {
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
