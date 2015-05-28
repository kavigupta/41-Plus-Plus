package fortytwo.vm.statements;

import fortytwo.language.field.Field;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StructureRoster;
import fortytwo.vm.environment.VariableRoster;
import fortytwo.vm.expressions.Expression;

public class Definition implements Statement {
	public final Field toCreate;
	public final VariableRoster<Expression> fields;
	// LOWPRI quick/dirty solution. Fix.
	private StructureRoster structs;
	public Definition(Field toCreate, VariableRoster<Expression> fields,
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
	public void clean(LocalEnvironment environment) {
		environment.vars.deregister(toCreate.name);
	}
	@Override
	public boolean typeCheck() {
		return structs.typeCheckConstructor(toCreate.type, fields);
	}
}
