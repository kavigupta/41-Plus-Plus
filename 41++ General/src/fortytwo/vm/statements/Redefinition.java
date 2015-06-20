package fortytwo.vm.statements;

import fortytwo.compiler.Context;
import fortytwo.language.SourceCode;
import fortytwo.language.field.Field;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.expressions.Expression;

public class Redefinition implements Statement {
	public final Field name;
	public final Expression value;
	private final Context context;
	public Redefinition(Field name, Expression value, Context context) {
		this.name = name;
		this.value = value;
		this.context = context;
	}
	@Override
	public void execute(LocalEnvironment environment) {
		environment.vars.redefine(name.name, value.literalValue(environment));
	}
	@Override
	public void clean(LocalEnvironment environment) {
		// no variables created
	}
	@Override
	public String toSourceCode() {
		return SourceCode.displayRedefinition(name, value);
	}
	@Override
	public boolean isSimple() {
		return true;
	}
	@Override
	public Context context() {
		return context;
	}
}
