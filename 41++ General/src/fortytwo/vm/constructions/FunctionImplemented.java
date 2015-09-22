package fortytwo.vm.constructions;

import java.util.List;
import java.util.Optional;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.declaration.FunctionOutput;
import fortytwo.compiler.parsed.statements.Statement;
import fortytwo.language.SourceCode;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.FunctionType;
import fortytwo.language.type.GenericType;
import fortytwo.vm.environment.OrderedEnvironment;
import fortytwo.vm.environment.TypeEnvironment;
import fortytwo.vm.errors.TypingErrors;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralFunction;
import fortytwo.vm.expressions.LiteralVoid;

/**
 * A class representing a function that has been parsed from 41++ source
 */
public class FunctionImplemented extends LiteralFunction {
	private final List<VariableIdentifier> variables;
	/**
	 * The function body, which is composed of statements.
	 */
	private final List<Statement> body;
	private String debugName;
	/**
	 * Simple struct constructor
	 */
	public FunctionImplemented(FunctionType type,
			List<VariableIdentifier> variables, List<Statement> body,
			String debugName) {
		super(Context.sum(body), type, getImplementedFunction(variables, body));
		this.variables = variables;
		this.body = body;
		this.debugName = debugName;
	}
	/**
	 * Contextualizes this function into one that can actually be used (linking
	 * to other functions is done here)
	 * 
	 * @param environment
	 *        the environment against which to contextualize this
	 * @return an implemented function representing this function
	 */
	@Override
	public FunctionImplemented contextualize(TypeEnvironment environment) {
		return this;
	}
	@Override
	public boolean typeCheck(TypeEnvironment env) {
		final TypeEnvironment local = TypeEnvironment.getChild(env);
		registerParameters(local);
		for (final Statement s : body) {
			final Optional<GenericType> actual = s.returnType(local);
			s.isTypeChecked(local);
			if (actual.isPresent()) if (!actual.get().equals(type.outputType))
				TypingErrors.incorrectOutput(debugName, type.outputType,
						actual.get(), (FunctionOutput) s);
		}
		return true;
	}
	public static FunctionImplementation getImplementedFunction(
			List<VariableIdentifier> variables, List<Statement> body) {
		return (env, inputs, roster) -> {
			final OrderedEnvironment local = env.minimalLocalEnvironment();
			for (int i = 0; i < variables.size(); i++)
				local.vars.assign(variables.get(i), inputs.get(i));
			for (final Statement s : body) {
				final Optional<LiteralExpression> exp = s.execute(local);
				if (exp.isPresent()) return exp.get();
			}
			// no need to clean the local environment, as it will be garbage
			// collected after now.
			// if no return, return LiteralVoid, signifying the void marker
			return LiteralVoid.INSTANCE;
		};
	}
	/**
	 * Registers the function's parameters' types on the given static
	 * environment (which incidentally should be newly minted)
	 */
	public void registerParameters(TypeEnvironment environment) {
		for (int i = 0; i < type.inputTypes.size(); i++)
			environment.addType(variables.get(i), (ConcreteType)
			// LOWPRI remove cast once generic typing is allowed
			type.inputTypes.get(i));
	}
	@Override
	public String toSourceCode() {
		return body.size() == 0 ? "" : SourceCode.displaySeries(body);
	}
	@Override
	public boolean typedEquals(LiteralExpression obj) {
		FunctionImplemented other = (FunctionImplemented) obj;
		if (body == null) {
			if (other.body != null) return false;
		} else if (!body.equals(other.body)) return false;
		return true;
	}
}
