package fortytwo.compiler.parsed.expressions;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.statements.ParsedStatement;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.expressions.LiteralExpression;

public abstract class Expression extends ParsedStatement {
	private ConcreteType type = null;
	public final ConcreteType type(StaticEnvironment env) {
		if (type == null) type = resolveType1(env);
		return type;
	}
	@Override
	protected final boolean typeCheck1(StaticEnvironment environment) {
		type(environment);
		return true;
	}
	protected abstract ConcreteType resolveType1(StaticEnvironment env);
	public abstract LiteralExpression literalValue(LocalEnvironment environment);
	@Override
	public final void clean(LocalEnvironment environment) {
		// no op deliberately
	}
	@Override
	public abstract Context context();
	@Override
	public final boolean isSimple() {
		return true;
	}
}
