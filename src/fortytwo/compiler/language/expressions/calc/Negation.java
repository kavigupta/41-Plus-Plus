package fortytwo.compiler.language.expressions.calc;

import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.vm.environment.Environment;
import fortytwo.vm.expressions.Expression;

public class Negation implements ParsedExpression {
	public final ParsedExpression contents;
	private Negation(ParsedExpression contents) {
		this.contents = contents;
	}
	public static Negation getInstance(ParsedExpression contents) {
		if (!contents.equals("number")) throw new RuntimeException(/*
														 * LOWPRI-E
														 */);
		return new Negation(contents);
	}
	@Override
	public Expression contextualize(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
}
