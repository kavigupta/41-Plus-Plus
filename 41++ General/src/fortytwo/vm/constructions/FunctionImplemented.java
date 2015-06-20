package fortytwo.vm.constructions;

import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.declaration.FunctionDefinition;
import fortytwo.compiler.parsed.declaration.FunctionOutput;
import fortytwo.compiler.parsed.statements.ParsedStatement;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.GenericType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.PrimitiveTypeWithoutContext;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.expressions.LiteralExpression;

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
	 * The function output, which can be evaluated against the environment
	 * after the statements are executed. TODO allow multiple function output
	 * statements, simply allow first "unnested" one to end the function
	 */
	private final FunctionOutput r;
	/**
	 * Simple struct constructor
	 */
	public FunctionImplemented(FunctionDefinition f,
			List<ParsedStatement> body, FunctionOutput r) {
		this.f = f;
		this.body = body;
		this.r = r;
	}
	/**
	 * Contextualizes this function into one that can actually be used (linking
	 * to other functions is done here)
	 * 
	 * @param environment the environment against which to contextualize this
	 * @return an implemented function representing this function
	 */
	public FunctionImplemented contextualize(StaticEnvironment environment) {
		return this;
	}
	public boolean typeCheck(StaticEnvironment env) {
		StaticEnvironment local = StaticEnvironment.getChild(env);
		f.registerParameters(local);
		body.forEach(x -> x.typeCheck(local));
		return true;
	}
	@Override
	protected LiteralExpression apply(GlobalEnvironment env,
			List<LiteralExpression> inputs, TypeVariableRoster roster) {
		LocalEnvironment local = env.minimalLocalEnvironment();
		f.assignInputs(inputs, local);
		body.stream().forEach(x -> x.execute(local));
		LiteralExpression out = r.output == null ? null : r.output
				.literalValue(local);
		// no need to clean the local environment, as it will be garbage
		// collected after now.
		return out;
	}
	@Override
	public GenericType outputType() {
		StaticEnvironment local = StaticEnvironment.getDefault();
		f.registerParameters(local);
		body.forEach(x -> x.typeCheck(local));
		return r.output == null ? new PrimitiveType(
				PrimitiveTypeWithoutContext.VOID, Context.SYNTHETIC)
				: r.output.resolveType(local);
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
		result = prime * result + ((r == null) ? 0 : r.hashCode());
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
		if (r == null) {
			if (other.r != null) return false;
		} else if (!r.equals(other.r)) return false;
		return true;
	}
}
