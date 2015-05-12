package fortytwo.compiler.language.statements;

import fortytwo.compiler.language.expressions.ParsedExpression;

public class ParsedIfElse implements ParsedStatement {
	public final ParsedExpression condition;
	public final ParsedStatement ifso, ifelse;
	public static ParsedIfElse getInstance(ParsedExpression condition,
			ParsedStatement ifso, ParsedStatement ifelse) {
		return new ParsedIfElse(condition, ifso, ifelse);
	}
	private ParsedIfElse(ParsedExpression condition, ParsedStatement ifso,
			ParsedStatement ifelse) {
		this.condition = condition;
		this.ifso = ifso;
		this.ifelse = ifelse;
	}
}
