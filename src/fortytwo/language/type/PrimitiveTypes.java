package fortytwo.language.type;

import java.math.BigDecimal;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token;
import fortytwo.vm.expressions.LiteralBool;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralNumber;
import fortytwo.vm.expressions.LiteralString;

public enum PrimitiveTypes {
	NUMBER(LiteralNumber.getInstance(BigDecimal.ZERO, Context.synthetic())),
	STRING(new LiteralString(new Token("", Context.minimal()))), BOOL(
			LiteralBool.getInstance(false, Context.synthetic())),
	TYPE(null), VOID(null);
	public final LiteralExpression def;
	private PrimitiveTypes(LiteralExpression def) {
		this.def = def;
	}
	public final String typeID() {
		return name().toLowerCase();
	}
	public String toSourceCode() {
		return typeID();
	}
	public LiteralExpression defaultValue() {
		return def;
	}
}
