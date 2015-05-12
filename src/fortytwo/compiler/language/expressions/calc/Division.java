package fortytwo.compiler.language.expressions.calc;

import fortytwo.compiler.language.expressions.Expression;

public class Division implements Expression {
	public final Expression first, second;
	public Division(Expression first, Expression second) {
		this.first = first;
		this.second = second;
	}
}
