package fortytwo.compiler.language.expressions.calc;

import fortytwo.compiler.language.expressions.Expression;

public class Modulus implements Expression {
	public final Expression first, second;
	public Modulus(Expression first, Expression second) {
		this.first = first;
		this.second = second;
	}
}
