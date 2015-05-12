package fortytwo.compiler.language.expressions.calc;

import fortytwo.compiler.language.expressions.Expression;

public class FloorDivision implements Expression {
	public final Expression first, second;
	public FloorDivision(Expression first, Expression second) {
		this.first = first;
		this.second = second;
	}
}
