package fortytwo.vm.environment;

import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.expressions.LiteralExpression;

public class OrderedEnvironment {
	public final UnorderedEnvironment global;
	public final VariableRoster<LiteralExpression> vars;
	public OrderedEnvironment(UnorderedEnvironment global) {
		this.global = global;
		vars = new VariableRoster<>();
	}
	public OrderedEnvironment reinitialize(UnorderedEnvironment newEnvironment) {
		final OrderedEnvironment newlocal = new OrderedEnvironment(newEnvironment);
		vars.pairs.forEach((k, v) -> newlocal.vars.assign(k, v));
		return newlocal;
	}
	public LiteralExpression referenceTo(VariableIdentifier id) {
		final LiteralExpression localE = vars.referenceTo(id);
		if (localE != null) return localE;
		final LiteralExpression globalE = global.staticEnv.referenceTo(id);
		return globalE;
	}
	public ConcreteType typeOf(VariableIdentifier name) {
		final ConcreteType type = vars.referenceTo(name).resolveType();
		if (type != null) return type;
		return global.staticEnv.typeOf(name);
	}
	public TypeEnvironment staticEnvironment() {
		final TypeEnvironment env = TypeEnvironment
				.getChild(global.staticEnv);
		vars.pairs
				.forEach((name, expr) -> env.addType(name, expr.resolveType()));
		return env;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (global == null ? 0 : global.hashCode());
		result = prime * result + (vars == null ? 0 : vars.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final OrderedEnvironment other = (OrderedEnvironment) obj;
		if (global == null) {
			if (other.global != null) return false;
		} else if (!global.equals(other.global)) return false;
		if (vars == null) {
			if (other.vars != null) return false;
		} else if (!vars.equals(other.vars)) return false;
		return true;
	}
}
