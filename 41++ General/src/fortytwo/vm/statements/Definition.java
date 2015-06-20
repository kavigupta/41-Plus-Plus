package fortytwo.vm.statements;

import fortytwo.compiler.Context;
import fortytwo.language.field.Field;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.VariableRoster;
import fortytwo.vm.expressions.Expression;

public class Definition implements Statement {
	public final Field toCreate;
	public final VariableRoster<Expression> fields;
	private final Context context;
	public Definition(Field toCreate, VariableRoster<Expression> fields,
			Context context) {
		this.toCreate = toCreate;
		this.fields = fields;
		this.context = context;
	}
	@Override
	public void execute(LocalEnvironment environment) {
		environment.vars.assign(toCreate.name,
				environment.global.staticEnv.structs.instance(toCreate,
						fields.literalValue(environment),
						toCreate.name.context()));
	}
	@Override
	public void clean(LocalEnvironment environment) {
		environment.vars.deregister(toCreate.name);
	}
	@Override
	public boolean isSimple() {
		return true;
	}
	@Override
	public String toSourceCode() {
		return null;
	}
	@Override
	public Context context() {
		return context;
	}
}
