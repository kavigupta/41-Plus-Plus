package fortytwo.compiler.parsed.expressions;

import fortytwo.compiler.parsed.statements.ParsedStatement;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.expressions.Expression;

public interface ParsedExpression extends ParsedStatement {
	@Override
	public Expression contextualize(LocalEnvironment env);
}
