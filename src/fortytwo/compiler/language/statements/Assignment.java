package fortytwo.compiler.language.statements;

import fortytwo.compiler.language.expressions.Expression;

public class Assignment implements Statement {
	public final String name, field;
	public final Expression parseExpression;
	public Assignment(String name, String field, Expression parseExpression) {
		this.name = name;
		this.field = field;
		this.parseExpression = parseExpression;
	}
}
