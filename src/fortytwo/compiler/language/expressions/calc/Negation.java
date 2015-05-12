package fortytwo.compiler.language.expressions.calc;

import fortytwo.compiler.language.expressions.Expression;
import fortytwo.compiler.language.expressions.LiteralExpression;
import fortytwo.compiler.language.expressions.LiteralNumber;
import fortytwo.vm.environment.Environment;

public class Negation implements Expression {
	public final Expression contents;
	private Negation(Expression contents) {
		this.contents = contents;
	}
	public static Negation getInstance(Expression contents) {
		if (!contents.equals("number")) throw new RuntimeException(/*
														 * LOWPRI-E
														 */);
		return new Negation(contents);
	}
	@Override
	public String type(Environment environment) {
		return "number";
	}
	@Override
	public LiteralExpression evaluate(Environment environment) {
		return LiteralNumber.getInstance(((LiteralNumber) contents
				.evaluate(environment)).contents.negate());
	}
}
