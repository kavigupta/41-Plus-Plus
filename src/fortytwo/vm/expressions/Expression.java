package fortytwo.vm.expressions;

import fortytwo.compiler.Context;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.statements.Statement;

public interface Expression extends Statement {
	public LiteralExpression literalValue(LocalEnvironment environment);
	public ConcreteType resolveType();
	@Override
	public default void clean(LocalEnvironment environment) {
		// there really should be nothing to clean here
	}
	public Context context();
	@Override
	public String toSourceCode();
}
