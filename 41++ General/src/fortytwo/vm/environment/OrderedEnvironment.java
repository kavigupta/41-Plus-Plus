package fortytwo.vm.environment;

import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.environment.type.NonTopTypeEnvironment;
import fortytwo.vm.expressions.LiteralExpression;

public class OrderedEnvironment {
	public final UnorderedEnvironment container;
	public final VariableRoster<LiteralExpression> vars;
	public OrderedEnvironment(UnorderedEnvironment global) {
		this.container = global;
		vars = new VariableRoster<>();
	}
	public OrderedEnvironment reinitialize(
			UnorderedEnvironment newEnvironment) {
		final OrderedEnvironment newlocal = new OrderedEnvironment(
				newEnvironment);
		vars.pairs.forEach((k, v) -> newlocal.vars.assign(k, v));
		return newlocal;
	}
	public LiteralExpression referenceTo(VariableIdentifier id) {
		final LiteralExpression localE = vars.referenceTo(id);
		if (localE != null) return localE;
		final LiteralExpression globalE = container.referenceTo(id);
		return globalE;
	}
	public ConcreteType typeOf(VariableIdentifier name) {
		final ConcreteType type = vars.referenceTo(name).resolveType();
		if (type != null) return type;
		return container.typeEnv.typeOf(name);
	}
	public NonTopTypeEnvironment staticEnvironment() {
		final NonTopTypeEnvironment env = NonTopTypeEnvironment.getChild(container.typeEnv);
		vars.pairs
				.forEach((name, expr) -> env.addType(name, expr.resolveType()));
		return env;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (container == null ? 0 : container.hashCode());
		result = prime * result + (vars == null ? 0 : vars.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final OrderedEnvironment other = (OrderedEnvironment) obj;
		if (container == null) {
			if (other.container != null) return false;
		} else if (!container.equals(other.container)) return false;
		if (vars == null) {
			if (other.vars != null) return false;
		} else if (!vars.equals(other.vars)) return false;
		return true;
	}
}
