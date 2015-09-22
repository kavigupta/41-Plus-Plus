package fortytwo.compiler.parsed.statements;

import java.util.Optional;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.Sentence;
import fortytwo.language.type.GenericType;
import fortytwo.vm.environment.OrderedEnvironment;
import fortytwo.vm.environment.TypeEnvironment;
import fortytwo.vm.expressions.LiteralExpression;

public abstract class Statement implements Sentence {
	private boolean isTypeChecked = false;
	private final Context context;
	public Statement(Context context) {
		super();
		this.context = context;
	}
	public final boolean isTypeChecked(TypeEnvironment environment) {
		if (!isTypeChecked) isTypeChecked = typeCheck(environment);
		return isTypeChecked;
	}
	/**
	 * This always returns true or throws an error. The return type is just to
	 * make checking explicit
	 */
	protected abstract boolean typeCheck(TypeEnvironment environment);
	/**
	 * Executes this statement against the given local environment
	 * 
	 * @param environment
	 * @return the return value of this statement, if a return was found
	 */
	public abstract Optional<LiteralExpression> execute(
			OrderedEnvironment environment);
	public abstract void clean(OrderedEnvironment environment);
	public abstract Optional<GenericType> returnType(TypeEnvironment env);
	@Override
	public final Context context() {
		return context;
	}
}
