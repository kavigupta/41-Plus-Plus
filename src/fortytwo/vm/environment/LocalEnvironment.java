package fortytwo.vm.environment;

import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.expressions.LiteralExpression;

public class LocalEnvironment {
	public final GlobalEnvironment global;
	public final LiteralVariableRoster vars;
	public LocalEnvironment(GlobalEnvironment global) {
		this.global = global;
		vars = new LiteralVariableRoster();
	}
	public LiteralExpression referenceTo(VariableIdentifier id) {
		LiteralExpression localE = vars.referenceTo(id);
		if (localE != null) return localE;
		LiteralExpression globalE = global.staticEnv.globalVariables
				.referenceTo(id);
		return globalE;
	}
	public ConcreteType typeOf(VariableIdentifier name) {
		ConcreteType type = vars.typeOf(name);
		if (type != null) return type;
		type = global.staticEnv.globalVariables.typeOf(name);
		if (type != null) return type;
		throw new RuntimeException(/* LOWPRI-E */);
	}
	public StaticEnvironment staticEnvironment() {
		StaticEnvironment env = global.staticEnv.clone();
		vars.pairs.entrySet().forEach(
				variable -> env.types.add(variable.getKey(), variable
						.getValue().resolveType()));
		// TODO this may require a slight refactor to account for the fact
		// that local types must be utilized .
		return null;
	}
}
