package fortytwo.compiler.language.statements;

import fortytwo.compiler.language.expressions.ParsedExpression;

public class Assignment implements ParsedStatement {
	public final String name, field;
	public final ParsedExpression parseExpression;
	public Assignment(String name, String field,
			ParsedExpression parseExpression) {
		this.name = name;
		this.field = field;
		this.parseExpression = parseExpression;
	}
}
