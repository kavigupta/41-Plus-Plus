package fortytwo.vm.constructions;

import java.util.List;
import java.util.Optional;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.statements.Suite;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.FunctionType;
import fortytwo.language.type.GenericType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.vm.environment.OrderedEnvironment;
import fortytwo.vm.environment.type.AbstractTypeEnvironment;
import fortytwo.vm.environment.type.NonTopTypeEnvironment;
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
	private final Suite body;
	private final String debugName;
	private final AbstractTypeEnvironment parentTypeFrame;
	/**
	 * Simple struct constructor
	 */
	public FunctionImplemented(AbstractTypeEnvironment parentTypeFrame,
			FunctionType type, List<VariableIdentifier> variables, Suite body,
			String debugName) {
		super(Context.sum(body), type, getImplementedFunction(variables, body));
		this.parentTypeFrame = parentTypeFrame;
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
	public FunctionImplemented contextualize(
			AbstractTypeEnvironment environment) {
		return this;
	}
	@Override
	public boolean typeCheck(AbstractTypeEnvironment env) {
		// ignores the input environment
		final NonTopTypeEnvironment local = this.createFrame();
		body.isTypeChecked(local);
		final Optional<GenericType> actual = body.returnType(local);
		if (type.outputType.equals(PrimitiveType.SYNTH_VOID)) {
			if (actual.isPresent()
					&& !actual.get().equals(PrimitiveType.SYNTH_VOID)) {
				TypingErrors.incorrectOutput(debugName, type.outputType,
						actual.get(), body);
			} else {
				return true;
			}
		}
		if (actual.isPresent()) {
			if (!actual.get().equals(type.outputType))
				TypingErrors.incorrectOutput(debugName, type.outputType,
						actual.get(), body);
		}
		return body.isTypeChecked(local);
	}
	public static FunctionImplementation getImplementedFunction(
			List<VariableIdentifier> variables, Suite body) {
		return (env, inputs, roster) -> {
			final OrderedEnvironment local = env.minimalLocalEnvironment();
			for (int i = 0; i < variables.size(); i++)
				local.vars.assign(variables.get(i), inputs.get(i));
			// no need to clean the local environment, as it will be garbage
			// collected after now.
			// if no return, return LiteralVoid, signifying the void marker
			return body.execute(local).orElse(LiteralVoid.INSTANCE);
		};
	}
	/**
	 * Registers the function's parameters' types on the given static
	 * environment (which incidentally should be newly minted)
	 */
	public NonTopTypeEnvironment createFrame() {
		NonTopTypeEnvironment environment = NonTopTypeEnvironment
				.getChild(parentTypeFrame);
		for (int i = 0; i < type.inputTypes.size(); i++)
			environment.addType(variables.get(i), (ConcreteType)
			// LOWPRI remove cast once generic typing is allowed
			type.inputTypes.get(i));
		return environment;
	}
	@Override
	public String toSourceCode() {
		return body.toSourceCode();
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
