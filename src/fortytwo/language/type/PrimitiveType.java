package fortytwo.language.type;

import java.math.BigDecimal;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token;
import fortytwo.vm.expressions.LiteralBool;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralNumber;
import fortytwo.vm.expressions.LiteralString;

public enum PrimitiveType implements ConcreteType {
	NUMBER(LiteralNumber.getInstance(BigDecimal.ZERO)), STRING(
			new LiteralString(new Token("", Context.minimal()))), BOOL(
			LiteralBool.FALSE), TYPE(null), VOID(null);
	public final LiteralExpression def;
	private PrimitiveType(LiteralExpression def) {
		this.def = def;
	}
	public final String typeID() {
		return name().toLowerCase();
	}
	@Override
	public String toSourceCode() {
		return typeID();
	}
	@Override
	public LiteralExpression defaultValue() {
		return def;
	}
}
