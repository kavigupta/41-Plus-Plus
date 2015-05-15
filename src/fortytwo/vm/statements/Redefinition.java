package fortytwo.vm.statements;

import fortytwo.compiler.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.expressions.Expression;

public class Redefinition implements Statement {
	public final VariableIdentifier name;
	public final Expression value;
	public Redefinition(VariableIdentifier name, Expression value) {
		this.name = name;
		this.value = value;
	}
	@Override
	public void execute(LocalEnvironment environment) {
		environment.vars.redefine(name, value);
	}
}