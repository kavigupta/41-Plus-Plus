package fortytwo.vm.expressions;

import java.math.BigDecimal;

import fortytwo.compiler.language.identifier.TypeIdentifier;
import fortytwo.vm.environment.VariableTypeRoster;

public class LiteralNumber extends LiteralExpression {
	public final BigDecimal contents;
	public static LiteralNumber getInstance(BigDecimal contents) {
		return new LiteralNumber(contents);
	}
	public LiteralNumber(BigDecimal contents) {
		this.contents = contents;
	}
	@Override
	public TypeIdentifier resolveType(VariableTypeRoster typeRoster) {
		return TypeIdentifier.getInstance("number");
	}
}
