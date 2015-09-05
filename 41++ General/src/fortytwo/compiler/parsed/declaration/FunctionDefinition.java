package fortytwo.compiler.parsed.declaration;

import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.Sentence;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.expressions.LiteralExpression;

/**
 * A class representing the initial definition of a function, which provides a
 * signature, the names of the input variables, and the context of the sentence.
 */
public class FunctionDefinition implements Sentence {
	/**
	 * The signature of this function
	 */
	public final FunctionSignature sig;
	private final List<VariableIdentifier> inputVariables;
	private final Context context;
	/**
	 * @param signature
	 *        the signature to be used
	 * @param inputVariables
	 *        the names of the input variables
	 * @param context
	 *        the context of the definition
	 */
	public FunctionDefinition(FunctionSignature signature,
			List<VariableIdentifier> inputVariables, Context context) {
		this.sig = signature;
		this.inputVariables = inputVariables;
		this.context = context;
	}
	/**
	 * Registers the function's parameters' types on the given static
	 * environment (which incidentally should be newly minted)
	 */
	public void registerParameters(StaticEnvironment environment) {
		for (int i = 0; i < sig.inputTypes.size(); i++)
			environment.addType(inputVariables.get(i), (ConcreteType)
			// LOWPRI remove cast once generic typing is allowed
			sig.inputTypes.get(i));
	}
	/**
	 * Registers this function's variables' values as given with the given
	 * local environment.
	 */
	public void assignInputs(List<LiteralExpression> inputs,
			LocalEnvironment local) {
		for (int i = 0; i < inputVariables.size(); i++)
			local.vars.assign(inputVariables.get(i), inputs.get(i));
	}
	@Override
	public SentenceType kind() {
		return SentenceType.DECLARATION_FUNCT;
	}
	@Override
	public boolean isSimple() {
		return true;
	}
	@Override
	public String toSourceCode() {
		return SourceCode.displayFunctionDefinition(sig, inputVariables);
	}
	@Override
	public Context context() {
		return context;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (context == null ? 0 : context.hashCode());
		result = prime * result
				+ (inputVariables == null ? 0 : inputVariables.hashCode());
		result = prime * result + (sig == null ? 0 : sig.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final FunctionDefinition other = (FunctionDefinition) obj;
		if (context == null) {
			if (other.context != null) return false;
		} else if (!context.equals(other.context)) return false;
		if (inputVariables == null) {
			if (other.inputVariables != null) return false;
		} else if (!inputVariables.equals(other.inputVariables)) return false;
		if (sig == null) {
			if (other.sig != null) return false;
		} else if (!sig.equals(other.sig)) return false;
		return true;
	}
}
