package fortytwo.compiler.language.statements;

import fortytwo.compiler.language.expressions.ParsedExpression;

public class ParsedWhileLoop implements ParsedStatement {
	public final ParsedExpression condition;
	public final ParsedStatement ParsedStatement;
	public ParsedWhileLoop(ParsedExpression condition,
			ParsedStatement ParsedStatement) {
		this.condition = condition;
		this.ParsedStatement = ParsedStatement;
	}
}
