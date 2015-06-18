package fortytwo.compiler.parsed.expressions;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.statements.ParsedStatement;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.expressions.Expression;

public interface ParsedExpression extends ParsedStatement {
	@Override
	public Expression contextualize(StaticEnvironment env);
	@Override
	public Context context();
	@Override
	public default boolean isSimple() {
		return true;
	}
}
