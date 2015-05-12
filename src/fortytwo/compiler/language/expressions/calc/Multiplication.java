package fortytwo.compiler.language.expressions.calc;

import fortytwo.compiler.language.expressions.Expression;

public class Multiplication implements Expression {
	public final Expression first, second;
	public Multiplication(Expression first, Expression second) {
		this.first = first;
		this.second = second;
	}
}
