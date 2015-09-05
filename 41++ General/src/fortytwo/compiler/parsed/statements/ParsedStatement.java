package fortytwo.compiler.parsed.statements;

import java.util.Optional;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.Sentence;
import fortytwo.language.type.GenericType;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.expressions.LiteralExpression;

public abstract class ParsedStatement implements Sentence {
	private boolean isTypeChecked = false;
	private final Context context;
	public ParsedStatement(Context context) {
		super();
		this.context = context;
	}
	public final boolean isTypeChecked(StaticEnvironment environment) {
		if (!isTypeChecked) isTypeChecked = typeCheck(environment);
		return isTypeChecked;
	}
	/**
	 * This always returns true or throws an error. The return type is just to
	 * make checking explicit
	 */
	protected abstract boolean typeCheck(StaticEnvironment environment);
	/**
	 * Executes this statement against the given local environment
	 * 
	 * @param environment
	 * @return the return value of this statement, if a return was found
	 */
	public abstract Optional<LiteralExpression> execute(
			LocalEnvironment environment);
	public abstract void clean(LocalEnvironment environment);
	public abstract Optional<GenericType> returnType(StaticEnvironment env);
	@Override
	public final Context context() {
		return context;
	}
}
