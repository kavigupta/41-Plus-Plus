package fortytwo.compiler.language.expressions.calc;

import fortytwo.compiler.language.expressions.Expression;

public class Negation implements Expression {
	public final Expression contents;
	public Negation(Expression contents) {
		this.contents = contents;
	}
}
