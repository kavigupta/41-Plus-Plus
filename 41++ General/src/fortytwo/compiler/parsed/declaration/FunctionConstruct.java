package fortytwo.compiler.parsed.declaration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.Sentence;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.TypeEnvironment;
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
	private final List<Sentence> suite;
	public FunctionConstruct(FunctionDefinition declaration,
			List<Sentence> suite) {
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
	public void register(TypeEnvironment environment,
			HashMap<VariableIdentifier, LiteralFunction> functions) {
		declaration.putReference(environment, functions, suite);
	}
	@Override
	public boolean isSimple() {
		return false;
	}
	@Override
	public String toSourceCode() {
		return declaration.toSourceCode()
				+ (suite.size() == 0 ? "" : SourceCode.displaySeries(suite));
	}
	@Override
	public Context context() {
		final ArrayList<Sentence> s = new ArrayList<>(suite);
		suite.add(declaration);
		return Context.sum(s);
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
