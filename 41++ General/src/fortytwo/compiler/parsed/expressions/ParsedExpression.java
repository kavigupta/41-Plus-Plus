package fortytwo.compiler.parsed.expressions;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.statements.ParsedStatement;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.expressions.LiteralExpression;

public interface ParsedExpression extends ParsedStatement {
	public ConcreteType resolveType(StaticEnvironment env);
	public LiteralExpression literalValue(LocalEnvironment environment);
	@Override
	public default void clean(LocalEnvironment environment) {
		// no op deliberately
	}
	@Override
	public Context context();
	@Override
	public default boolean isSimple() {
		return true;
	}
}
