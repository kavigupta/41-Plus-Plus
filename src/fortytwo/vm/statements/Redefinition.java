package fortytwo.vm.statements;

import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.VariableTypeRoster;
import fortytwo.vm.expressions.Expression;

public class Redefinition implements Statement {
	public final VariableIdentifier name;
	public final Expression value;
	// LOWPRI quick/dirty solution. Fix.
	private VariableTypeRoster types;
	public Redefinition(VariableIdentifier name, Expression value,
			VariableTypeRoster types) {
		this.name = name;
		this.value = value;
		this.types = types;
	}
	@Override
	public void execute(LocalEnvironment environment) {
		environment.vars.redefine(name, value.literalValue(environment));
	}
	@Override
	public boolean typeCheck() {
		if (types.typeOf(name).equals(value.resolveType())) return true;
		throw new RuntimeException(/* LOWPRI-E */);
	}
}
