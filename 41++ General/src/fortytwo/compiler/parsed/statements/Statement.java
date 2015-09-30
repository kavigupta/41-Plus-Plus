package fortytwo.compiler.parsed.statements;

import java.util.Optional;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.Sentence;
import fortytwo.language.type.GenericType;
import fortytwo.vm.environment.OrderedEnvironment;
import fortytwo.vm.environment.type.AbstractTypeEnvironment;
import fortytwo.vm.environment.type.AbstractTypeEnvironment;
import fortytwo.vm.expressions.LiteralExpression;

/**
 * An abstract class representing something that can be executed against an
 * environment.
 */
public abstract class Statement implements Sentence {
	/**
	 * Whether or not this statement has been type checked
	 */
	private boolean isTypeChecked = false;
	/**
	 * The context of this statement
	 */
	private final Context context;
	/**
	 * @param context
	 *        the context of this statement
	 */
	protected Statement(Context context) {
		super();
		this.context = context;
	}
	/**
	 * If it is not typechecked, it typechecks it. Otherwise, it just outputs
	 * true.
	 */
	public final boolean isTypeChecked(AbstractTypeEnvironment environment) {
		if (!isTypeChecked) isTypeChecked = typeCheck(environment);
		return isTypeChecked;
	}
	/**
	 * This always returns true or throws an error. The return type is just to
	 * make the "success" condition explicit.
	 */
	protected abstract boolean typeCheck(AbstractTypeEnvironment environment);
	/**
	 * Executes this statement against the given local environment
	 * 
	 * @param environment
	 * @return the return value of this statement, if a return statement exists
	 *         somewhere within it
	 */
	public abstract Optional<LiteralExpression> execute(
			OrderedEnvironment environment);
	/**
	 * Undoes whatever {@link #execute(OrderedEnvironment)} accomplished on the
	 * local environment.
	 */
	public abstract void clean(OrderedEnvironment environment);
	/**
	 * Returns a type if and only if the given block has a return statement.
	 * Otherwise, returns {@link Optional#empty()}
	 * 
	 */
	public abstract Optional<GenericType> returnType(
			AbstractTypeEnvironment env);
	@Override
	public final Context context() {
		return context;
	}
}
