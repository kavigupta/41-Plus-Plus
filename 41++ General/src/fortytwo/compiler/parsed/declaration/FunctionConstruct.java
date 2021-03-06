package fortytwo.compiler.parsed.declaration;

import java.util.HashMap;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.Sentence;
import fortytwo.compiler.parsed.statements.Suite;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.UnorderedEnvironment;
import fortytwo.vm.expressions.LiteralFunction;

/**
 * Represents a function that has been parsed but whose suite sub-declarations
 * have not been fully parsed into a static environment.
 */
public class FunctionConstruct implements Sentence {
	/**
	 * The function definition
	 */
	private final FunctionDefinition declaration;
	/**
	 * The body of the function, in the form of a series of sentences
	 */
	private final Suite suite;
	public FunctionConstruct(FunctionDefinition declaration, Suite suite) {
		this.declaration = declaration;
		this.suite = suite;
	}
	/**
	 * Register this function on the given type environment and function roster.
	 * 
	 * This will associate the mangled function name of this function with the
	 * function type defined in the type environment and the literal value of
	 * the function in the function roster.
	 * 
	 * @param environment
	 *        the type environment to place this in
	 * @param functions
	 *        the function map to add this to.
	 */
	public void register(UnorderedEnvironment environment,
			HashMap<VariableIdentifier, LiteralFunction> functions) {
		declaration.putReference(environment, functions, suite);
	}
	@Override
	public boolean isSimple() {
		return false;
	}
	@Override
	public String toSourceCode() {
		return declaration.toSourceCode() + suite.toSourceCode();
	}
	@Override
	public Context context() {
		return Context.sum(Context.sum(suite), declaration);
	}
	@Override
	public SentenceType kind() {
		return SentenceType.FUNCTION;
	}
	@Override
	public String toString() {
		return "FunctionConstruct [declaration=" + declaration.toSourceCode()
				+ ", suite=" + suite + "]";
	}
}
