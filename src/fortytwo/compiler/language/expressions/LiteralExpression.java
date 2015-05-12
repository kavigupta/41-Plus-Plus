package fortytwo.compiler.language.expressions;

import fortytwo.vm.environment.Environment;

public abstract class LiteralExpression implements Expression {
	@Override
	public abstract String type(Environment environment);
	@Override
	public LiteralExpression evaluate(Environment environment) {
		return this;
	}
}
