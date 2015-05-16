package fortytwo.vm.statements;

import fortytwo.compiler.language.identifier.TypeIdentifier;
import fortytwo.compiler.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.VariableRoster;
import fortytwo.vm.environment.VariableTypeRoster;
import fortytwo.vm.expressions.LiteralObject;

public class Definition implements Statement {
	public final TypeIdentifier type;
	public final VariableIdentifier name;
	public final VariableRoster fields;
	public Definition(TypeIdentifier type, VariableIdentifier name,
			VariableRoster fields) {
		this.type = type;
		this.name = name;
		this.fields = fields;
	}
	@Override
	public void execute(LocalEnvironment environment) {
		environment.vars.assign(name, new LiteralObject(
				environment.global.structs.referenceTo(type), fields));
	}
	@Override
	public boolean typeCheck(VariableTypeRoster typeRoster) {
		// everything had to be typechecked on input, I think.
		// This function needs TODO to check if the fields exist and are a
		// complete definition of the structure
		return true;
	}
}
