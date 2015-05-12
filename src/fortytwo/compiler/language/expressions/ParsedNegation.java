package fortytwo.compiler.language.expressions;

import fortytwo.vm.environment.Environment;
import fortytwo.vm.expressions.Expression;

public class ParsedNegation implements ParsedExpression {
	public final ParsedExpression contents;
	private ParsedNegation(ParsedExpression contents) {
		this.contents = contents;
	}
	public static ParsedNegation getInstance(ParsedExpression contents) {
		if (!contents.equals("number")) throw new RuntimeException(/*
														 * LOWPRI-E
														 */);
		return new ParsedNegation(contents);
	}
	@Override
	public Expression contextualize(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
}
