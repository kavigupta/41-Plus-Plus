package fortytwo.vm.constructions;

import java.util.List;
import java.util.Optional;

import fortytwo.compiler.parsed.declaration.FunctionDefinition;
import fortytwo.compiler.parsed.declaration.FunctionOutput;
import fortytwo.compiler.parsed.statements.ParsedStatement;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.GenericType;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.errors.TypingErrors;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralVoid;

/**
 * A class representing a function that has been parsed from 41++ source
 */
public class FunctionImplemented extends Function42 {
	/**
	 * The function definition
	 */
	private final FunctionDefinition f;
	/**
	 * The function body, which is composed of statements.
	 */
	private final List<ParsedStatement> body;
	/**
	 * Simple struct constructor
	 */
	public FunctionImplemented(FunctionDefinition f,
			List<ParsedStatement> body) {
		this.f = f;
		this.body = body;
	}
	/**
	 * Contextualizes this function into one that can actually be used (linking
	 * to other functions is done here)
	 * 
	 * @param environment
	 *        the environment against which to contextualize this
	 * @return an implemented function representing this function
	 */
	public FunctionImplemented contextualize(StaticEnvironment environment) {
		return this;
	}
	public boolean typeCheck(StaticEnvironment env) {
		StaticEnvironment local = StaticEnvironment.getChild(env);
		f.registerParameters(local);
		for (ParsedStatement s : body) {
			Optional<GenericType> actual = s.returnType(local);
			s.isTypeChecked(local);
			if (actual.isPresent()) {
				if (!actual.get().equals(f.sig.outputType))
					TypingErrors.incorrectOutput(f.sig, actual.get(),
							(FunctionOutput) s);
			}
		}
		return true;
	}
	@Override
	protected LiteralExpression apply(GlobalEnvironment env,
			List<LiteralExpression> inputs, TypeVariableRoster roster) {
		LocalEnvironment local = env.minimalLocalEnvironment();
		f.assignInputs(inputs, local);
		for (ParsedStatement s : body) {
			Optional<LiteralExpression> exp = s.execute(local);
			if (exp.isPresent()) return exp.get();
		}
		// no need to clean the local environment, as it will be garbage
		// collected after now.
		// if no return, return LiteralVoid, signifying the void marker
		return LiteralVoid.INSTANCE;
	}
	@Override
	public GenericType outputType() {
		return f.sig.outputType;
	}
	@Override
	public FunctionSignature signature() {
		return f.sig;
	}
	/**
	 * @return the definition of this function
	 */
	public FunctionDefinition definition() {
		return f;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((f == null) ? 0 : f.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		FunctionImplemented other = (FunctionImplemented) obj;
		if (body == null) {
			if (other.body != null) return false;
		} else if (!body.equals(other.body)) return false;
		if (f == null) {
			if (other.f != null) return false;
		} else if (!f.equals(other.f)) return false;
		return true;
	}
}
