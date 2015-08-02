package fortytwo.language.type;

import java.math.BigDecimal;

import fortytwo.compiler.Context;
import fortytwo.compiler.LiteralToken;
import fortytwo.vm.expressions.LiteralBool;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralNumber;
import fortytwo.vm.expressions.LiteralString;

public enum PrimitiveTypeWOC {
	NUMBER(LiteralNumber.getInstance(BigDecimal.ZERO, Context.SYNTHETIC)),
	STRING(new LiteralString(LiteralToken.SYNTHETIC_EMPTY_STRING)), BOOL(LiteralBool
			.getInstance(false, Context.SYNTHETIC)), TYPE(null), VOID(null);
	public final LiteralExpression def;
	private PrimitiveTypeWOC(LiteralExpression def) {
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
