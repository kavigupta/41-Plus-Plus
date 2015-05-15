package fortytwo.compiler.language.expressions;

import fortytwo.vm.environment.LocalEnvironment;
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
	public Expression contextualize(LocalEnvironment env) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public SentenceType type() {
		// TODO Auto-generated method stub
		return null;
	}
}
