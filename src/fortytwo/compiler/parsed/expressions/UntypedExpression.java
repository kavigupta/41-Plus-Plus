package fortytwo.compiler.parsed.expressions;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token;
import fortytwo.compiler.parsed.statements.ParsedStatement;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.expressions.Expression;

public interface UntypedExpression extends ParsedStatement {
	@Override
	public Expression contextualize(StaticEnvironment env);
	public Context context();
	@Override
	public default boolean isSimple() {
		return true;
	}
	public default Token toToken() {
		return new Token(toSourceCode(), context());
	}
}
