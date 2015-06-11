package fortytwo.vm.environment;

import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.expressions.LiteralExpression;

public class LocalEnvironment {
	public final GlobalEnvironment global;
	public final VariableRoster<LiteralExpression> vars;
	public LocalEnvironment(GlobalEnvironment global) {
		this.global = global;
		vars = new VariableRoster<>();
	}
	public LocalEnvironment reinitialize(GlobalEnvironment newEnvironment) {
		LocalEnvironment newlocal = new LocalEnvironment(newEnvironment);
		vars.forEach(x -> newlocal.vars.assign(x.key, x.value));
		return newlocal;
	}
	public LiteralExpression referenceTo(VariableIdentifier id) {
		LiteralExpression localE = vars.referenceTo(id);
		if (localE != null) return localE;
		LiteralExpression globalE = global.staticEnv.referenceTo(id);
		return globalE;
	}
	public ConcreteType typeOf(VariableIdentifier name) {
		ConcreteType type = vars.typeOf(name);
		if (type != null) return type;
		return global.staticEnv.typeOf(name);
	}
	public StaticEnvironment staticEnvironment() {
		StaticEnvironment env = StaticEnvironment.getChild(global.staticEnv);
		vars.forEach(variable -> env.addType(variable.getKey(), variable
				.getValue().resolveType()));
		return env;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((global == null) ? 0 : global.hashCode());
		result = prime * result + ((vars == null) ? 0 : vars.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		LocalEnvironment other = (LocalEnvironment) obj;
		if (global == null) {
			if (other.global != null) return false;
		} else if (!global.equals(other.global)) return false;
		if (vars == null) {
			if (other.vars != null) return false;
		} else if (!vars.equals(other.vars)) return false;
		return true;
	}
}
