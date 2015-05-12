package fortytwo.compiler.language.statements;

import fortytwo.compiler.language.expressions.ParsedExpression;

public class ParsedAssignment implements ParsedStatement {
	public final String name, field;
	public final ParsedExpression parseExpression;
	public ParsedAssignment(String name, String field,
			ParsedExpression parseExpression) {
		this.name = name;
		this.field = field;
		this.parseExpression = parseExpression;
	}
}
