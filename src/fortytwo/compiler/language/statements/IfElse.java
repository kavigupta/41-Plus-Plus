package fortytwo.compiler.language.statements;

import fortytwo.compiler.language.expressions.ParsedExpression;

public class IfElse implements ParsedStatement {
	public final ParsedExpression condition;
	public final ParsedStatement ifso, ifelse;
	public static IfElse getInstance(ParsedExpression condition,
			ParsedStatement ifso, ParsedStatement ifelse) {
		return new IfElse(condition, ifso, ifelse);
	}
	private IfElse(ParsedExpression condition, ParsedStatement ifso,
			ParsedStatement ifelse) {
		this.condition = condition;
		this.ifso = ifso;
		this.ifelse = ifelse;
	}
}
