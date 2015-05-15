package fortytwo.compiler.language.expressions;

import fortytwo.compiler.language.statements.ParsedStatement;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.expressions.Expression;

public interface ParsedExpression extends ParsedStatement {
	@Override
	public Expression contextualize(LocalEnvironment env);
}
