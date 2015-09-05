package fortytwo.compiler.parsed.expressions;

import java.util.Optional;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.statements.ParsedStatement;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.GenericType;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.expressions.LiteralExpression;

/**
 * A class for representing an expression
 */
public abstract class Expression extends ParsedStatement {
	private ConcreteType type = null;
	protected Expression(Context context) {
		super(context);
	}
	/**
	 * The type of this expression. After the first invocation, it returns a
	 * cached copy, so a null environment can be passed.
	 * 
	 * @param env
	 *        the static environment
	 * @return the type of this expression
	 */
	public final ConcreteType type(StaticEnvironment env) {
		if (type == null) type = findType(env);
		return type;
	}
	@Override
	protected final boolean typeCheck(StaticEnvironment environment) {
		type(environment);
		return true;
	}
	protected abstract ConcreteType findType(StaticEnvironment env);
	/**
	 * @return the name of this expression, if it is a variable identifier
	 */
	public abstract Optional<VariableIdentifier> identifier();
	/**
	 * Determines the literal value of this expression given the local
	 * environment
	 * 
	 * @param environment
	 * @return the literal value of the expression
	 */
	public abstract LiteralExpression literalValue(
			LocalEnvironment environment);
	@Override
	public final Optional<LiteralExpression> execute(
			LocalEnvironment environment) {
		literalValue(environment);
		return Optional.empty();
	}
	@Override
	public final void clean(LocalEnvironment environment) {
		// no op deliberately
	}
	@Override
	public final boolean isSimple() {
		return true;
	}
	@Override
	public Optional<GenericType> returnType(StaticEnvironment env) {
		return Optional.empty();
	}
}
