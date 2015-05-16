package fortytwo.vm.statements;

import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.VariableTypeRoster;
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
	@Override
	public boolean typeCheck(VariableTypeRoster typeRoster) {
		// TODO Auto-generated method stub
		return false;
	}
}
