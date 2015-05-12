package fortytwo.compiler.language.expressions;

import fortytwo.vm.environment.Environment;
import fortytwo.vm.expressions.Expression;

public abstract class LiteralExpression implements ParsedExpression, Expression {
	@Override
	public Expression contextualize(Environment env) {
		return this;
	}
}
