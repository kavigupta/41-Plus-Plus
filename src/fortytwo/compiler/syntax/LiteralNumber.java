package fortytwo.compiler.syntax;

import java.math.BigDecimal;

public class LiteralNumber implements SyntacticElement {
	public final BigDecimal contents;
	public static SyntacticElement getInstance(BigDecimal contents) {
		return new LiteralNumber(contents);
	}
	public LiteralNumber(BigDecimal contents) {
		this.contents = contents;
	}
	@Override
	public SEType getType() {
		return SEType.LITERAL_NUM;
	}
}
