package fortytwo.compiler.language.expressions;

import fortytwo.compiler.language.statements.ParsedStatement;
import fortytwo.vm.environment.Environment;
import fortytwo.vm.expressions.Expression;

public interface ParsedExpression extends ParsedStatement {
	public Expression contextualize(Environment env);
}
