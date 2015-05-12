package fortytwo.vm.expressions;

import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.vm.environment.Environment;

public abstract class LiteralExpression implements ParsedExpression, Expression {
	@Override
	public Expression contextualize(Environment env) {
		return this;
	}
}
