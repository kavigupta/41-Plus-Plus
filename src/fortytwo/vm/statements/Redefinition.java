package fortytwo.vm.statements;

import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.errors.TypingErrors;
import fortytwo.vm.expressions.Expression;

public class Redefinition implements Statement {
	public final VariableIdentifier name;
	public final Expression value;
	// LOWPRI quick/dirty solution. Fix.
	private StaticEnvironment env;
	public Redefinition(VariableIdentifier name, Expression value,
			StaticEnvironment env) {
		this.name = name;
		this.value = value;
		this.env = env;
	}
	@Override
	public void execute(LocalEnvironment environment) {
		environment.vars.redefine(name, value.literalValue(environment));
	}
	@Override
	public void clean(LocalEnvironment environment) {
		// no variables created
	}
	@Override
	public boolean typeCheck() {
		if (env.typeOf(name).equals(value.resolveType())) return true;
		TypingErrors.redefinitionTypeMismatch(name, value);
		// should never get here
		return false;
	}
}
