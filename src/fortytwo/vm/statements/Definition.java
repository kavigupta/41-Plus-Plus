package fortytwo.vm.statements;

import fortytwo.language.field.Field;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.VariableRoster;
import fortytwo.vm.environment.VariableTypeRoster;
import fortytwo.vm.expressions.LiteralObject;

public class Definition implements Statement {
	public final Field name;
	public final VariableRoster fields;
	public Definition(Field name, VariableRoster fields) {
		this.name = name;
		this.fields = fields;
	}
	@Override
	public void execute(LocalEnvironment environment) {
		environment.vars.assign(name.name, new LiteralObject(
				environment.global.structs.referenceTo(name.type), fields));
	}
	@Override
	public boolean typeCheck(VariableTypeRoster typeRoster) {
		// everything had to be typechecked on input, I think. This function
		// needs TODO to check if the fields exist and are a complete
		// definition of the structure
		return true;
	}
}
