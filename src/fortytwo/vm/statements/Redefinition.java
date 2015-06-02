package fortytwo.vm.statements;

import fortytwo.compiler.Context;
import fortytwo.language.field.Field;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.errors.TypingErrors;
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
	public boolean typeCheck() {
		if (name.type.equals(value.resolveType())) return true;
		TypingErrors.redefinitionTypeMismatch(name, value);
		// should never get here
		return false;
	}
	@Override
	public String toSourceCode() {
		// TODO Auto-generated method stub
		return null;
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
