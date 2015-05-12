package fortytwo.compiler.language.statements;

import fortytwo.compiler.language.expressions.Expression;

public class WhileLoop implements Statement {
	public final Expression condition;
	public final Statement statement;
	public WhileLoop(Expression condition, Statement statement) {
		this.condition = condition;
		this.statement = statement;
	}
}
