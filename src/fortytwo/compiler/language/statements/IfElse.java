package fortytwo.compiler.language.statements;

import fortytwo.compiler.language.expressions.Expression;

public class IfElse implements Statement {
	public final Expression condition;
	public final Statement ifso, ifelse;
	public static IfElse getInstance(Expression condition, Statement ifso,
			Statement ifelse) {
		return new IfElse(condition, ifso, ifelse);
	}
	private IfElse(Expression condition, Statement ifso, Statement ifelse) {
		this.condition = condition;
		this.ifso = ifso;
		this.ifelse = ifelse;
	}
}
