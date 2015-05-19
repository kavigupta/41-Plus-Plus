package fortytwo.vm.statements;

import fortytwo.language.field.Field;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StructureRoster;
import fortytwo.vm.environment.VariableRoster;

public class Definition implements Statement {
	public final Field toCreate;
	public final VariableRoster fields;
	// LOWPRI quick/dirty solution. Fix.
	private StructureRoster structs;
	public Definition(Field toCreate, VariableRoster fields,
			StructureRoster structs) {
		this.toCreate = toCreate;
		this.fields = fields;
		this.structs = structs;
	}
	@Override
	public void execute(LocalEnvironment environment) {
		environment.vars.assign(toCreate.name,
				environment.global.staticEnv.structs.instance(
						toCreate.type, fields.literalValue(environment)));
	}
	@Override
	public boolean typeCheck() {
		// everything had to be typechecked on input,
		// LocalEnvironment.instance checks if the fields can be coerced
		return structs.typeCheckConstructor(toCreate.type, fields);
	}
}
