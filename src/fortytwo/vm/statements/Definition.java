package fortytwo.vm.statements;

import fortytwo.language.field.Field;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StructureRoster;
import fortytwo.vm.environment.VariableRoster;
import fortytwo.vm.environment.VariableTypeRoster;

public class Definition implements Statement {
	public final Field toCreate;
	public final VariableRoster fields;
	public Definition(Field toCreate, VariableRoster fields) {
		this.toCreate = toCreate;
		this.fields = fields;
	}
	@Override
	public void execute(LocalEnvironment environment) {
		environment.vars.assign(toCreate.name,
				environment.global.structs.instance(toCreate.type, fields));
	}
	@Override
	public boolean typeCheck(VariableTypeRoster typeRoster,
			StructureRoster roster) {
		// everything had to be typechecked on input,
		// LocalEnvironment.instance checks if the fields can be coerced
		return roster.typeCheckConstructor(toCreate.type, fields);
	}
}
